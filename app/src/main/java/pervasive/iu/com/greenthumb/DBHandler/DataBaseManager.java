package pervasive.iu.com.greenthumb.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import pervasive.iu.com.greenthumb.Model.TasksPOJO;

/**
 * Created by madrinathapa on 3/25/17.
 */

public class DataBaseManager extends SQLiteOpenHelper {

    private String createTables[] = {DataBaseQuery.CREATE_TABLE_TASKS};
    private static String databaseName = "IOTFarming";
    private static int dataBaseVersion=1;
    private static Context context=null;
    private static SQLiteDatabase sqliteDatabase=null;
    private static DataBaseManager databaseManagerInstance=null;

    public DataBaseManager(Context context){
        super(context, DataBaseManager.databaseName, null, DataBaseManager.dataBaseVersion);
    }

    public DataBaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public DataBaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler){
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        for (String createTable : createTables){
            sqLiteDatabase.execSQL(createTable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public synchronized void insertTaskDetails(ContentValues contentValues){
        Cursor cursor=getDatabase().rawQuery("SELECT * FROM " + DataBaseQuery.TABLE_TASKS + " WHERE " +
                DataBaseQuery.S_NO + " =? ", new String[]{String.valueOf(contentValues.get(DataBaseQuery.S_NO))});

        Log.e("cursor ", "c "+cursor.getCount());
        if(cursor.getCount()==0){
            long insert_result=getDatabase().insert(DataBaseQuery.TABLE_TASKS, null, contentValues);
        }
    }

    public synchronized void updateTaskDetails(ContentValues contentValues){
        Cursor cursor=getDatabase().rawQuery("SELECT * FROM " + DataBaseQuery.TABLE_TASKS + " WHERE " +
                DataBaseQuery.S_NO + " =? ", new String[]{String.valueOf(contentValues.get(DataBaseQuery.S_NO))});
        if(cursor.getCount()>0){
            long update_result=getDatabase().update(DataBaseQuery.TABLE_TASKS, contentValues,
                    DataBaseQuery.S_NO+"=?", new String[]{String.valueOf(contentValues.get(DataBaseQuery.S_NO))});
        }
    }

    public synchronized static SQLiteDatabase getDatabase(){
        if(sqliteDatabase==null){
            sqliteDatabase=DataBaseManager.getInstance(DataBaseManager.context).getWritableDatabase();
        }
        return DataBaseManager.sqliteDatabase;
    }

    public synchronized static void setContext(Context context){
        DataBaseManager.context=context;
    }

    public static DataBaseManager getInstance(Context context){
        if(databaseManagerInstance==null && DataBaseManager.context!=null){
            databaseManagerInstance=new DataBaseManager(DataBaseManager.context);
        }
        return databaseManagerInstance;
    }

    public synchronized <T> ArrayList<T> retrieveTablerows(String tableName, String where, String[] args){
        ArrayList<T> table_rows=new ArrayList<T>();
        Cursor cursor=null;

        try{
            if(where==null){
                cursor=getDatabase().rawQuery("SELECT * FROM " + tableName, null); //NO I18N
            }
            else{
                cursor=getDatabase().rawQuery("SELECT * FROM "+tableName+" WHERE "+where+"=?",args); //NO I18N
            }
            table_rows=retrieve_rows(tableName,cursor);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.e("table rows", table_rows+" row");
        return table_rows;
    }

    private synchronized <T> ArrayList<T> retrieve_rows(String tableName, Cursor cursor){
        ArrayList<T> rows=new ArrayList<T>();
        if(cursor.moveToFirst()){
            do{
                TasksPOJO pojo_object=new TasksPOJO();
                pojo_object.setSno(cursor.getInt(0));
                pojo_object.setTaskDescription(cursor.getString(1));
                pojo_object.setTaskPriority(cursor.getString(2));
                pojo_object.setTaskDeadLine(cursor.getString(3));
                pojo_object.setTaskAssignedTo(String.valueOf(cursor.getString(4)));
                pojo_object.setTaskstatus(String.valueOf(cursor.getString(5)));
                pojo_object.setGardenname(cursor.getString(6));
                pojo_object.setGardenid(cursor.getString(7));
                pojo_object.setPlotid(cursor.getString(8));
                pojo_object.setPlotname(cursor.getString(9));
                rows.add((T)pojo_object);
            }while(cursor.moveToNext());
        }
        return rows;
    }

}
