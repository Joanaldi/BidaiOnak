package com.example.bidaionak;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Bd extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "destinos.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_DESTINOS = "destinos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_COSTE = "coste";
    public static final String COLUMN_TRANSPORTE = "transporte";

    public Bd(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_DESTINOS = "CREATE TABLE " + TABLE_DESTINOS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NOMBRE + " TEXT," +
                COLUMN_COSTE + " DOUBLE," +
                COLUMN_TRANSPORTE + " TEXT" +
                ")";
        db.execSQL(CREATE_TABLE_DESTINOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESTINOS);
        onCreate(db);
    }
    public long nuevoDestino(String nombre, String coste, String trasporte) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_COSTE, coste);
        values.put(COLUMN_TRANSPORTE, trasporte);
        long id = db.insert(TABLE_DESTINOS, null, values);

        return id;
    }
    public int editarDestino(Destino destino) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, destino.getNombre());
        values.put(COLUMN_COSTE, destino.getCosteTotal());
        values.put(COLUMN_TRANSPORTE, destino.getTransporte());
        int rowsAffected = db.update(TABLE_DESTINOS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(destino.getId())});


        return rowsAffected;
    }
    public void deleteTask(long taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DESTINOS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskId)});

    }

    public List<Destino> getAllTasks() {
        List<Destino> Destinolist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DESTINOS, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
                    int columnIndexNombre = cursor.getColumnIndex(COLUMN_NOMBRE);
                    int columnIndexCoste = cursor.getColumnIndex(COLUMN_COSTE);
                    int columnIndexTransporte = cursor.getColumnIndex(COLUMN_TRANSPORTE);

                    do {
                        long id = cursor.getInt(columnIndexId);
                        String nombre = cursor.getString(columnIndexNombre);
                        String coste = cursor.getString(columnIndexCoste);
                        String transporte = cursor.getString(columnIndexTransporte);

                        Destino destino = new Destino(id,nombre, coste, transporte);
                        Destinolist.add(destino);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        db.close();
        return Destinolist;
    }

}

