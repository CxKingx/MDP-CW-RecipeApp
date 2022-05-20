package cn.edu.nottingham.s20125628.recipecw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG="Database Helper";
    private static final String TABLE_NAME="Recipe";
    private static final String COL1="ID";
    private static final String COL2="RecipeTitle";
    private static final String COL3="RecipeDescription";
    private static final String COL4="RecipeRating";
    private static final String COL5="RecipeIngredients";
    private static final String COL6="VideoTitle";
    private static final String COL7="VideoPath";

    public DatabaseHelper(Context context){
        super(context,TABLE_NAME,null,1);
    }

    //Create the whole databse
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

            String createTable = "CREATE TABLE " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL2 +" TEXT,"+
                    COL3 +" TEXT,"+
                    COL4 +" TEXT,"+
                    COL5 +" TEXT,"+
                    COL6 +" TEXT,"+
                    COL7 +" TEXT)";


        sqLiteDatabase.execSQL(createTable);
        Log.d(TAG, "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    // Add data to new row
    public boolean addData(String Title,String Description , String Rating , String Ingredients, String VideoTitle, String VideoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2, Title);
        contentValues.put(COL3, Description);
        contentValues.put(COL4, Rating);
        contentValues.put(COL5, Ingredients);
        contentValues.put(COL6, VideoTitle);
        contentValues.put(COL7, VideoPath);


        Log.d(TAG, "addData: Adding " + Title + " to " + TABLE_NAME);
        Log.d(TAG, "addData: Adding " + Description + " to " + TABLE_NAME);
        Log.d(TAG, "addData: Adding " + Rating + " to " + TABLE_NAME);
        Log.d(TAG, "addData: Adding " + Ingredients + " to " + TABLE_NAME);
        Log.d(TAG, "addData: Adding " + VideoTitle + " to " + TABLE_NAME);
        Log.d(TAG, "addData: Adding " + VideoPath + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Get Each row in cursor
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        //Log.d(TAG, "table is:"+data.getString(0)+" "+data.getString(1)+" "+data.getString(2)+" "+data.getString(3));
        return data;
    }


    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        //String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
          //      " WHERE " + COL2 + " = '" + name + "'";
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newName);

        db.execSQL(query);
    }
    // Update A specific row's Rating
    public void updateRating(String newRating,int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL4 +
                " = '" + newRating + "' WHERE " + COL1 + " = '" + id + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting rating to " + newRating);

        db.execSQL(query);
    }

    //Delete the whole row
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + (id) + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }
}
