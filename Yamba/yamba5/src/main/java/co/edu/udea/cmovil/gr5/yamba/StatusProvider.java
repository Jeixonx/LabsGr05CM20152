package co.edu.udea.cmovil.gr5.yamba;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.util.Log;

public class StatusProvider extends ContentProvider {
    private static final String TAG = StatusProvider.class.getSimpleName();
    private DbHelper dbhelper;

    public StatusProvider() {
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreated");
        dbhelper = new DbHelper(getContext());
        return false;
    }

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE,
                StatusContract.STATUS_DIR);
        /*si la URI no finaliza con un número, se refiere a la coleccion */
        sURIMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE + "/#", StatusContract.STATUS_ITEM);
        /* Si la URI finaliza con un número, se refiere a un estado*/

    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)){
            case StatusContract.STATUS_DIR:
                Log.d(TAG, "gotType: " + StatusContract.STATUS_TYPE_DIR);
                return StatusContract.STATUS_TYPE_DIR;
            case StatusContract.STATUS_ITEM:
                Log.d(TAG, "gotType: " + StatusContract.STATUS_TYPE_ITEM);
                return StatusContract.STATUS_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);
        }
    }//retorna el NIME type apropiado que se definió en StatusContract


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri ret=null;

        if (sURIMatcher.match(uri) != StatusContract.STATUS_DIR){
            //Verifica que no sea el directorio completo
            throw new IllegalArgumentException("Illegal uri: " + uri);
        }

        SQLiteDatabase db = dbhelper.getWritableDatabase();//Obtiene instancia de la BD
        long rowId = db.insertWithOnConflict(StatusContract.TABLE, null,
                values, SQLiteDatabase.CONFLICT_IGNORE);//inserta

        //explicar estas lineas
        if(rowId != -1){
            long id = values.getAsLong(StatusContract.Column.ID);
            Log.d(TAG, "inserted uri: "+ ret);
            // Notificar que la informacion para esta uri ha cambiado
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return ret;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }






    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
