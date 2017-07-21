package trova.funghi.persistence.entity;

import java.util.List;

/**
 * Created by xid73 on 08/07/2017.
 */

public class Comuni implements IEntity{
    public static final String CHILD_COMUNI = "comuni/province";
    private List<String> comuni;
    @Override
    public String getEntityName() {
        return CHILD_COMUNI;
    }


    public List<String> getComuni() {
        return comuni;
    }

    public void setComuni(List<String> comuni) {
        this.comuni = comuni;
    }
}
