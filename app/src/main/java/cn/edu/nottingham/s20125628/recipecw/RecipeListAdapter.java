package cn.edu.nottingham.s20125628.recipecw;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> implements Serializable {
    public RecipeClassData RecipeObject ;
    ArrayList<RecipeClassData> Recipelist;
    Context context;
    private static RecyclerViewRecipeClickListener recipeitemListener;

    public RecipeListAdapter(ArrayList<RecipeClassData> Recipelist, Context context, RecyclerViewRecipeClickListener recipeitemListener ) {
        this.Recipelist = Recipelist;
        this.context = context;
        RecipeListAdapter.recipeitemListener = recipeitemListener;
        Log.d("2","Hei New object adapter");
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_recyler,parent,false);
        return new RecipeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListAdapter.ViewHolder holder, int position) {
        Log.d("2","Setting BindHolder");
        holder.RecipeNameView.setText(Recipelist.get(position).getTitle());
        holder.RatingView.setText(Recipelist.get(position).getRating());
        holder.DeleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("2","Deleting Ingreidnet " + holder.getAdapterPosition());
                recipeitemListener.recyclerViewListClicked(view, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() { return Recipelist.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,Serializable {

        TextView RecipeNameView;
        TextView RatingView;
        Button DeleteRecipe;


        public ViewHolder(View itemView) {
            super(itemView);
            RecipeNameView = itemView.findViewById(R.id.RecipeNameView);
            RatingView = itemView.findViewById(R.id.Rating_View);
            DeleteRecipe = itemView.findViewById(R.id.deleteRecipe);

            itemView.setOnClickListener(this);
        }

        //Whole
        @Override
        public void onClick(View view) {

            Log.d("2","View Recipe Detail "+ Recipelist.get(this.getAdapterPosition()).toString());

            RecipeObject = Recipelist.get(this.getAdapterPosition());
            // Send to new Activity
            Intent intent = new Intent(context, ViewRecipe.class);
            intent.putExtra("RecipeDataID", RecipeObject.getID());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);



        }
    }
}
