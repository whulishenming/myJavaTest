package lsm.addressToLocation;

/**
 * Created by shenmingli on 2018/4/23.
 */
public class Location {
    private String lng;

    private String lat;

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return this.getLat() + "," + this.getLng();
    }
}
