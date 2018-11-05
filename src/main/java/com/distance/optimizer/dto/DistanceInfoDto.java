package com.distance.optimizer.dto;

import java.io.Serializable;

/**
 * @author FarazAhmed
 */
public class DistanceInfoDto implements Serializable {

    private String text;
    private int value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

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

        DistanceInfoDto that = (DistanceInfoDto) o;

        if (value != that.value) return false;
        return !(text != null ? !text.equals(that.text) : that.text != null);

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + value;
        return result;
    }

    @Override
    public String toString() {
        return "DistanceInfoDto{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}
