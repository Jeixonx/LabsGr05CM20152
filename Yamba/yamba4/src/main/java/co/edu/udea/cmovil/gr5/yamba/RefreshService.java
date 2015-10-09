package co.edu.udea.cmovil.gr5.yamba;

import android.app.IntentService;
import android.content.Intent;
import java.util.List;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientException;
import com.thenewcircle.yamba.client.YambaStatus;

/**
 * Created by imran on 9/10/15.
 */
public class RefreshService extends IntentService {
    private static final String TAG = "RefreshService";

    public RefreshService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this); //
        final String username = prefs.getString("username", "");
        final String password = prefs.getString("password", "");
        // Check that username and password are not empty
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            //
            Toast.makeText(this,
                    "Please update your username and password",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Log.d(TAG, "onStarted");
        YambaClient cloud = new YambaClient(username, password);
        try {
            List<YambaStatus> timeline = cloud.getTimeline(20);
            for (AsyncTask.Status status : timeline) {
                Log.d(TAG,String.format("%s: %s",  status.getUser(),status.getMessage()));
            }
        } catch (YambaClientException e) {
            Log.e(TAG, "Failed to fetch the timeline", e);
            e.printStackTrace();
        }
        return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyed");
    }
}
