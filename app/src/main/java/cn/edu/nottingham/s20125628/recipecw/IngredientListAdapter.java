package cn.edu.nottingham.s20125628.recipecw;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ViewHolder>{
    public String IngredientName;
    ArrayList<String> IngredientList;
    Context context;
    private static RecyclerViewClickListener itemListener;

    public IngredientListAdapter(ArrayList<String> IngredientList, Context context, RecyclerViewClickListener itemListener ) {
        this.IngredientList = IngredientList;
        this.context = context;
        IngredientListAdapter.itemListener = itemListener;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //assign  recycler item to viewholder
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_recycler,parent,false);
        return new IngredientListAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder( IngredientListAdapter.ViewHolder holder, int position) {
        holder.IngredientNameView.setText(IngredientList.get(position));
        //holder.IngredientPositionView.setText(String.valueOf(holder.getAdapterPosition()+1));
        holder.IngredientPositionView.setText("-");
        holder.DeleteIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemListener.recyclerViewListClicked(view, holder.getAdapterPosition());
                Log.d("2","Deleting Ingreidnet " + holder.getAdapterPosition());
            }
        });
    }
    @Override
    public int getItemCount() {
        return IngredientList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView IngredientNameView;
        TextView IngredientPositionView;
        Button DeleteIngredient;


        public ViewHolder(View itemView) {
            super(itemView);
            IngredientNameView = itemView.findViewById(R.id.IngredientListView);
            IngredientPositionView= itemView.findViewById(R.id.IngredientPosition);
            DeleteIngredient = itemView.findViewById(R.id.deleteIngredient);

            //itemView.setOnClickListener(this);
        }

        //Whole Recycler view claik
        @Override
        public void onClick(View view) {
            // Pass Previous Song position
            //itemListener.recyclerViewListClicked(view, this.getAdapterPosition());
        }
    }
}
