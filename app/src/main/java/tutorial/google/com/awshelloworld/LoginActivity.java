package tutorial.google.com.awshelloworld;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//cognito imports
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
//ddb
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
//google
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //log tag
    public final String TAG = this.getClass().getSimpleName();

    //view
    protected TextView mCreateAccountTextView;
    protected EditText mPasswordEditText;
    protected EditText mEmailEditText;
    protected Button mLoginButton;

    public static CognitoCachingCredentialsProvider cognitoProvider;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /*
            A flag indicating that a PendingIntent is in progress and prevents
            us from starting further intents.
     */
    private boolean mIntentInProgress;

    /*
    * Track whether the sign-in button has been clicked so that we know to resolve
    * all issues preventing sign-in without waiting.
    */
    private boolean mSignInClicked;

    /*
     * Store the connection result from onConnectionFailed callbacks so that we can
     * resolve them when the user clicks sign-in.
     */
    private ConnectionResult mConnectionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize the Amazon Cognito credentials provider
        cognitoProvider = new CognitoCachingCredentialsProvider(
                this, // Context
                "us-east-1:1da08193-49c6-426f-ba03-194a87b451bb", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        //instantiating google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        //VIEW
        mEmailEditText = (EditText) findViewById(R.id.emailEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mCreateAccountTextView = (TextView) findViewById(R.id.textViewCreateAccount);

        findViewById(R.id.google_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Gmail login button has been pressed");
                if (v.getId() == R.id.google_sign_in_button
                        && !mGoogleApiClient.isConnecting()) {
                    mSignInClicked = true;
                    resolveSignInError();
                    CognitoAsyncTask cognitoAsyncTask = new CognitoAsyncTask(LoginActivity.this);
                    cognitoAsyncTask.execute();
                }
            }
        });

        mCreateAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Create account text view has been pressed");
                Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
                startActivity(intent);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Login button has been pushed");
            }
        });

        //able to make dynamodb client  //TODO This can probably be removed later
        AmazonDynamoDB client = new AmazonDynamoDBClient(cognitoProvider);

        //TODO In case I want to go back to using a fragment
//        //initialize helper fragment for google+ api client
//        fragmentTransaction = getFragmentManager().beginTransaction();
//        mGoogleApiClientFragment = new GoogleApiClientFragment();
//        fragmentTransaction.add(mGoogleApiClientFragment, mGoogleApiClientFragment.getClass().getSimpleName());

    }

    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    public void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInClicked = false;
        Intent intent = new Intent(this, SuccessfulLoginActivity.class);
        startActivity(intent);
        Log.d(TAG, "User login successful");
    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

//    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
//        if (requestCode == RC_SIGN_IN) {
//            mIntentInProgress = false;
//
//            if (!mGoogleApiClient.isConnecting()) {
//                mGoogleApiClient.connect();
//            }
//        }
//    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    //    //google api client //TODO method modified below
//    public void onConnectionFailed(ConnectionResult result) {
//        if (!mIntentInProgress && result.hasResolution()) {
//            try {
//                mIntentInProgress = true;
//                startIntentSenderForResult(result.getResolution().getIntentSender(),
//                        RC_SIGN_IN, null, 0, 0, 0);
//            } catch (IntentSender.SendIntentException e) {
//                // The intent was canceled before it was sent.  Return to the default
//                // state and attempt to connect to get an updated ConnectionResult.
//                mIntentInProgress = false;
//                mGoogleApiClient.connect();
//            }
//        }
//
//    }

    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks
            // 'sign-in'.
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }


    //    int errorCode = GooglePlusUtil.checkGooglePlusApp(this);
//    if (errorCode != GooglePlusUtil.SUCCESS) {
//        GooglePlusUtil.getErrorDialog(errorCode, this, 0).show();
//    }

}