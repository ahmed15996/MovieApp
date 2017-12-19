package com.example.aninterface.movieapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.aninterface.movieapp.Model.DatabaseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;

/**
 * Created by interface on 10/12/2017.
 */

public class Database extends SQLiteOpenHelper {

    private static final int version = 1;

    private static final String ID = "id";
    private static final String MOVIE_ID = "movie_id";
    private static final String TV_ID = "tv_id";
    private static final String NAME = "name";
    private static final String POSTERR_PATH = "poster_path";


    public Database(Context context) {
        super(context, Constanc.DB_NAME, null, version);
    }

    private String createTable(String table, String id) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(table);
        builder.append("(");
        builder.append(ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,");
        builder.append(id + " TEXT ,");
        builder.append(NAME + " TEXT,");
        builder.append(POSTERR_PATH + " TEXT);");

        return builder.toString();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(createTable(Constanc.MOVIE_TABLE, MOVIE_ID));
        sqLiteDatabase.execSQL(createTable(Constanc.TV_TABLE, TV_ID));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constanc.MOVIE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constanc.TV_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean add(String table, DatabaseModel model) {
        boolean isInsert = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (table.equals(Constanc.MOVIE_TABLE))
            values.put(MOVIE_ID, model.getMovie_id());
        else
            values.put(TV_ID, model.getMovie_id());
        values.put(NAME, model.getMovie_name());
        values.put(POSTERR_PATH, model.getPoster_path());
        long insert = db.insert(table, null, values);
        if (insert != -1)
            isInsert = true;
        db.close();
        return isInsert;
    }

    public void delete(String table, String movie_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (table.equals(Constanc.MOVIE_TABLE))
            db.delete(table, MOVIE_ID + "=?", new String[]{movie_id});
        else
            db.delete(table, TV_ID + "=?", new String[]{movie_id});
        db.close();
    }

    public boolean selectItem(String table, String movie_id) {
        boolean isExists = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (table.equals(Constanc.MOVIE_TABLE)) {
            cursor = db.query(table, new String[]{}, MOVIE_ID + "=?", new String[]{movie_id}, null, null,
                    null);
        } else {
            cursor = db.query(table, new String[]{}, TV_ID + "=?", new String[]{movie_id}, null, null,
                    null);
        }

        if (cursor.getCount() == 1) {
            isExists = true;
        }
        db.close();
        return isExists;
    }

    public List<DatabaseModel> selectAll(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<DatabaseModel> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + table, null);
        if (cursor.moveToFirst()) {

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(ID));
                String movie_id ;
                if (table.equals(Constanc.MOVIE_TABLE))
                    movie_id = cursor.getString(cursor.getColumnIndex(MOVIE_ID));
                else
                    movie_id = cursor.getString(cursor.getColumnIndex(TV_ID));


                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String poster = cursor.getString(cursor.getColumnIndex(POSTERR_PATH));
                list.add(new DatabaseModel(String.valueOf(id), movie_id, name, poster));
            }

        }
        return list;
    }
}
