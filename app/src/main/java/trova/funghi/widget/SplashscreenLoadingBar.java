package trova.funghi.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import trova.funghi.R;

/**
 * Created by xid73 on 17/10/2016.
 */
public class SplashscreenLoadingBar extends ProgressBar {
    /**
     * Create a new progress bar with range 0...100 and initial progress of 0.
     *
     * @param context the application environment
     */
    public SplashscreenLoadingBar(Context context) {
        super(context);
        initView();
    }

    public SplashscreenLoadingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SplashscreenLoadingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setIndeterminate(true);
        //noinspection ResourceType
        AnimationDrawable progressBarAnimationIndeterminate = (AnimationDrawable) getResources().getDrawable(R.drawable.progresbar_splashscreen);
        setIndeterminateDrawable(progressBarAnimationIndeterminate);
    }

}
