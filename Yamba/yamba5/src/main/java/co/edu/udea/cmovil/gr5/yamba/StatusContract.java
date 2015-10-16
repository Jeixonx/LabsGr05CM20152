package co.edu.udea.cmovil.gr5.yamba;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Imran on 11-Oct-15.
 */
public class StatusContract {
    public static final String DB_NAME = "timeline.db"; //nombre de la BD
    public static final int DB_VERSION = 1; //Version de DB
    public static final String TABLE = "status"; //Nombre de la tabla
    public static final String DEFAULT_SORT = Column.CREATED_AT + " DESC"; //Para ordenar descendientemente por
    //fecha de creación


    public static final String AUTHORITY = "co.edu.udea.cmovil.gr5.yamba.StatusProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final int STATUS_ITEM = 1;
    public static final int STATUS_DIR = 2;

    public static final String STATUS_TYPE_ITEM = "vnd.android.cursor.item/vnd.co.edu.udea.cmovil.gr5.yamba.provider.status";
    //NIME Type para un estado
    public static final String STATUS_TYPE_DIR = "vnd.android.cursor.dir/vnd.co.edu.udea.cmovil.gr5.yamba.provider.status";
    //NIME type para el directorio de todos los estados

    public class Column{ //Columnas de la TABLA
        public static final String ID = BaseColumns._ID;
        public static final String USER = "user";
        public static final String MESSAGE = "message";
        public static final String CREATED_AT = "created_at";
    }
}
