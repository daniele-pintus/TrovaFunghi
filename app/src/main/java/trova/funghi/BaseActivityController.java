package trova.funghi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import trova.funghi.authentication.AuthenticationEmailPasswordManager;
import trova.funghi.authentication.OnAuthenticationListener;
import trova.funghi.persistence.entity.ErrorEntity;
import trova.funghi.persistence.entity.IEntity;
import trova.funghi.persistence.entity.UserProfile;
import trova.funghi.persistence.entity.dao.OnDAOExecutionListener;
import trova.funghi.persistence.entity.dao.UserProfileDAO;

public class BaseActivityController extends AppCompatActivity {

    protected final String LOG_TAG = this.getClass().getSimpleName();
//Views
    private Button buttonSignOut;
    private TextView errorMsgs;
    private View mProgressView;
//    Data
    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_controller_layout);
        mProgressView = findViewById(R.id.login_progress);
        this.errorMsgs = (TextView)this.findViewById(R.id.errorMsgs);
        this.buttonSignOut = (Button)findViewById(R.id.signOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressView.setVisibility(mProgressView.getVisibility()!=View.VISIBLE ? View.VISIBLE : View.GONE);
                AuthenticationEmailPasswordManager authManagerEmailPwd = new AuthenticationEmailPasswordManager(CoreApplication.getInstance().getFirebaseAuth());
                authManagerEmailPwd.setOnAuthenticationListener(new OnAuthenticationSignoutListener());
                authManagerEmailPwd.signout();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.updateUI(R.string.hello_world);
    }

    public void updateData(UserProfile userProfile){
        FirebaseUser fbUser = CoreApplication.getInstance().getFirebaseAuth().getCurrentUser();
        String ruolo = "";

        String msg = this.getString(R.string.hello_world,fbUser.getEmail(),userProfile.getProfile().toString(),userProfile.getRegisteredAccount().getRoles().values()+"");
        Log.d(LOG_TAG,"updateData");
        this.errorMsgs.setText(msg);
        buttonSignOut.setText(R.string.action_sign_out);
        mProgressView.setVisibility(mProgressView.getVisibility()!=View.VISIBLE ? View.VISIBLE : View.GONE);
    }
    public void updateUI(int id){
        String value = this.errorMsgs.getContext().getString(R.string.hello_world,"","","");
        if(!CoreApplication.getInstance().isCurrentUserPresent()){
            this.errorMsgs.setText(value);
            buttonSignOut.setText(R.string.action_sign_in);
            mProgressView.setVisibility(View.GONE);
            return;
        }
        UserProfileDAO userProfileDAO = new UserProfileDAO();
        userProfileDAO.loadUserProfileEntity(new OnDAOExecutionListener() {
            @Override
            public void onSuccessDAOExecuted(IEntity entity) {
                updateData((UserProfile)entity);
            }

            @Override
            public void onErrorDAOExecuted(ErrorEntity errorEntity) {
                Log.e(LOG_TAG,"errorEntity say:"+errorEntity.getDatabaseError());
            }
        });
    }


    private class OnAuthenticationSignoutListener implements OnAuthenticationListener {

        @Override
        public void onAuthenticationSuccess() {
            Log.d(LOG_TAG,"onAuthenticationSuccess");
            updateUI(R.string.msg_signout_ok);
        }

        @Override
        public void onAuthenticationFailure(Exception failureEx) {
            Log.d(LOG_TAG,"onAuthenticationFailure");
            Log.e(LOG_TAG,"Failure due to:["+failureEx.getMessage()+"]");
        }

        @Override
        public void onValidationFailure() {
            Log.d(LOG_TAG,"onValidationFailure");
        }
    }
}
