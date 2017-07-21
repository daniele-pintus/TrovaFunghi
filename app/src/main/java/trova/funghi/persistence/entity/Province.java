package trova.funghi.persistence.entity;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by xid73 on 02/07/2017.
 */

public class Province implements IEntity {

    public static final String CHILD_PROVINCE = "province";
    private ArrayList<String> desc;
    @Override
    @Exclude
    public String getEntityName() {
        return Province.CHILD_PROVINCE;
    }

    public ArrayList<String> getDesc() {
        return desc;
    }

    public void setDesc(ArrayList<String> desc) {
        this.desc = desc;
    }
}
