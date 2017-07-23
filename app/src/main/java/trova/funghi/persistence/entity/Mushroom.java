package trova.funghi.persistence.entity;

/**
 * Created by danie on 22/07/2017.
 */

public class Mushroom implements IEntity {

    private String id;
    private String sciName;
    private String vulName;
    private String habitat;
    private String edibility;
    private String description;
    private String popularity;
    private String thumbNailUri;

    public Mushroom() {}

    @Override
    public String getEntityName() {
        return "Mushroom";
    }

    public Mushroom(String id, String sciName, String vulName, String habitat, String edibility, String description, String popularity) {
        this.id = id;
        this.sciName = sciName;
        this.vulName = vulName;
        this.habitat = habitat;
        this.edibility = edibility;
        this.description = description;
        this.popularity = popularity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSciName() {
        return sciName;
    }

    public void setSciName(String sciName) {
        this.sciName = sciName;
    }

    public String getVulName() {
        return vulName;
    }

    public void setVulName(String vulName) {
        this.vulName = vulName;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getEdibility() {
        return edibility;
    }

    public void setEdibility(String edibility) {
        this.edibility = edibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getThumbNailUri() {
        return thumbNailUri;
    }

    public void setThumbNailUri(String thumbNailUri) {
        this.thumbNailUri = thumbNailUri;
    }
}
