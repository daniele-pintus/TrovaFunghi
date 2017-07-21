package trova.funghi.persistence.entity;

import com.google.firebase.database.Exclude;

import java.util.Map;

/**
 * Created by xid73 on 27/06/2017.
 */

public class Profile implements IEntity{

    public static final String CHILD_PROFILE = "profile";
    public Profile(){

    }

    public Profile(String _contactEmail){
        this.comune = "";
        this.provincia = "";
        this.contactEmail = _contactEmail;
        this.name="";
        this.birthDate="";
    }
    private String comune;
    private String provincia;
    private String contactEmail;
    private String name;
    private String birthDate;
    private Map<String,Boolean> prefFunghi;
    private String others;


    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return this.getName()+"|"+this.getProvincia()+"|"+this.getComune()+"|"+this.getBirthDate()+"|"+this.getContactEmail()+this.getPrefFunghi().values().toString();
    }

    public Map<String, Boolean> getPrefFunghi() {
        return prefFunghi;
    }

    public void setPrefFunghi(Map<String, Boolean> prefFunghi) {
        this.prefFunghi = prefFunghi;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    @Override
    @Exclude
    public String getEntityName() {
        return UserProfile.K_PROFILE;
    }
}
