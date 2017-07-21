package trova.funghi.persistence.entity.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import trova.funghi.persistence.entity.Comuni;
import trova.funghi.persistence.entity.ErrorEntity;
import trova.funghi.persistence.entity.Province;

/**
 * Created by xid73 on 02/07/2017.
 */

public class ProvinciaDAO extends BaseDAO {

    protected final String LOG_TAG = this.getClass().getSimpleName();

    public ProvinciaDAO(){
        super();
    }

    public void loadProvince(final OnDAOExecutionListener listener){
        DatabaseReference mDatabase = this.getDbRef();
        mDatabase.child(Province.CHILD_PROVINCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Province entity = dataSnapshot.getValue(Province.class);
//                Log.d(LOG_TAG,"Profile:"+entity.getDesc().toString());
                listener.onSuccessDAOExecuted(entity);
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

    public void loadComuniByProvincia(String provincia,final OnDAOExecutionListener listener){
        DatabaseReference mDatabase = this.getDbRef();
        mDatabase.child(Comuni.CHILD_COMUNI).child(provincia).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> rtrnvalue = (List<String>)dataSnapshot.getValue();
                Comuni entity = new Comuni();
                entity.setComuni(rtrnvalue);
//                Log.d(LOG_TAG,"Comuni:"+entity.getComuni().toString());
                listener.onSuccessDAOExecuted(entity);
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
}
