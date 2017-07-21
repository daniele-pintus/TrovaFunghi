package trova.funghi.persistence.entity.dao;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import trova.funghi.CoreApplication;
import trova.funghi.persistence.entity.Account;
import trova.funghi.persistence.entity.ErrorEntity;
import trova.funghi.persistence.entity.IEntity;
import trova.funghi.persistence.entity.Profile;
import trova.funghi.persistence.entity.UserProfile;

/**
 * Created by xid73 on 30/06/2017.
 */

public class UserProfileDAO extends BaseDAO{
    protected final String LOG_TAG = this.getClass().getSimpleName();
    public UserProfileDAO(){
        super();
    }

    private void profile(final OnDAOExecutionListener listener){
        FirebaseUser fbUser = CoreApplication.getInstance().getFirebaseAuth().getCurrentUser();
        DatabaseReference mDatabase = this.getDbRef();
        mDatabase.child(Profile.CHILD_PROFILE).child(fbUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                Log.d(LOG_TAG,"Profile:"+profile.toString());
                listener.onSuccessDAOExecuted(profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG,"databaseError: "+databaseError);
                ErrorEntity errorEntity = new ErrorEntity();
                errorEntity.setDatabaseError(databaseError);
                listener.onErrorDAOExecuted(errorEntity);
            }
        });
    }

    private void account(final OnDAOExecutionListener listener){
        FirebaseUser fbUser = CoreApplication.getInstance().getFirebaseAuth().getCurrentUser();
        DatabaseReference mDatabase = this.getDbRef();
        mDatabase.child(Account.CHILD_ACCOUNT).child(fbUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);
                Log.d(LOG_TAG,"Account:"+account.toString());
                listener.onSuccessDAOExecuted(account);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG,"databaseError: "+databaseError);
                ErrorEntity errorEntity = new ErrorEntity();
                errorEntity.setDatabaseError(databaseError);
                listener.onErrorDAOExecuted(errorEntity);
            }
        });
    }

    public void backupProfile(final OnDAOExecutionListener onDAOExecutionListener){
        final FirebaseUser fbUser = CoreApplication.getInstance().getFirebaseAuth().getCurrentUser();
        final DatabaseReference mDatabase = this.getDbRef();
        loadUserProfileEntity(new OnDAOExecutionListener() {
            @Override
            public void onSuccessDAOExecuted(IEntity entity) {
                mDatabase.child("droppedAccounts").push().setValue((UserProfile) entity, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null){
                            mDatabase.child(Profile.CHILD_PROFILE).child(fbUser.getUid()).setValue(null, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError==null){
                                        onDAOExecutionListener.onSuccessDAOExecuted(new Profile());
                                    }else{
                                        ErrorEntity errorEntity = new ErrorEntity();
                                        errorEntity.setDatabaseError(databaseError);
                                        onDAOExecutionListener.onErrorDAOExecuted(errorEntity);
                                    }
                                }
                            });
                        }else{
                            ErrorEntity errorEntity = new ErrorEntity();
                            errorEntity.setDatabaseError(databaseError);
                            onDAOExecutionListener.onErrorDAOExecuted(errorEntity);
                        }
                    }
                });
            }

            @Override
            public void onErrorDAOExecuted(ErrorEntity errorEntity) {
                onDAOExecutionListener.onErrorDAOExecuted(errorEntity);
            }
        });
    }

    public void saveProfile(UserProfile userProfile){
        FirebaseUser fbUser = CoreApplication.getInstance().getFirebaseAuth().getCurrentUser();
        Log.d(LOG_TAG,"fbUser.getUid(): "+fbUser.getUid()+" name="+userProfile.getProfile().getName());
        DatabaseReference mDatabase = this.getDbRef();
        mDatabase.child(Profile.CHILD_PROFILE).child(fbUser.getUid()).setValue(userProfile.getProfile());
    }

    public void loadUserProfileEntity(final OnDAOExecutionListener listener){
        OnUserProfileDAOExecutionListener innerListener = new OnUserProfileDAOExecutionListener(listener);
        this.profile(innerListener);
        this.account(innerListener);
    }

    private class OnUserProfileDAOExecutionListener implements OnDAOExecutionListener{
        private OnDAOExecutionListener callBack;
        public OnUserProfileDAOExecutionListener(OnDAOExecutionListener _callback){
            this.callBack = _callback;
        }
        private Map<String,IEntity> mapCollector = new HashMap<>();
        @Override
        public void onSuccessDAOExecuted(IEntity entity) {
            mapCollector.put(entity.getEntityName(),entity);
            if(mapCollector.size()==2){
                this.callBack.onSuccessDAOExecuted(new UserProfile(mapCollector));
            }
        }

        @Override
        public void onErrorDAOExecuted(ErrorEntity errorEntity) {
            this.callBack.onErrorDAOExecuted(errorEntity);
        }
    }
}
