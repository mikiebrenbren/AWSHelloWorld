package tutorial.google.com.awshelloworld;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;


public class NewUserActivity extends ActionBarActivity {

    protected EditText mFirstNameEditText;
    protected EditText mLastNameEditText;
    protected EditText mEmailEditText;
    protected EditText mPasswordEditText;
    protected Button mCreateAccountButton;

    protected CognitoCachingCredentialsProvider mcredentialsProvider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mFirstNameEditText = (EditText) findViewById(R.id.editTextFirstName);
        mLastNameEditText = (EditText) findViewById(R.id.editTextLastName);
        mEmailEditText = (EditText) findViewById(R.id.editTextCreateEmail);
        mPasswordEditText = (EditText) findViewById(R.id.editTextCreatePassword);
        mCreateAccountButton = (Button) findViewById(R.id.createAccountButton);

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("First Name", mFirstNameEditText.getText().toString());
                Log.i("Last Name", mLastNameEditText.getText().toString());
                Log.i("Email", mEmailEditText.getText().toString());
                Log.i("Password", mPasswordEditText.getText().toString());

            }
        });
    }

}
