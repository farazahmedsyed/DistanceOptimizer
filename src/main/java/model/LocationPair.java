package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Venturedive on 9/13/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationPair {
    private String srcLocStr;
    private String destLocStr;
    private Boolean sent;

    public String getSrcLocStr() {
        return srcLocStr;
    }

    public void setSrcLocStr(String srcLocStr) {
        this.srcLocStr = srcLocStr;
    }

    public String getDestLocStr() {
        return destLocStr;
    }

    public void setDestLocStr(String destLocStr) {
        this.destLocStr = destLocStr;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationPair that = (LocationPair) o;

        if (srcLocStr != null ? !srcLocStr.equals(that.srcLocStr) : that.srcLocStr != null) return false;
        if (destLocStr != null ? !destLocStr.equals(that.destLocStr) : that.destLocStr != null) return false;
        return !(sent != null ? !sent.equals(that.sent) : that.sent != null);

    }

    @Override
    public int hashCode() {
        int result = srcLocStr != null ? srcLocStr.hashCode() : 0;
        result = 31 * result + (destLocStr != null ? destLocStr.hashCode() : 0);
        result = 31 * result + (sent != null ? sent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LocationPair{" +
                "srcLocStr='" + srcLocStr + '\'' +
                ", destLocStr='" + destLocStr + '\'' +
                ", sent=" + sent +
                '}';
    }
}
