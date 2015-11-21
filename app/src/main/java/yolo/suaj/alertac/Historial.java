package yolo.suaj.alertac;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alumno on 14/11/2015.
 */
public class Historial extends SQLiteOpenHelper {

    //Atributo para especificar tabla

    private String sqlCreate = "CREATE TABLE HISTORIAL (fecha TEXT NOT NULL, accion TEXT NOT NULL, numero INTEGER, mensaje TEXT NOT NULL)";


    public Historial(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        //ELIMINACIÓN, migración según el versionado
        db.execSQL("DROP TABLE IF EXISTS HISTORIAL");

    }
}
