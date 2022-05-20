package cn.edu.nottingham.s20125628.recipecw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewRecipe extends AppCompatActivity implements Serializable {
    DatabaseHelper mDatabaseHelper;
    RecipeClassData ShowRecipeData;
    String RecipeID;
    public TextView RecipeName,RecipeDescription,RecipeIngredient,VideoNameView;
    public EditText RecipeRating;
    ArrayList<RecipeClassData> recipeList =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        Intent Main=getIntent();
        RecipeID = Main.getStringExtra("RecipeDataID");
        Log.d("2","Got Recipe ID "+ RecipeID);
        LoadAllRecipeData();
        getThisRecipe();
        FindViews();
        SetViews();
        }

    //Set View to the recipe text
    private void SetViews(){
        RecipeName.setText(ShowRecipeData.getTitle());
        RecipeDescription.setText(ShowRecipeData.getDescription());
        RecipeIngredient.setText(ShowRecipeData.getIngredients());
        RecipeRating.setText(ShowRecipeData.getRating());
        VideoNameView.setText(ShowRecipeData.getVideoTitle());
    }
    //Update Rating in the database
    public void UpdateRating(View view){

        String NewRating = RecipeRating.getText().toString();
        Log.d("2","Upddate Rating to  "+ NewRating);
        mDatabaseHelper.updateRating(NewRating,Integer.parseInt(ShowRecipeData.getID()));

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //Prepare the Views
    private void FindViews() {
        RecipeName =findViewById(R.id.RecipeNameInput);
        RecipeDescription =findViewById(R.id.RecipeDescriptionInput);
        RecipeIngredient =findViewById(R.id.IngredientInput);
        RecipeRating =findViewById(R.id.RatingInput);

        RecipeRating.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});

        VideoNameView =findViewById(R.id.VideoText);
    }

    //Compare the ID that we want to get from the list of all recipe
    public void getThisRecipe(){
            int i=0;
            Log.d("2","Get Specific  Recipe Data ");
            while(i<recipeList.size()){
                if (recipeList.get(i).getID().equals(RecipeID)){
                    ShowRecipeData = recipeList.get(i);
                    Log.d("2","Got Recipe Data "+  ShowRecipeData.toString());
                }
                i++;
            }
        }

        // Get All recipe Data so we can get the object by searching the ID later
        public void LoadAllRecipeData(){
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
//
        }

    public void ViewVideo(View view) {
        ShowRecipeData.getVideoPath();
        if(ShowRecipeData.getVideoPath().equals("null")){
            Log.d(String.valueOf(1),"No Video ");
            Toast.makeText(ViewRecipe.this, "No Video", Toast.LENGTH_LONG).show();

        }
        else{
            Log.d(String.valueOf(1),"Have Video ");
            Intent intent = new Intent(getApplicationContext(), ViewVideo.class);
            intent.putExtra("VideoPath", Uri.parse(ShowRecipeData.getVideoPath()));
            intent.putExtra("VideoTitle",ShowRecipeData.getVideoTitle());
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addCategory(Intent.ACTION_OPEN_DOCUMENT_TREE);

            startActivity(intent);
        }
    }
}

