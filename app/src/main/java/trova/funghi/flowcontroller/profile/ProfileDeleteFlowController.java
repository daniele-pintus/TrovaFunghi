package trova.funghi.flowcontroller.profile;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import trova.funghi.CoreApplication;
import trova.funghi.R;
import trova.funghi.authentication.AuthenticationEmailPasswordManager;
import trova.funghi.authentication.OnAuthenticationListener;
import trova.funghi.persistence.entity.ErrorEntity;
import trova.funghi.persistence.entity.IEntity;
import trova.funghi.persistence.entity.dao.OnDAOExecutionListener;
import trova.funghi.persistence.entity.dao.UserProfileDAO;
import trova.funghi.util.dialogs.AlertDialogHelper;
import trova.funghi.view.SimpleWheelDialog;

/**
 * Created by xid73 on 19/07/2017.
 */

public class ProfileDeleteFlowController {
    protected final String LOG_TAG = this.getClass().getSimpleName();

    private ProfileDeleteFlowController.IOnProfileDeleteListener listener;
    private OnAuthenticationListener onReauthenticateProfileFlowController;
    private OnAuthenticationListener onDeleteProfileFlowController;
    private Context ctxt;

    public ProfileDeleteFlowController(AppCompatActivity _ctxt, ProfileDeleteFlowController.IOnProfileDeleteListener _listener){
        this.ctxt = _ctxt;
        this.listener = _listener;
        this.onReauthenticateProfileFlowController = new OnReauthenticateProfileFlowController();
        this.onDeleteProfileFlowController = new OnDeleteProfileFlowController();
    }

    protected void deleteAccount(){
        Log.d(LOG_TAG,"###>deleteAccount");
        UserProfileDAO dao = new UserProfileDAO();
        OnDAOExecutionListener onDAOExecutionListener = new OnDAOExecutionListener() {
            @Override
            public void onSuccessDAOExecuted(IEntity entity) {
                AuthenticationEmailPasswordManager authManagerEmailPwd = new AuthenticationEmailPasswordManager(CoreApplication.getInstance().getFirebaseAuth());
                authManagerEmailPwd.setOnAuthenticationListener(onDeleteProfileFlowController);
                authManagerEmailPwd.deleteAccount();
            }

            @Override
            public void onErrorDAOExecuted(ErrorEntity errorEntity) {
                onDeleteProfileFlowController.onAuthenticationFailure(new Exception(errorEntity.getDatabaseError().getMessage()));
            }
        };
        dao.backupProfile(onDAOExecutionListener);

    }



    public void dropAccount(String password){
        activateSimpleWheelDialog(this.ctxt);
        AuthenticationEmailPasswordManager authManagerEmailPwd = new AuthenticationEmailPasswordManager(CoreApplication.getInstance().getFirebaseAuth());
        authManagerEmailPwd.setOnAuthenticationListener(onReauthenticateProfileFlowController);
        Log.d(LOG_TAG,"###>Password=["+password+"]");
        authManagerEmailPwd.reauthenticate(password);
    }



    public interface IOnProfileDeleteListener{
        public void onDidDropAccount();
        public void onDidErrorDropAccount();
    }

    private class OnReauthenticateProfileFlowController implements OnAuthenticationListener {

        @Override
        public void onAuthenticationSuccess() {
            Log.i(LOG_TAG,"######### OnReauthenticateProfileFlowController success ########");
            ProfileDeleteFlowController.this.deleteAccount();
        }

        @Override
        public void onAuthenticationFailure(Exception failureEx) {
            disableSimpleWheelDialog();
            Log.d(LOG_TAG,"OnReauthenticateProfileFlowController failure");
            Log.e(LOG_TAG,"Failure due to:["+failureEx.getMessage()+"]");
            listener.onDidErrorDropAccount();
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,ProfileDeleteFlowController.this.ctxt);
            if(failureEx instanceof FirebaseAuthInvalidUserException){
                alertDialog.setMessage(ProfileDeleteFlowController.this.ctxt.getString(R.string.fb_ex_signin_user));
            }else{
                alertDialog.setMessage(ProfileDeleteFlowController.this.ctxt.getString(R.string.err_generic));
            }
            alertDialog.show();
        }

        @Override
        public void onValidationFailure() {

        }
    }

    private class OnDeleteProfileFlowController implements OnAuthenticationListener {

        @Override
        public void onAuthenticationSuccess() {
            disableSimpleWheelDialog();
            Log.i(LOG_TAG,"######### OnDeleteProfileFlowController success ########");
            listener.onDidDropAccount();
        }

        @Override
        public void onAuthenticationFailure(Exception failureEx) {
            disableSimpleWheelDialog();
            Log.d(LOG_TAG,"OnDeleteProfileFlowController failure");
            Log.e(LOG_TAG,"Failure due to:["+failureEx.getMessage()+"]");
            listener.onDidErrorDropAccount();
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,ProfileDeleteFlowController.this.ctxt);
            if(failureEx instanceof FirebaseAuthInvalidUserException){
                alertDialog.setMessage(ProfileDeleteFlowController.this.ctxt.getString(R.string.fb_ex_signin_user));
            }else{
                alertDialog.setMessage(ProfileDeleteFlowController.this.ctxt.getString(R.string.err_generic));
            }
            alertDialog.show();
        }

        @Override
        public void onValidationFailure() {

        }
    }

    private SimpleWheelDialog waitDialog;
    /**
     * Open the loadingwheel
     * @param context
     */
    protected void activateSimpleWheelDialog(Context context){
        if(this.waitDialog!=null && this.waitDialog.isShowing()){
            disableSimpleWheelDialog();
        }
        this.waitDialog = new SimpleWheelDialog(context);
        this.waitDialog.setCancelable(false);
        this.waitDialog.show();
    }

    /**
     * Dismiss the opened loadingwheel
     */
    protected void disableSimpleWheelDialog(){
        this.waitDialog.dismiss();
    }
}
