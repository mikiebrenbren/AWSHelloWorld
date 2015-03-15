package tutorial.google.com.awshelloworld;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
//google
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

public class SuccessfulLoginActivity extends ActionBarActivity  {
    public final String TAG = this.getClass().getSimpleName();
    Button mLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_login);

        mLogoutButton = (Button) findViewById(R.id.logoutButton);
        mLogoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                if (view.getId() == R.id.logoutButton) {
//                    if (mGoogleApiClient.isConnected()) {
//                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                        mGoogleApiClient.disconnect(); //TODO looking in to passing GoogleApiClient or making it application level
//                        mGoogleApiClient.connect();
                        Intent intent = new Intent(SuccessfulLoginActivity.this, LoginActivity.class);
                        startActivity(intent);
//                    }
//                }
                Log.i(TAG, "Inside on click");
            }
        });
    }
}
