package co.edu.udea.cmovil.gr5.yamba;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import java.util.List;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
    private static final String TAG = RefreshService.class.getSimpleName();
    private boolean isEmpty;

    public RefreshService() {
        super("RefreshService");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "OnCreated");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "OnStarted");
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        final String username = prefs.getString("username", "");
        final String password = prefs.getString("password", "");
        // Verificamos que usuario y contraseña no esten vacias
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            isEmpty=true;
            return;
        }

        DbHelper dbHelper = new DbHelper(this); //instancia de DbHelper
        SQLiteDatabase db = dbHelper.getWritableDatabase(); //obtener instancia de la BD
        ContentValues values = new ContentValues();

        Log.d(TAG, "onStarted");
        YambaClient cloud = new YambaClient(username, password);
        try {
            List<YambaStatus> timeline = cloud.getTimeline(20); //obtener ultimos 20 estados linea de tiempo
            for (YambaStatus status : timeline) {
                Log.d(TAG,String.format("%s: %s",  status.getUser(),status.getMessage()));
                //Values al DB
                values.clear(); //Se utiliza el mismo objeto, por ello hay que limpiarlo en cada iteracion
                values.put(StatusContract.Column.ID, status.getId()); //Se pasan pares nombre-valor
                values.put(StatusContract.Column.USER, status.getUser());
                values.put(StatusContract.Column.MESSAGE, status.getMessage());
                values.put(StatusContract.Column.CREATED_AT, status.getCreatedAt().getTime());
                db.insertWithOnConflict(StatusContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE); //Se guarda el estado en la BD
            }
        } catch (YambaClientException e) {
            Log.e(TAG, "Error al refrescar linea de tiempo", e);
            e.printStackTrace();
        }
        return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isEmpty){
            Toast.makeText(this, "Favor actualizar nombre usuario y contraseña", Toast.LENGTH_LONG).show();
            isEmpty = false;
        }
        Log.d(TAG, "onDestroyed");
    }
}
