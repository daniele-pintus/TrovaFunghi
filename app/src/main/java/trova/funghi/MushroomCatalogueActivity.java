package trova.funghi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

import trova.funghi.flowcontroller.mushroom.MushroomFlowController;
import trova.funghi.frgmnt.IBaseController;
import trova.funghi.frgmnt.MushroomCatalogueFragment;
import trova.funghi.frgmnt.MushroomDetailFragment;
import trova.funghi.persistence.entity.Mushroom;

public class MushroomCatalogueActivity extends AppCompatActivity {
    protected final String LOG_TAG = this.getClass().getSimpleName();

    //    INTERNAL MEMBERs
    protected String currentTagFragment;

    // FLOW CONTROLLERs
    private MushroomFlowController flowController;

    // LISTENERS

    //    VIEWs
    private Toolbar appToolbar;

    //    Fragments
    private IBaseController<List<Mushroom>> fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "[onCreate]");
        super.onCreate(savedInstanceState);

        this.fragment = prepareFragment(MushroomCatalogueActivity.this.currentTagFragment, null);

        flowController = new MushroomFlowController(getApplicationContext());
        flowController.setListener(new MushroomCatalogueActivity.MushroomFlowControllerListenerImpl());


        setContentView(R.layout.catalogue_container_layout);

        appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);
    }

    protected IBaseController prepareFragment(String controllerTag, Bundle bundle){
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        IBaseController baseController = null;

        if(this.currentTagFragment!=null) {
            if (!this.currentTagFragment.equals(controllerTag)) {
                // TODO implements with replace fragment if needed
            }else{
                ft.show(this.getSupportFragmentManager().findFragmentByTag(this.currentTagFragment));
            }
        }else{
            baseController = new MushroomCatalogueFragment();
            currentTagFragment = baseController.getTagFragment();
            ft.add(R.id.catalogue_container, baseController, currentTagFragment).commit();
        }
        return baseController;
    }

    private class MushroomFlowControllerListenerImpl implements MushroomFlowController.MushroomFlowControllerListener {

        @Override
        public void onMushroomDataLoaded(Mushroom mushroom) { // do nothing
        }

        @Override
        public void onMushroomListDataLoaded(List<Mushroom> mushroomList) {
            fragment.loadData(mushroomList);
        }

    }
}
