package trova.funghi.persistence.entity.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by xid73 on 30/06/2017.
 */

public abstract class BaseDAO {
    private DatabaseReference mDatabase;
    public BaseDAO(){
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    protected DatabaseReference getDbRef(){
        return this.mDatabase;
    }
}
