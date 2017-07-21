package trova.funghi.frgmnt;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import trova.funghi.R;
import trova.funghi.flowcontroller.profile.ProfileDeleteFlowController;
import trova.funghi.persistence.entity.Province;
import trova.funghi.persistence.entity.UserProfile;
import trova.funghi.util.dialogs.AlertDialogHelper;
import trova.funghi.view.profile.ProfileContainerCustomView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileFrgmnt extends IBaseController<UserProfile> implements ProfileDeleteFlowController.IOnProfileDeleteListener{

    protected final String LOG_TAG = this.getClass().getSimpleName();

//    FLOW CONTROLLERs
    private ProfileDeleteFlowController flowController;
//    VIEWs
    private ProfileContainerCustomView profileContainerCustomView;
//    DATAs
    private boolean isEditMode;

//    LISTENERs
    private OnProfileContainerDataModifiedViewListener listener;
    private IOnProfileContainerDataModifiedControllerListener externalListener;

    public ProfileFrgmnt() {
        super();
        setTagFragment("ProfileFrgmnt");
    }


    @Override
    public void loadData(UserProfile _userProfile) {
        Log.i(LOG_TAG,"loadData="+_userProfile.getProfile().getContactEmail());
        this.profileContainerCustomView.load(_userProfile);
        profileContainerCustomView.showProgress(false);
    }

    public void loadProvince(Province _province){
        Log.i(LOG_TAG,"loadProvince");
        this.profileContainerCustomView.loadProvinceOrComuni(_province);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG,"[onCreateView]");
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        this.profileContainerCustomView = (ProfileContainerCustomView) rootView.findViewById(R.id.profile_container_custom);
        this.listener = new OnProfileContainerDataModifiedViewListener();
        this.profileContainerCustomView.setListenerOnModified(listener);
        this.setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG,"[onViewCreated]");
        profileContainerCustomView.showProgress(true);
        super.onViewCreated(view, savedInstanceState);
        this.flowController = new ProfileDeleteFlowController((AppCompatActivity)this.getActivity(),this);
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG,"[onResume]");
        super.onResume();
//        this.restoreActionBar();
    }

//    public void restoreActionBar() {
//        Log.i(this.getClass().getName(),"restoreActionBar");
//        ActionBar supportActionBar = ((AppCompatActivity)this.getActivity()).getSupportActionBar();
//        supportActionBar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.x_b1)));
//        supportActionBar.setDisplayShowTitleEnabled(true);
//        supportActionBar.setHomeAsUpIndicator(this.getResources().getDrawable(R.drawable.ic_arrowl));
//        supportActionBar.setDisplayHomeAsUpEnabled(true);
//        supportActionBar.setTitle("Profilo utente");
//        supportActionBar.show();
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i(LOG_TAG,"onCreateOptionsMenu");
        if(isEditMode)	inflater.inflate(R.menu.frgmnt_menu_profile_confirm, menu);
        else			inflater.inflate(R.menu.frgmnt_menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.i(LOG_TAG,"onPrepareOptionsMenu");
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.act_edit) {
            setEditMode(true);
        }else if(item.getItemId() == R.id.act_confirm) {
            setEditMode(false);
        }else if(item.getItemId() == R.id.act_drop_account){
            DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText etPwd = (EditText) ((AlertDialog)dialog).getDelegate().findViewById(R.id.password);
                    flowController.dropAccount(etPwd.getText().toString());
                    etPwd.setText(null);
                    dialog.dismiss();
                }
            };
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialogCustomView(
                    dialogOnClickListener,this.getContext(),R.string.lbl_msg_drop_account
                    ,R.layout.form_reauthenticate_x_drop_account,R.string.lbl_btn_drop_account
            );
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setEditMode(boolean isEditMode){
        this.isEditMode = isEditMode;
        // invalidate the options menu
        getActivity().invalidateOptionsMenu();
        if(this.profileContainerCustomView!=null){
            this.profileContainerCustomView.setEditMode(this.isEditMode);
        }
    }

    public IOnProfileContainerDataModifiedControllerListener getExternalListener() {
        return externalListener;
    }

    public void setExternalListener(IOnProfileContainerDataModifiedControllerListener externalListener) {
        this.externalListener = externalListener;
    }

    @Override
    public void onDidDropAccount() {
        Log.i(LOG_TAG,"[onDidDropAccount]");
        this.getActivity().onBackPressed();
    }

    @Override
    public void onDidErrorDropAccount() {
        Log.i(LOG_TAG,"[onDidErrorDropAccount]");
    }

    public interface IOnProfileContainerDataModifiedControllerListener{
        public void onSave(UserProfile _userProfile);
    }


    private class OnProfileContainerDataModifiedViewListener implements ProfileContainerCustomView.IOnProfileContainerDataModifiedViewListener {

        @Override
        public void onSave(UserProfile _userProfile) {
            externalListener.onSave(_userProfile);
        }

        @Override
        public void onValidationError(UserProfile _userProfile) {
            setEditMode(!isEditMode);
        }
    }
}
