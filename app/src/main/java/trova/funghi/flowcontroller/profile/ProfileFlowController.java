package trova.funghi.flowcontroller.profile;

import android.content.Context;
import android.util.Log;

import trova.funghi.authentication.OnAuthenticationListener;
import trova.funghi.persistence.entity.ErrorEntity;
import trova.funghi.persistence.entity.IEntity;
import trova.funghi.persistence.entity.Province;
import trova.funghi.persistence.entity.UserProfile;
import trova.funghi.persistence.entity.dao.OnDAOExecutionListener;
import trova.funghi.persistence.entity.dao.ProvinciaDAO;
import trova.funghi.persistence.entity.dao.UserProfileDAO;

public class ProfileFlowController {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    private IOnProfileListener listener;
    private OnAuthenticationListener onReauthenticateProfileFlowController;
    private OnAuthenticationListener onDeleteProfileFlowController;
    private Context ctxt;

    public ProfileFlowController(Context _ctxt,IOnProfileListener _listener){
        this.ctxt = _ctxt;
        this.listener = _listener;
    }

    public void save(UserProfile _userProfile){
        Log.d(LOG_TAG,"[save]");
        UserProfileDAO userProfileDAO = new UserProfileDAO();
        userProfileDAO.saveProfile(_userProfile);
        listener.onSavedSuccessful();
    }

    public void load(){
        UserProfileDAO userProfileDAO = new UserProfileDAO();
        userProfileDAO.loadUserProfileEntity(new OnDAOExecutionListener() {
            @Override
            public void onSuccessDAOExecuted(IEntity entity) {
                Log.i(LOG_TAG,"onSuccessDAOExecuted");
                listener.onDidLoad((UserProfile)entity);
            }

            @Override
            public void onErrorDAOExecuted(ErrorEntity errorEntity) {
                Log.e(LOG_TAG,"errorEntity say:"+errorEntity.getDatabaseError());
                listener.onDidErrorLoad(errorEntity);
            }
        });

        ProvinciaDAO dao = new ProvinciaDAO();
        dao.loadProvince(new OnDAOExecutionListener() {
            @Override
            public void onSuccessDAOExecuted(IEntity entity) {
                listener.onDidLoadProvince((Province)entity);
            }

            @Override
            public void onErrorDAOExecuted(ErrorEntity errorEntity) {
                Log.e(LOG_TAG,"errorEntity say:"+errorEntity.getDatabaseError());
                listener.onDidErrorLoad(errorEntity);
            }
        });
    }


    public interface IOnProfileListener{
        public void onDidLoad(UserProfile _userProfile);
        public void onDidLoadProvince(Province _province);
        public void onDidErrorLoad(ErrorEntity _errorEntity);
        public void onSavedSuccessful();
        public void onSavedError();
    }

}
