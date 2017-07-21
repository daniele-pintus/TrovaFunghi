package trova.funghi;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

import trova.funghi.authentication.AuthenticationEmailPasswordManager;
import trova.funghi.authentication.OnAuthenticationListener;
import trova.funghi.constants.IConstants;
import trova.funghi.persistence.entity.Account;
import trova.funghi.persistence.entity.ErrorEntity;
import trova.funghi.persistence.entity.IEntity;
import trova.funghi.persistence.entity.Profile;
import trova.funghi.persistence.entity.UserProfile;
import trova.funghi.persistence.entity.dao.OnDAOExecutionListener;
import trova.funghi.persistence.entity.dao.ProvinciaDAO;
import trova.funghi.util.dialogs.AlertDialogHelper;
import trova.funghi.util.locale.CalendarHelper;
import trova.funghi.view.auth.ContainerFormsView;

/**
 * A login screen that offers login via emailView/passwordView.
 */
public class AuthenticationActivity extends FragmentActivity {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    // UI references.
    private ContainerFormsView containerFormsView;
//    LISTENERs
    private OnAuthenticationFlowOperations onAuthenticationFlowOperations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_root_forms_authentication);
        containerFormsView = (ContainerFormsView)this.findViewById(R.id.containerForms);
        this.onAuthenticationFlowOperations = new OnAuthenticationFlowOperations();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG,"onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG,"onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG,"onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"onResume");
        containerFormsView.setOnContainerAuthListener(this.onAuthenticationFlowOperations);
        this.loadData();
    }

    public void loadData(){
        ProvinciaDAO dao = new ProvinciaDAO();
        dao.loadProvince(new OnDAOExecutionListener() {
            @Override
            public void onSuccessDAOExecuted(IEntity entity) {
                containerFormsView.loadData(entity);
            }

            @Override
            public void onErrorDAOExecuted(ErrorEntity errorEntity) {
                Log.e(LOG_TAG,"errorEntity say:"+errorEntity.getDatabaseError());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"onDestroy");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(LOG_TAG,"onPostResume");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(LOG_TAG,"onPostCreate");
    }

    @Override
    public void onBackPressed() {
        // When the user hits the back button set the resultCode
        // as Activity.RESULT_CANCELED to indicate a failure
        return;
    }

    private class OnAuthenticationFlowOperations implements ContainerFormsView.OnContainerAuthListener {

        @Override
        public void onExecuteSignin(String email,String password) {
            AuthenticationEmailPasswordManager authManagerEmailPwd = new AuthenticationEmailPasswordManager(CoreApplication.getInstance().getFirebaseAuth());
            authManagerEmailPwd.setOnAuthenticationListener(new OnAuthenticationSigninListener());
            Log.d(LOG_TAG,"emailView="+email);
            Log.d(LOG_TAG,"passwordView="+password);
            authManagerEmailPwd.signin(email,password);
        }

        @Override
        public void onExecuteSignup(String email,String password) {
            AuthenticationEmailPasswordManager authManagerEmailPwd = new AuthenticationEmailPasswordManager(CoreApplication.getInstance().getFirebaseAuth());
            authManagerEmailPwd.setOnAuthenticationListener(new OnAuthenticationSignupListener());
            Log.d(LOG_TAG,"emailView="+email);
            Log.d(LOG_TAG,"passwordView="+password);
            authManagerEmailPwd.signup(email,password);
        }

        @Override
        public void onExecuteSendPasswordResetEmail(String email) {
            AuthenticationEmailPasswordManager authManagerEmailPwd = new AuthenticationEmailPasswordManager(CoreApplication.getInstance().getFirebaseAuth());
            authManagerEmailPwd.setOnAuthenticationListener(new OnAuthenticationSendPasswordResetEmailListener());
            Log.d(LOG_TAG,"emailView="+email);
            authManagerEmailPwd.sendPasswordResetEmail(email);
        }

        @Override
        public void onAbort() {
            setResult(AuthenticationActivity.RESULT_CANCELED);
            AuthenticationActivity.super.onBackPressed();
        }
    }


    private class OnAuthenticationSendPasswordResetEmailListener implements OnAuthenticationListener{
        protected final String LOG_TAG = this.getClass().getSimpleName();
        @Override
        public void onAuthenticationSuccess() {
            Log.d(LOG_TAG,"onAuthenticationSuccess");
            AuthenticationActivity.this.containerFormsView.showProgress(false);
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,AuthenticationActivity.this);
            AuthenticationActivity.this.containerFormsView.updateUI(R.string.msg_send_password_reset_email_ok,alertDialog);
        }

        @Override
        public void onAuthenticationFailure(Exception failureEx) {
            Log.d(LOG_TAG,"onAuthenticationFailure");
            Log.e(LOG_TAG,"Failure due to:["+failureEx.getMessage()+"]");
            AuthenticationActivity.this.containerFormsView.showProgress(false);
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,AuthenticationActivity.this);
            if(failureEx instanceof FirebaseAuthInvalidUserException){
                AuthenticationActivity.this.containerFormsView.updateUI(R.string.fb_ex_send_password_reset_email_user,alertDialog);
            }else{
                AuthenticationActivity.this.containerFormsView.updateUI(R.string.err_generic,alertDialog);
            }
        }

        @Override
        public void onValidationFailure() {
            Log.d(LOG_TAG,"onValidationFailure");
        }
    }

    private class OnAuthenticationSendEmailVerificationListener implements OnAuthenticationListener{
        protected final String LOG_TAG = this.getClass().getSimpleName();
        @Override
        public void onAuthenticationSuccess() {
            Log.d(LOG_TAG,"onAuthenticationSuccess");
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,AuthenticationActivity.this);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setResult(IConstants.STATE_AUTH_ACCOUNT_SIGNEDIN);
                    AuthenticationActivity.this.finish();
                }
            });
            AuthenticationActivity.this.containerFormsView.showProgress(false);
            AuthenticationActivity.this.containerFormsView.updateUI(R.string.msg_send_verify_email,alertDialog);
        }

        @Override
        public void onAuthenticationFailure(Exception failureEx) {
            Log.d(LOG_TAG,"onAuthenticationFailure");
            Log.e(LOG_TAG,"Failure due to:["+failureEx.getMessage()+"]");
            setResult(IConstants.STATE_AUTH_ACCOUNT_REGISTERED);
            AuthenticationActivity.this.finish();
        }

        @Override
        public void onValidationFailure() {
            Log.d(LOG_TAG,"onValidationFailure");
        }
    }

    private class OnAuthenticationSignupListener implements OnAuthenticationListener{
        protected final String LOG_TAG = this.getClass().getSimpleName();
        @Override
        public void onAuthenticationSuccess() {
            Log.d(LOG_TAG,"onAuthenticationSuccess");
            UserProfile userProfile = CoreApplication.getInstance().getDefaultUserProfile();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            Account account = userProfile.getRegisteredAccount();
            HashMap<String,Boolean> map = new HashMap<String,Boolean>();
            map.put("cercatore",Boolean.TRUE);
            account.setRoles(map);
            account.setPolicyAccepted(Boolean.TRUE);
            account.setPolicyAcceptedDate(0L);
            account.setContractEndDate(CalendarHelper.getCurrentDate());
            String uid = CoreApplication.getInstance().getFirebaseAuth().getCurrentUser().getUid();
            mDatabase.child(Account.CHILD_ACCOUNT).child(uid).setValue(userProfile.getRegisteredAccount());
            mDatabase.child(Profile.CHILD_PROFILE).child(uid).setValue(userProfile.getProfile());
            mDatabase.child(Account.CHILD_ACCOUNT).child(uid).child("policyAcceptedDate").setValue(ServerValue.TIMESTAMP);

            AuthenticationEmailPasswordManager authManagerEmailPwd = new AuthenticationEmailPasswordManager(CoreApplication.getInstance().getFirebaseAuth());
            authManagerEmailPwd.setOnAuthenticationListener(new OnAuthenticationSendEmailVerificationListener());
            authManagerEmailPwd.sendEmailVerification();
        }

        @Override
        public void onAuthenticationFailure(Exception failureEx) {
            Log.d(LOG_TAG,"onAuthenticationFailure");
            Log.d(LOG_TAG,"Failure due to:["+failureEx.getMessage()+"]");
            AuthenticationActivity.this.containerFormsView.showProgress(false);
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,AuthenticationActivity.this);
            if(failureEx instanceof FirebaseAuthWeakPasswordException){
                AuthenticationActivity.this.containerFormsView.updateUI(R.string.fb_ex_signup_password,alertDialog);
            }else if(failureEx instanceof FirebaseAuthInvalidCredentialsException){
                AuthenticationActivity.this.containerFormsView.updateUI(R.string.fb_ex_signup_user,alertDialog);
            }else if(failureEx instanceof FirebaseAuthUserCollisionException){
                AuthenticationActivity.this.containerFormsView.updateUI(R.string.fb_ex_signup_user_collision,alertDialog);
            }else{
                AuthenticationActivity.this.containerFormsView.updateUI(R.string.err_generic,alertDialog);
            }
        }

        @Override
        public void onValidationFailure() {
            Log.d(LOG_TAG,"onValidationFailure");
        }
    }

    private class OnAuthenticationSigninListener implements OnAuthenticationListener{
        protected final String LOG_TAG = this.getClass().getSimpleName();
        @Override
        public void onAuthenticationSuccess() {
            Log.d(LOG_TAG,"onAuthenticationSuccess");
            if(CoreApplication.getInstance().getFirebaseAuth().getCurrentUser().isEmailVerified()){
                AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,AuthenticationActivity.this);
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        setResult(IConstants.STATE_AUTH_ACCOUNT_SIGNEDIN);
                        AuthenticationActivity.this.finish();
                    }
                });
                AuthenticationActivity.this.containerFormsView.showProgress(false);
                AuthenticationActivity.this.containerFormsView.updateUI(R.string.msg_signin_ok,alertDialog);
            }else{
                AuthenticationEmailPasswordManager authManagerEmailPwd = new AuthenticationEmailPasswordManager(CoreApplication.getInstance().getFirebaseAuth());
                authManagerEmailPwd.setOnAuthenticationListener(new OnAuthenticationSendEmailVerificationListener());
                authManagerEmailPwd.sendEmailVerification();
            }
        }

        @Override
        public void onAuthenticationFailure(Exception failureEx) {
            Log.d(LOG_TAG,"onAuthenticationFailure");
            Log.e(LOG_TAG,"Failure due to:["+failureEx.getMessage()+"]");
            AuthenticationActivity.this.containerFormsView.showProgress(false);
            AlertDialog alertDialog = AlertDialogHelper.getAlertDialog(null,AuthenticationActivity.this);
            if(failureEx instanceof FirebaseAuthInvalidUserException){
                AuthenticationActivity.this.containerFormsView.updateUI(R.string.fb_ex_signin_user,alertDialog);
            }else if(failureEx instanceof FirebaseAuthInvalidCredentialsException){
                AuthenticationActivity.this.containerFormsView.updateUI(R.string.fb_ex_signin_password,alertDialog);
            }else{
                AuthenticationActivity.this.containerFormsView.updateUI(R.string.err_generic,alertDialog);
            }
        }

        @Override
        public void onValidationFailure() {
            Log.d(LOG_TAG,"onValidationFailure");
        }
    }
}

