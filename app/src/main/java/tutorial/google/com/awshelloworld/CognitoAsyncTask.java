package tutorial.google.com.awshelloworld;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michael.brennan on 3/13/15. This world
 */
public class CognitoAsyncTask extends AsyncTask<Void, Void, Void>{

    public final String TAG = this.getClass().getSimpleName();

    private Context mContext;

    public CognitoAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d(TAG, "Inside doInBackground");

        GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext.getApplicationContext());
        AccountManager am = AccountManager.get(mContext);
        Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String token = null;
        try {
            token = GoogleAuthUtil.getToken(mContext.getApplicationContext(), accounts[0].name,
                    "audience:server:client_id:913110743460-alic4npfdaip8co0jjni99h2qbo144br.apps.googleusercontent.com");
        } catch (IOException e) {
            Log.i(TAG,"IOException " + e.getMessage());
        } catch (GoogleAuthException e) {
            Log.i(TAG, "GoogleAuthException " + e.getMessage());
        }
        Map<String, String> logins = new HashMap<>();
        logins.put("accounts.google.com", token);
        LoginActivity.cognitoProvider.withLogins(logins);

        Log.d(TAG, "Begginning Sync");
        // Initialize the Cognito Sync client
        CognitoSyncManager syncClient = new CognitoSyncManager(
                mContext,
                Regions.US_EAST_1, // Region
                LoginActivity.cognitoProvider);

        // Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        dataset.put("myKey", "myValue");
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                //Your handler code here
                Log.d(TAG, "Successful sync");
            }
        });
        Log.d(TAG, "End of Sync");


        try {
            Toast.makeText(mContext, "View Logs," + "\n here is the account name " + accounts[0].name + "\n here is the token " + token,
                    Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Log.i(TAG, "Exception" + e.getCause() + " " + e.getMessage());
        }
        Log.i(TAG, "Logs are beginning");
        Log.i(TAG, "Context: " + mContext.toString());
        Log.i(TAG, "Application Context: " + mContext.getApplicationContext().toString());
        Log.i(TAG,accounts[0].name);
        Log.i(TAG, String.valueOf(logins.containsKey("accounts.google.com")));
        Log.i(TAG, "thisis the token:" + token + "");
        Log.i(TAG, "End of async task");

//        Toast.makeText(mContext.getApplicationContext(), "this is my Toast message!!! =)",
//                Toast.LENGTH_LONG).show();

//        Log.d(TAG, "my ID is"  + LoginActivity.cognitoProvider.getIdentityId());


        return null;
    }
}
