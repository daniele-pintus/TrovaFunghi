package trova.funghi.frgmnt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import trova.funghi.R;
import trova.funghi.persistence.entity.Mushroom;
import trova.funghi.view.mushroom.MushroomCatalogueView;

public class MushroomCatalogueFragment extends IBaseController<List<Mushroom>> {
    protected final String LOG_TAG = this.getClass().getSimpleName();

    // VIEW
    private MushroomCatalogueView mushroomCatalogueView;

    public MushroomCatalogueFragment() {
        super();
        setTagFragment("MushroomCatalogueFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG,"[onCreateView]");
        final View rootView = inflater.inflate(R.layout.fragment_mushroom_catalogue, container, false);
        this.mushroomCatalogueView = (MushroomCatalogueView) rootView.findViewById(R.id.catalogue_container_custom);
        this.setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void loadData(List<Mushroom> mushroomList) {
        Log.i(LOG_TAG, "[loadData] " + mushroomList.size());
        mushroomCatalogueView.setMushroomList(mushroomList);
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
