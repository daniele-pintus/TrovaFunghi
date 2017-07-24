package trova.funghi.frgmnt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trova.funghi.R;
import trova.funghi.flowcontroller.profile.ProfileDeleteFlowController;
import trova.funghi.persistence.entity.Mushroom;
import trova.funghi.view.mushroom.MushroomDetailView;
import trova.funghi.view.profile.ProfileContainerCustomView;

/**
 * Created by danie on 22/07/2017.
 */

public class MushroomDetailFragment extends IBaseController<Mushroom> {
    protected final String LOG_TAG = this.getClass().getSimpleName();

    // VIEW
    private MushroomDetailView mushroomDetailView;

    public MushroomDetailFragment() {
        super();
        setTagFragment("MushroomDetailFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG,"[onCreateView]");
        final View rootView = inflater.inflate(R.layout.fragment_mushroom_detail, container, false);
        this.mushroomDetailView = (MushroomDetailView) rootView.findViewById(R.id.detail_container_custom);
        this.setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void loadData(Mushroom mushroom) {
        Log.i(LOG_TAG, "[loadData] " + mushroom.getId());
        mushroomDetailView.setMushroom(mushroom);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "[onViewCreated]");
//        mushroomDetailView.showProgress(true);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "[onResume]");
        super.onResume();
    }
}