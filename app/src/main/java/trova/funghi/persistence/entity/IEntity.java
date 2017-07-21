package trova.funghi.persistence.entity;

import com.google.firebase.database.Exclude;

/**
 * Created by xid73 on 30/06/2017.
 */

public interface IEntity {
    @Exclude
    public abstract String getEntityName();
}
