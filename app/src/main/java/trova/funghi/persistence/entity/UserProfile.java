package trova.funghi.persistence.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xid73 on 30/06/2017.
 */

public class UserProfile implements IEntity{
    public static final String K_PROFILE = "profile";
    public static final String K_ACCOUNT = "account";
    public static final String K_USERPROFILE = "userProfile";
    Map<String,IEntity> entityMap;

    public UserProfile(Map map){
        this.entityMap = new HashMap<String,IEntity>(map);
    }

    private Account registeredAccount;
    private Profile profile;

    public Account getRegisteredAccount() {
        return (Account)this.entityMap.get(UserProfile.K_ACCOUNT);
    }

    public void setRegisteredAccount(IEntity registeredAccount) {
        this.entityMap.put(UserProfile.K_ACCOUNT,registeredAccount);
    }

    public Profile getProfile() {
        return (Profile)this.entityMap.get(UserProfile.K_PROFILE);
    }

    public void setProfile(IEntity profile) {
        this.entityMap.put(UserProfile.K_PROFILE,profile);
    }

    @Override
    @Exclude
    public String getEntityName() {
        return UserProfile.K_USERPROFILE;
    }
}
