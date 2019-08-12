package in.anees.petapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.anees.petapp.R;
import in.anees.petapp.data.model.Pet;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class PetListRecyclerViewAdapter extends RecyclerView.Adapter<PetListRecyclerViewAdapter.ViewHolder> {

    public interface OnPetListItemClickListener {
        void onPetClick(int position);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView petLogo;
        private TextView petTitle;

        private OnPetListItemClickListener listener;

        public ViewHolder(@NonNull View itemView, OnPetListItemClickListener listener) {
            super(itemView);
            petLogo = itemView.findViewById(R.id.imageViewPetPhoto);
            petTitle = itemView.findViewById(R.id.textViewPetName);
            this.listener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onPetClick(getAdapterPosition());
        }
    }

    private List<Pet> mPetList;
    private OnPetListItemClickListener mListener;

    public PetListRecyclerViewAdapter(List<Pet> petList, OnPetListItemClickListener listener) {
        this.mPetList = petList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView).load(mPetList.get(position).getImageUrl()).into(holder.petLogo);
        holder.petTitle.setText(mPetList.get(position).getTitle());
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(holder.itemView).clear(holder.itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setPetListToRecyclerView(List<Pet> pets){
        mPetList = pets;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPetList.size();
    }
}
