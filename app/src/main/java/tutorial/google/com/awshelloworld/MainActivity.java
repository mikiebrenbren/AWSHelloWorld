package tutorial.google.com.awshelloworld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;


public class MainActivity extends ActionBarActivity {

    public final String TAG = this.getClass().getSimpleName();

    protected TextView mCreateAccountTextView;
    protected EditText mPasswordEditText;
    protected EditText mEmailEditText;
    protected Button mLoginButton;
    public static CognitoCachingCredentialsProvider cognitoProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mEmailEditText = (EditText) findViewById(R.id.emailEditText);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mCreateAccountTextView = (TextView) findViewById(R.id.textViewCreateAccount);

        mCreateAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Create account text view has been pressed");
                Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
                startActivity(intent);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Login button has been pushed");
            }
        });

        // Initialize the Amazon Cognito credentials provider
        cognitoProvider = new CognitoCachingCredentialsProvider(
                this, // Context
                "us-east-1:1da08193-49c6-426f-ba03-194a87b451bb", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        new CognitoAsyncTask().execute();

        AmazonDynamoDB client = new AmazonDynamoDBClient(cognitoProvider);


    }
}
