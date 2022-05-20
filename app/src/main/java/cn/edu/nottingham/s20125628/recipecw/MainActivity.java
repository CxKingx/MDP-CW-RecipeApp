package cn.edu.nottingham.s20125628.recipecw;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
/**
 * Created By Kevin Ferdinand 20125628 for MDP CW2 2022
 * Each references for the relevant function is above the functions or at the beginning of the file
 * and a compiled version in README
 * Tested in VM : Pixel 2 API 29 Android ver 10
 * Tested in Physical Machine : Samsung A52S with API 31 Android ver 12
 * E:\Apps\MediaPlayerCW\app\build\intermediates\apk\debug
 */
public class MainActivity extends AppCompatActivity implements RecyclerViewRecipeClickListener, Serializable {
    DatabaseHelper mDatabaseHelper;
    ArrayList<RecipeClassData> recipeList =new ArrayList<>();
    RecyclerView RecipeRecyclerView;
    RecipeListAdapter recipeListAdapter;
    ArrayList<String> pathArrList = new ArrayList<String>();
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Reload Main Activity
                    Log.e("1", "onActivityResult: PERMISSION GRANTED");
                    finish();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("1", "onActivityResult: PERMISSION DENIED");

                }
            });
    ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int resultCode = activityResult.getResultCode();
                            Intent data = activityResult.getData();
                            if (resultCode == RESULT_OK) {
                                Log.d("2","Update List" + recipeList.size());
                                // Reload all Recycler View
                                recipeList.clear();
                                //Load the Database into the list again
                                LoadAllRecipeData();
                                //Notify Change
                                recipeListAdapter.notifyDataSetChanged();

                            }
                            else{
                                Log.d("2","kena fail" );
                                Toast.makeText(MainActivity.this, "Operation canceled", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadAllRecipeData();
        Log.d("2","Calling Adapter to recyclerview");
        RecipeRecyclerView=findViewById(R.id.RecipeRecyclerView);

        SetupRecyclerView();
        if(!checkPermission()){
            requestPermission();
            return;
        }
        getAllVideoPath(getApplicationContext());


    }
    private void getAllVideoPath(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Log.d("TAG", "print all URis " + uri.toString());
        String[] projection = { MediaStore.Video.VideoColumns.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        int vidsCount = 0;
        if (cursor != null) {
            vidsCount = cursor.getCount();
            Log.d("TAG", "Total count of videos: " + vidsCount);
            while (cursor.moveToNext()) {
                pathArrList.add(cursor.getString(0));
                Log.d("TAG", cursor.getString(0));
            }
            cursor.close();
        }
        Log.d("TAG", "Out of videoloop " );
        for (String num : pathArrList) {
            if(num.contains("Chiisana Koi no Uta.mp4")){
                Log.d("TAG", "Gottem" );
            }
            Log.d("TAG", num );
        }
        //return pathArrList.toArray(new String[pathArrList.size()]);
    }
    void requestPermission(){
        // If permission denied, ask for permission #Change
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this,"Read Permission is required to load songs,please enable them",Toast.LENGTH_SHORT).show();
            //ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }else//Ask permission
            //ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
    }
    public boolean checkPermission(){
        if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))== PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }
    public void SetupRecyclerView(){
        RecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeListAdapter = new RecipeListAdapter(recipeList,getApplicationContext(),this); ////
        RecipeRecyclerView.setAdapter(recipeListAdapter);
    }

    public void LoadAllRecipeData(){
        Log.d(String.valueOf(1),"Load Recipe Datas");
                mDatabaseHelper = new DatabaseHelper(this);
        Cursor DatabaseCursor = mDatabaseHelper.getData();

        while(DatabaseCursor.moveToNext()){
            RecipeClassData RecipeData =new RecipeClassData(
                    DatabaseCursor.getString(1),
                    DatabaseCursor.getString(2),
                    DatabaseCursor.getString(3),
                    DatabaseCursor.getString(4),
                    DatabaseCursor.getString(5),
                    DatabaseCursor.getString(6)
            );
            RecipeData.setID(DatabaseCursor.getString(0));
            recipeList.add(RecipeData);
            /*
            Log.d(String.valueOf(1), "table is:"+DatabaseCursor.getString(0)+" "
                    +DatabaseCursor.getString(1)+" "
                    +DatabaseCursor.getString(2)+" "
                    +DatabaseCursor.getString(3)+" "
                    +DatabaseCursor.getString(4)+" "
                    +DatabaseCursor.getString(5)+" "
                    +DatabaseCursor.getString(6)+" "
            );

             */

        }
        Log.d(String.valueOf(1),"Printing Recipe Datas");
        for (RecipeClassData num : recipeList) {
            Log.d(String.valueOf(1),num.toString());
        }
    }
    public void NewRecipe(View view) {
        Intent intent = new Intent(MainActivity.this, NewRecipe.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
        activityResultLauncher.launch(intent);
    }
    @Override
    public void recyclerViewListClicked(View v, int position) {
        Log.d("2","Accepted Delete Recipe  ");
        Log.d("2",recipeList.get(position).toString());
        mDatabaseHelper.deleteName(Integer.parseInt(recipeList.get(position).getID()),recipeList.get(position).getTitle());
        Log.d("2","Deleted Recipe  ");
        recipeList.clear();
        //Load the Database into the list again
        LoadAllRecipeData();
        //Notify Change
        recipeListAdapter.notifyDataSetChanged();


    }

    public void SortRating(View view) {
        Log.d("2","Before Sort   "+recipeList.toString());
        Collections.sort(recipeList);
        Log.d("2","After Sort   "+recipeList.toString());
        recipeListAdapter.notifyDataSetChanged();
    }
}