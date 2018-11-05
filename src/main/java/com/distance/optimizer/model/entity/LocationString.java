package com.distance.optimizer.model.entity;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author FarazAhmed
 */
@Document(collection = "location_strings")
public class LocationString extends BaseEntity {
    private String loc;
    private Boolean completed;

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationString that = (LocationString) o;

        if (loc != null ? !loc.equals(that.loc) : that.loc != null) return false;
        return !(completed != null ? !completed.equals(that.completed) : that.completed != null);

    }

    @Override
    public int hashCode() {
        int result = loc != null ? loc.hashCode() : 0;
        result = 31 * result + (completed != null ? completed.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LocationString{" +
                "loc='" + loc + '\'' +
                ", completed=" + completed +
                '}';
    }
}
