package dto;

import java.io.Serializable;

/**
 * Created by Venturedive on 9/13/2017.
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
}
