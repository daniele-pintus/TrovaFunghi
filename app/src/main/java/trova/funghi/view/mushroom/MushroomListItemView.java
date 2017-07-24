package trova.funghi.view.mushroom;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import trova.funghi.R;
import trova.funghi.persistence.entity.Mushroom;

public class MushroomListItemView extends LinearLayout {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    protected Context context;

    private Mushroom mushroom;

    // Sub Views
    private TextView sciNameTextView;
    private TextView edibilityTextView;
    private ImageView thumbNailImageView;

    public MushroomListItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MushroomListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MushroomListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        this.context = context;

        inflate(context, R.layout.mushroom_list_item_layout, this);

        this.sciNameTextView = (TextView) findViewById(R.id.sci_name);
        this.edibilityTextView = (TextView) findViewById(R.id.edibility);
        this.thumbNailImageView = (ImageView) findViewById(R.id.detail_thumbnail);
    }
    public void setMushroom(Mushroom mMushroom){

        this.mushroom = mMushroom;

        sciNameTextView.setText(mushroom.getSciName());
        edibilityTextView.setText(mushroom.getEdibility());
        Picasso.with(context).load(mMushroom.getThumbNailUri()).fit().centerCrop().into(thumbNailImageView);
    }
}
