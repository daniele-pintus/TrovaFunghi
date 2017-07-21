package trova.funghi.persistence.entity.dao;

import trova.funghi.persistence.entity.ErrorEntity;
import trova.funghi.persistence.entity.IEntity;

/**
 * Created by xid73 on 30/06/2017.
 */

public interface OnDAOExecutionListener {
    public abstract void onSuccessDAOExecuted(IEntity entity);
    public abstract void onErrorDAOExecuted(ErrorEntity errorEntity);
}
