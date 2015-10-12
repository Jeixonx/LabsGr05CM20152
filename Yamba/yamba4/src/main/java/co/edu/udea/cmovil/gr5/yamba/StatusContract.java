package co.edu.udea.cmovil.gr5.yamba;

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

    public class Column{ //Columnas de la TABLA
        public static final String ID = BaseColumns._ID;
        public static final String USER = "user";
        public static final String MESSAGE = "message";
        public static final String CREATED_AT = "created_at";
    }
}
