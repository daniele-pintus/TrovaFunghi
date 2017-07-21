package trova.funghi.frgmnt;

import android.support.v4.app.Fragment;

/**
 * Created by xid73 on 15/07/2017.
 */

public abstract class IBaseController<T> extends Fragment{
    private String tagFragment;

    protected IBaseController(){
    }

    public String getTagFragment() {
        return tagFragment;
    }

    public void setTagFragment(String tagFragment) {
        this.tagFragment = tagFragment;
    }

    public abstract void loadData(T dataToLoad);

}
