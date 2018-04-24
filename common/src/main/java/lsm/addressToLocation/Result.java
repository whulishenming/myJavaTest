package lsm.addressToLocation;

/**
 * Created by shenmingli on 2018/4/23.
 */
public class Result {

    private Location location;

    private String precise;

    private String confidence;

    private String levell;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPrecise() {
        return precise;
    }

    public void setPrecise(String precise) {
        this.precise = precise;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getLevell() {
        return levell;
    }

    public void setLevell(String levell) {
        this.levell = levell;
    }
}
