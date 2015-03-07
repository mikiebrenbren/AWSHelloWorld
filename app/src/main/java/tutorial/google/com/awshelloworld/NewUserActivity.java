package tutorial.google.com.awshelloworld;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class NewUserActivity extends ActionBarActivity {

    protected EditText mFirstNameEditText;
    protected EditText mLastNameEditText;
    protected EditText mEmailEditText;
    protected EditText mPasswordEditText;
    protected Button mCreateAccountButton;


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
