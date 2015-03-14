package tutorial.google.com.awshelloworld;

import android.app.FragmentTransaction;
import android.content.Intent;
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
//ddb
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import com.google.android.gms.common.ConnectionResult;
import tutorial.google.com.awshelloworld.google.GoogleApiClientFragment;




public class LoginActivity extends ActionBarActivity {

//    implements
//    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener

    //log tag
    public final String TAG = this.getClass().getSimpleName();

    //view
    protected TextView mCreateAccountTextView;
    protected EditText mPasswordEditText;
    protected EditText mEmailEditText;
    protected Button mLoginButton;

    public static CognitoCachingCredentialsProvider cognitoProvider;

    private boolean mSignInClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can
 * resolve them when the user clicks sign-in.
 */
    private ConnectionResult mConnectionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //VIEW
        mEmailEditText = (EditText) findViewById(R.id.emailEditText);
        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mCreateAccountTextView = (TextView) findViewById(R.id.textViewCreateAccount);

        //TODO this is where I left off, the google+ button on the example has 'this' as a parameter not an onclicklistener
        findViewById(R.id.google_sign_in_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "Gmail login button has been pressed");

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

        //initialize helper fragment for google+ api client
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(new GoogleApiClientFragment(), GoogleApiClientFragment.class.getSimpleName()).commit();

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

//    /* A helper method to resolve the current ConnectionResult error. */
//    private void resolveSignInError() {
//        if (mConnectionResult.hasResolution()) {
//            try {
//                mIntentInProgress = true;
//                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
//                        RC_SIGN_IN, null, 0, 0, 0);
//            } catch (IntentSender.SendIntentException e) {
//                // The intent was canceled before it was sent.  Return to the default
//                // state and attempt to connect to get an updated ConnectionResult.
//                mIntentInProgress = false;
//                mGoogleApiClient.connect();
//            }
//        }
//    }



    //    int errorCode = GooglePlusUtil.checkGooglePlusApp(this);
//    if (errorCode != GooglePlusUtil.SUCCESS) {
//        GooglePlusUtil.getErrorDialog(errorCode, this, 0).show();
//    }

}