package trova.funghi;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import trova.funghi.flowcontroller.profile.ProfileFlowController;
import trova.funghi.frgmnt.IBaseController;
import trova.funghi.frgmnt.ProfileFrgmnt;
import trova.funghi.persistence.entity.ErrorEntity;
import trova.funghi.persistence.entity.Province;
import trova.funghi.persistence.entity.UserProfile;
import trova.funghi.util.dialogs.AlertDialogHelper;

public class ProfileActivity extends AppCompatActivity {

    protected final String LOG_TAG = this.getClass().getSimpleName();
//    FLOW CONTROLLERs
    private ProfileFlowController pfc;
//    INTERNAL MEMBERs
    protected String currentTagFragment;
    //    VIEWs
    private Toolbar appToolbar;
    private FrameLayout profileContainerView;

//    Fragments
    private IBaseController<UserProfile> controller;

//    Listeners
    private OnProfileContainerDataModifiedControllerListener onProfileContainerDataModifiedControllerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG,"[onCreate]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        this.profileContainerView = (FrameLayout)this.findViewById(R.id.profile_body);
        appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(appToolbar);
        this.pfc = new ProfileFlowController(ProfileActivity.this,new OnProfileListener());
        this.controller = ProfileActivity.this.prepareController(ProfileActivity.this.currentTagFragment,null);
    }


    protected IBaseController prepareController(String controllerTag,Bundle bundle){
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        IBaseController baseController = null;
        if(this.currentTagFragment!=null) {
            if (!this.currentTagFragment.equals(controllerTag)) {
//                TODO implements with replace fragment if needed
            }else{
                ft.show(this.getSupportFragmentManager().findFragmentByTag(this.currentTagFragment));
            }
        }else{
            baseController = new ProfileFrgmnt();
            onProfileContainerDataModifiedControllerListener = new OnProfileContainerDataModifiedControllerListener();
            ((ProfileFrgmnt)baseController).setExternalListener(onProfileContainerDataModifiedControllerListener);
            currentTagFragment = baseController.getTagFragment();
            ft.add(R.id.profile_body, baseController,currentTagFragment).commit();
        }
        return baseController;
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG,"[onResume]");
        super.onResume();
        this.pfc.load();
    }

    public void restoreActionBar() {
        Log.i(this.getClass().getName(),"restoreActionBar");
        ActionBar supportActionBar = this.getSupportActionBar();
        supportActionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bkg_toolbar));
        supportActionBar.setDisplayShowTitleEnabled(true);
        supportActionBar.setHomeAsUpIndicator(this.getResources().getDrawable(R.drawable.ic_arrowl));
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle("Profilo utente");
        supportActionBar.show();
    }

    // Menu icons are inflated just as they were with actionbar
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        restoreActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(LOG_TAG,"onOptionsItemSelected");
        boolean rtrnValue = super.onOptionsItemSelected(item);
//        Configuration configuration = this.getResources().getConfiguration();
//    	if(configuration.orientation==Configuration.ORIENTATION_LANDSCAPE){
//    		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//    	}
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(LOG_TAG,"home pressed");
                finish();
                rtrnValue = true;
                break;
            default:
                break;
        }
        return rtrnValue;
    }

    private class OnProfileListener implements ProfileFlowController.IOnProfileListener {

        @Override
        public void onDidLoad(UserProfile _userProfile) {
            controller.loadData(_userProfile);
        }

        @Override
        public void onDidLoadProvince(Province _province) {
            ((ProfileFrgmnt)controller).loadProvince(_province);
        }

        @Override
        public void onDidErrorLoad(ErrorEntity _errorEntity) {

        }

        @Override
        public void onSavedSuccessful() {
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,ProfileActivity.this);
            alertDialog.setMessage(ProfileActivity.this.getResources().getString(R.string.msg_updated_ok));
            alertDialog.show();
        }

        @Override
        public void onSavedError() {
        }
    }

    private class OnProfileContainerDataModifiedControllerListener implements ProfileFrgmnt.IOnProfileContainerDataModifiedControllerListener {

        @Override
        public void onSave(UserProfile _userProfile) {
            pfc.save(_userProfile);
        }

    }
}
