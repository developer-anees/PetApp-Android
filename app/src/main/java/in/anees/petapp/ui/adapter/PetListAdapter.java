package in.anees.petapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.anees.petapp.R;
import in.anees.petapp.model.Pet;
import in.anees.petapp.network.ImageDownloader;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class PetListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Pet> mPetList;
    private static final ImageDownloader sImageDownloader = new ImageDownloader();

    public PetListAdapter(Context context, ArrayList<Pet> petList) {
        mContext = context;
        mPetList = petList;
    }

    @Override
    public int getCount() {
        return mPetList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_list_item, parent, false);

            holder = new ViewHolder();
            holder.petTitle = (TextView) convertView.findViewById(R.id.textViewPetName);
            holder.petLogo = (ImageView) convertView.findViewById(R.id.imageViewPetPhoto);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Pet pet = mPetList.get(position);
        holder.petTitle.setText(pet.getTitle());

        if (holder.petLogo != null) {
            sImageDownloader.download(pet.getImageUrl(), holder.petLogo);
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView petLogo;
        private TextView petTitle;
    }
}
