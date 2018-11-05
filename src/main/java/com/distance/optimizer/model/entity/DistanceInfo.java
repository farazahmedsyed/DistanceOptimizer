package com.distance.optimizer.model.entity;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author FarazAhmed
 */
public class DistanceInfo {
    private String text;
    private int value;

    @Field("t")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Field("v")
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistanceInfo that = (DistanceInfo) o;

        if (value != that.value) return false;
        return !(text != null ? !text.equals(that.text) : that.text != null);

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "DistanceInfo{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}
