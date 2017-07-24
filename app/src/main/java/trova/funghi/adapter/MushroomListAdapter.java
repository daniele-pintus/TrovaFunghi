package trova.funghi.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import trova.funghi.R;
import trova.funghi.persistence.entity.Mushroom;

/**
 * Created by danie on 24/07/2017.
 */

public class MushroomListAdapter  extends ArrayAdapter<Mushroom> {

    // CONTEXT
    private Context context;

    // MODEL
    private List<Mushroom> mushroomList;

    public MushroomListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Mushroom> mushroomList) {
        super(context, resource, mushroomList);
        this.context = context;
        this.mushroomList = mushroomList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.mushroom_list_item_layout, parent, false);

        // Sub Views
        TextView sciNameTextView = (TextView) rowView.findViewById(R.id.sci_name);
        TextView edibilityTextView = (TextView) rowView.findViewById(R.id.edibility);
        ImageView thumbNailImageView = (ImageView) rowView.findViewById(R.id.detail_thumbnail);

        Mushroom mushroom = mushroomList.get(position);

        sciNameTextView.setText(mushroom.getSciName());
        edibilityTextView.setText(mushroom.getEdibility());
        Picasso.with(context).load(mushroom.getThumbNailUri()).fit().centerCrop().into(thumbNailImageView);

        return rowView;
    }
}
