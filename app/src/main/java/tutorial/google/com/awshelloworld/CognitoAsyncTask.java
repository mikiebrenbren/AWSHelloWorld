package tutorial.google.com.awshelloworld;

import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;

/**
 * Created by michael.brennan on 3/13/15. This world
 */
public class CognitoAsyncTask extends AsyncTask<Void, Void, Void>{

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected Void doInBackground(Void... params) {
        Log.d(TAG, "Inside doInBackground");

        Log.d(TAG, "my ID is"  + MainActivity.cognitoProvider.getIdentityId());


        return null;
    }
}
