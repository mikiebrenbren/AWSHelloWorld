package tutorial.google.com.awshelloworld.aws;

import android.app.IntentService;
import android.content.Intent;


/**
 * Created by michaelbrennan on 3/14/15.
 */
public final class ExampleIntentService extends IntentService  {


    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public ExampleIntentService() {
        super("ExampleIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Put your code you want to run here
    }
}