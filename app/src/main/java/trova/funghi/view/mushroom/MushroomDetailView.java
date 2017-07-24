package trova.funghi.view.mushroom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import trova.funghi.R;
import trova.funghi.persistence.entity.Mushroom;

/**
 * Created by dp on 22/07/2017.
 */

public class MushroomDetailView extends LinearLayout {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    protected Context context;

    private Mushroom mushroom;

    // Sub Views
    private TextView sciNameTextView;
    private TextView vulNameTextView;
    private TextView habitatTextView;
    private TextView edibilityTextView;
    private TextView descriptionTextView;
    private TextView popularityTextView;
    private ImageView thumbNailImageView;

    boolean isImageFitToScreen;

    public MushroomDetailView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MushroomDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MushroomDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        this.context = context;

        inflate(context, R.layout.mushroom_detail_layout, this);

        this.sciNameTextView = (TextView) findViewById(R.id.sci_name);
        this.vulNameTextView = (TextView) findViewById(R.id.vul_name);
        this.habitatTextView = (TextView) findViewById(R.id.habitat);
        this.edibilityTextView = (TextView) findViewById(R.id.edibility);
        this.descriptionTextView = (TextView) findViewById(R.id.description);
        this.popularityTextView = (TextView) findViewById(R.id.popularity);
        this.thumbNailImageView = (ImageView) findViewById(R.id.detail_thumbnail);
        thumbNailImageView.setOnClickListener(new ImageOnClickListener());
    }

    public void setMushroom(Mushroom mMushroom){

        this.mushroom = mMushroom;

        sciNameTextView.setText(mushroom.getSciName());
        vulNameTextView.setText(mushroom.getVulName());
        habitatTextView.setText(mushroom.getHabitat());
        edibilityTextView.setText(mushroom.getEdibility());
        descriptionTextView.setText(mushroom.getDescription());
        popularityTextView.setText(mushroom.getPopularity());
        Picasso.with(context).load(mMushroom.getThumbNailUri()).fit().centerCrop().into(thumbNailImageView);
    }

    private class ImageOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(isImageFitToScreen) {
                isImageFitToScreen=false;
                thumbNailImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                thumbNailImageView.setAdjustViewBounds(true);
            }else{
                isImageFitToScreen=true;
                thumbNailImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                thumbNailImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

}
