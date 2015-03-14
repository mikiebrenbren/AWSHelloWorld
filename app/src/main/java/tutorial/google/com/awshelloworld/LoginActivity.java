package tutorial.google.com.awshelloworld;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//cognito imports
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
//google+ imports
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
//import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;


public class LoginActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //VIEW
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mEmailEditText = (EditText) findViewById(R.id.emailEditText);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mCreateAccountTextView = (TextView) findViewById(R.id.textViewCreateAccount);

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

        //instantiating google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        // Initialize the Amazon Cognito credentials provider
        cognitoProvider = new CognitoCachingCredentialsProvider(
                this, // Context
                "us-east-1:1da08193-49c6-426f-ba03-194a87b451bb", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        new CognitoAsyncTask().execute();

        //able to make dynamodb client  //TODO This can probably be removed later
        AmazonDynamoDB client = new AmazonDynamoDBClient(cognitoProvider);

    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void onConnected(Bundle connectionHint) {
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(result.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }

    }
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

//    int errorCode = GooglePlusUtil.checkGooglePlusApp(this);
//    if (errorCode != GooglePlusUtil.SUCCESS) {
//        GooglePlusUtil.getErrorDialog(errorCode, this, 0).show();
//    }

}
