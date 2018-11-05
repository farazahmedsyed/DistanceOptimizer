package com.distance.optimizer.model.entity;

import com.distance.optimizer.dto.LocationPairDto;
import com.distance.optimizer.utils.EntityHelper;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FarazAhmed
 */
@Document(collection = "location_pairs")
public class LocationPair extends BaseEntity {
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
        return "LocationPairDto{" +
                "srcLocStr='" + srcLocStr + '\'' +
                ", destLocStr='" + destLocStr + '\'' +
                ", sent=" + sent +
                '}';
    }

    public static LocationPairDto convertToDto(LocationPair locationPair){
        LocationPairDto locationPairDto = null;
        if (EntityHelper.isNotNull(locationPair)){
            locationPairDto = new LocationPairDto();
            locationPairDto.setSent(locationPair.getSent());
            locationPairDto.setDestLocStr(locationPair.getDestLocStr());
            locationPairDto.setSrcLocStr(locationPair.getSrcLocStr());
        }
        return locationPairDto;
    }

    public static List<LocationPairDto> convertToDto(List<LocationPair> locationPairs){
        List<LocationPairDto> locationPairDtos = null;
        if (EntityHelper.isListPopulated(locationPairs)){
            locationPairDtos = new ArrayList<>();
            for (LocationPair locationPair : locationPairs){
                locationPairDtos.add(convertToDto(locationPair));
            }
        }
        return locationPairDtos;
    }
}
