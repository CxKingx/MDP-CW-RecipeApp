package cn.edu.nottingham.s20125628.recipecw;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class NewRecipe extends AppCompatActivity implements RecyclerViewClickListener,Serializable {
    private final int REQUEST_TAKE_GALLERY_VIDEO=3;
    public EditText RecipeName,RecipeDescription,RecipeRating,RecipeIngredient;
    public TextView VideoNameView;
    public String VideoName,VideoExtractedPath;
    public int VideoSize;
    public boolean VideoSizeBool=true;
    public ArrayList<String> IngredientList = new ArrayList<String>();
    RecyclerView recyclerView;
    IngredientListAdapter IngredientAdapter;
    DatabaseHelper mDatabaseHelper;
    //https://www.geeksforgeeks.org/uri-getpath-method-in-java-with-examples/
    ActivityResultLauncher<Intent> SelectVideoactivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int resultCode = activityResult.getResultCode();
                            Intent data = activityResult.getData();
                            Uri selectedVideoUri = data.getData();
                            if (resultCode == RESULT_OK) {
                                if (selectedVideoUri!=null){
                                    Log.d("1","Uri not empty "+selectedVideoUri.getPath()+" \n"+data.getData().toString());

                                }
                                else{
                                    Log.d("1","Uri  empty");
                                }
                                VideoName = GetVideoTitle(selectedVideoUri);
                                VideoExtractedPath = selectedVideoUri.toString();


                                Log.d("1","Video Title is "+ VideoName);
                                Log.d("1","Video Path is "+ VideoExtractedPath);
                                //PlayVideo();
                            }
                            else{
                                //Toast.makeText(MainActivity.this, "Operation canceled", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);
        FindViews();

        recyclerView=findViewById(R.id.ingredientRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        IngredientAdapter = new IngredientListAdapter(IngredientList,getApplicationContext(),this);
        recyclerView.setAdapter(IngredientAdapter);

        mDatabaseHelper = new DatabaseHelper(this);

    }

    public void SaveRecipe(View view) {

        Log.d("2","Title are  " +RecipeName.getText().toString());
        Log.d("2","Description are  " +RecipeDescription.getText().toString());
        Log.d("2","Rating are  " +RecipeRating.getText().toString());
        Log.d("2","Ingreidnets are  " +IngredientList.toString());
        Log.d("2","Video Title are  " +VideoName);
        Log.d("2","Video Path are  " +VideoExtractedPath);

        if (!CheckInput()){
            //Log.d("2","Empty Inputs");
            if (!VideoSizeBool){
                Toast.makeText(NewRecipe.this, "Video Size too big ", Toast.LENGTH_LONG).show();
                return;
            }else{
                Toast.makeText(NewRecipe.this, "Must put all inputs", Toast.LENGTH_LONG).show();
                return;
            }

        }


        // Check if video is empty
        //Log.d("2","Checking Video Inputs");
        if(VideoExtractedPath==null || VideoName==null){

            VideoExtractedPath="null";
            VideoName="null";
        }

        // Save Recipe
        //boolean insertData = mDatabaseHelper.addData(RecipeName.getText().toString());
        boolean insertData = mDatabaseHelper.addData(RecipeName.getText().toString() , RecipeDescription.getText().toString() , RecipeRating.getText().toString() , IngredientList.toString() , VideoName, VideoExtractedPath);

        if (insertData) {
            Log.d("2","data isnetted");
        } else {
            Log.d("2","not inserted");
        }

        //Return to Main Menu
        Intent resultIntent = new Intent();
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

    public boolean CheckInput(){
        Log.d("2","Checking Inputs" );
        Log.d("2","Recipe Name" );


        Log.d("2","file size is " + VideoSize/1024);

        if (RecipeName.getText().toString().isEmpty()){
            return false;
        }
        Log.d("2","Recipe Desc" );
        if (RecipeDescription.getText().toString().isEmpty()){
            return false;
        }
        Log.d("2","Recipe rat" );
        if (RecipeRating.getText().toString().isEmpty()){
            return false;
        }
        Log.d("2","IngerdeitnList" );
        if (IngredientList.isEmpty()){
            return false;
        }

        if (VideoSize/1024>10000){
            VideoSizeBool=false;
            return VideoSizeBool;
        }
        else{
            VideoSizeBool=true;
        }


        return true;
    }
    public void UploadVideo(View view) {
        Log.d("2","Selecting Video");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //Intent intent = new Intent();
        intent.setType("video/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        SelectVideoactivityResultLauncher.launch(intent);


    }
    //https://www.youtube.com/watch?v=LuGiI1MR_3E&ab_channel=SWIKbyMirTahaAli
    @SuppressLint("Range")
    public String GetVideoTitle(Uri uri){

        String VideoTitleExtracted=null;
        if(uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try{
                if (cursor !=null && cursor.moveToFirst()){
                    VideoTitleExtracted = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    VideoSize = Integer.parseInt(cursor.getString(cursor.getColumnIndex(OpenableColumns.SIZE)));
                }
            }finally{
                cursor.close();
            }
            if (VideoTitleExtracted==null){
                VideoTitleExtracted=uri.getPath();
                int cutt = VideoTitleExtracted.lastIndexOf('/');
                if(cutt!=1){
                    VideoTitleExtracted=VideoTitleExtracted.substring(cutt+1);
                }
            }

        }

        VideoNameView.setText(VideoTitleExtracted);
        return VideoTitleExtracted;
    }

    public void FindViews(){
        RecipeName=findViewById(R.id.RecipeNameInput);
        RecipeDescription=findViewById(R.id.RecipeDescriptionInput);
       // RecipeDescription.setScroller();
        RecipeRating=findViewById(R.id.RatingInput);
       // https://stackoverflow.com/questions/24199134/set-the-range-of-numbers-from-2-to-1000-in-an-edit-text-box-in-android

        RecipeRating.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        RecipeIngredient=findViewById(R.id.RecipeIngredientInput);
        VideoNameView=findViewById(R.id.VideoText);
    }

    public void AddIngredient(View view) {
        //gettext to string
        if (RecipeIngredient.getText().toString().isEmpty()){

        }else{
            IngredientList.add( RecipeIngredient.getText().toString());
            //Reset Ingredient Text
            RecipeIngredient.setText(null);
            Log.d("2",IngredientList.toString());
            //Update List
            IngredientAdapter.notifyItemInserted(IngredientList.size());
        }


    }
    @Override
    public void recyclerViewListClicked(View v, int position) {
        Log.d("2","Deleting Ingreidnet in New Recipe" + position);
        IngredientList.remove(position);
        IngredientAdapter.notifyItemRemoved(position);

    }

/*
    public void PlayVideo() {
        VideoView videoView =(VideoView)findViewById(R.id.videoView3);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        Uri newUri = Uri.parse(VideoExtractedPath);
        Log.d("2","VideoExtractedPath " + VideoExtractedPath+ "\nUri parth "+newUri.toString());
        //specify the location of media file
        //Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4");

        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(newUri);
        videoView.requestFocus();
        videoView.start();
    }

 */
/*
    private String getVideoPath(ContentResolver contentResolver, Uri uri) {
        //Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Log.d("Abs", "Transfger from URI TO absolute");

        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        ArrayList<String> pathArrList = new ArrayList<String>();
        int vidsCount = 0;
        if (cursor != null) {
            vidsCount = cursor.getCount();
            Log.d("TAG", "Total count of videos: " + vidsCount);
            //cursor.moveToFirst();
            while (cursor.moveToNext()) {
                pathArrList.add(cursor.getString(0));
                //Log.d("TAG", cursor.getString(0));
            }

            Log.d("TAG", pathArrList.toString());
            cursor.close();
        }
        else{
            Log.d("Abs", "cursor emprty");
        }
        return "test";
    }

 */


}