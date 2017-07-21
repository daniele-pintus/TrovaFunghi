package trova.funghi.persistence.entity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Exclude;

/**
 * Created by xid73 on 30/06/2017.
 */

public class ErrorEntity implements IEntity{
    public static final String K_ERROR = "error";
    private DatabaseError databaseError;


    public DatabaseError getDatabaseError() {
        return databaseError;
    }

    public void setDatabaseError(DatabaseError databaseError) {
        this.databaseError = databaseError;
    }

    @Override
    @Exclude
    public String getEntityName() {
        return null;
    }
}
