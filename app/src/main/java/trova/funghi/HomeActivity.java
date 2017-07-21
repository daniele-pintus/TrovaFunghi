package trova.funghi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import trova.funghi.authentication.AuthenticationEmailPasswordManager;
import trova.funghi.authentication.OnAuthenticationListener;
import trova.funghi.persistence.entity.ErrorEntity;
import trova.funghi.persistence.entity.IEntity;
import trova.funghi.persistence.entity.UserProfile;
import trova.funghi.persistence.entity.dao.OnDAOExecutionListener;
import trova.funghi.persistence.entity.dao.UserProfileDAO;
import trova.funghi.util.dialogs.AlertDialogHelper;

public class HomeActivity extends AppCompatActivity {

    protected final String LOG_TAG = this.getClass().getSimpleName();
//    VIEWs
    private  Toolbar appToolbar;
    private View authProgressView;
    private TextView presentationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG,"[onCreate]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        authProgressView = findViewById(R.id.authProgress);
        this.presentationView = (TextView)findViewById(R.id.presentation);
        // Find the toolbar view inside the activity layout
        appToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(appToolbar);
    }

    // Menu icons are inflated just as they were with actionbar
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        restoreActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(LOG_TAG,"onOptionsItemSelected");
        boolean rtrnValue = super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.mhome_view_profile:
                Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
                this.startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
                rtrnValue = true;
                break;
            case R.id.mhome_exit:
                this.onBackPressed();
                rtrnValue = true;
                break;
            case R.id.mhome_disconnect:
                this.showProgress(true);
                AuthenticationEmailPasswordManager authManagerEmailPwd = new AuthenticationEmailPasswordManager(CoreApplication.getInstance().getFirebaseAuth());
                authManagerEmailPwd.setOnAuthenticationListener(new OnAuthenticationSignoutListener());
                authManagerEmailPwd.signout();
            default:
                break;
        }
        return rtrnValue;
    }

    public void restoreActionBar() {
		Log.i(this.getClass().getName(),"restoreActionBar");
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(true);
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
//		actionBar.setHomeAsUpIndicator(this.getResources().getDrawable(R.drawable.ic_arrowl));
		actionBar.setTitle(R.string.app_name);

		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);

//		actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.tshape));
		actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bkg_toolbar));
	}

    public void showProgress(boolean show){
        this.authProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG,"onBackPressed");
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG,"onStart");
        super.onStart();
    }

    public void updateUI(UserProfile userProfile){
        this.presentationView.setText(
                this.getString(R.string.msg_presentetion,userProfile.getRegisteredAccount().getRoles().keySet().toArray()[0].toString(),userProfile.getProfile().getContactEmail()));
        this.showProgress(false);
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG,"onResume");
        super.onResume();
        this.showProgress(true);
        if(CoreApplication.getInstance().isCurrentUserPresent()){
            UserProfileDAO userProfileDAO = new UserProfileDAO();
            userProfileDAO.loadUserProfileEntity(new OnDAOExecutionListener() {
                @Override
                public void onSuccessDAOExecuted(IEntity entity) {
                    updateUI((UserProfile)entity);
                }

                @Override
                public void onErrorDAOExecuted(ErrorEntity errorEntity) {
                    Log.e(LOG_TAG,"errorEntity say:"+errorEntity.getDatabaseError());
                    HomeActivity.this.showProgress(false);
                    AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,HomeActivity.this,R.string.err_reload_user);
                    alertDialog.show();
                }
            });
        }else{
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,this,R.string.err_user_no_more_logged);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    HomeActivity.this.showProgress(false);
                    HomeActivity.this.onBackPressed();
                }
            });
            alertDialog.show();
        }
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG,"onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG,"onDestroy");
        super.onDestroy();
    }

    private class OnAuthenticationSignoutListener implements OnAuthenticationListener {

        @Override
        public void onAuthenticationSuccess() {
            Log.d(LOG_TAG,"onAuthenticationSuccess");
            HomeActivity.this.showProgress(false);
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,HomeActivity.this,R.string.msg_signout_ok);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    HomeActivity.this.finish();
                }
            });
            alertDialog.show();
        }

        @Override
        public void onAuthenticationFailure(Exception failureEx) {
            Log.d(LOG_TAG,"onAuthenticationFailure");
            Log.e(LOG_TAG,"Failure due to:["+failureEx.getMessage()+"]");
            HomeActivity.this.showProgress(false);
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,HomeActivity.this,R.string.msg_signout_ko);
            alertDialog.show();
        }

        @Override
        public void onValidationFailure() {
            Log.d(LOG_TAG,"onValidationFailure");
        }
    }
}
