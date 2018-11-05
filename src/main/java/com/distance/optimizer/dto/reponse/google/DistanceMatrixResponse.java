package com.distance.optimizer.dto.reponse.google;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author FarazAhmed
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistanceMatrixResponse implements Serializable {
    private String[] destinationAddresses;
    private String[] originAddresses;
    private List<Row> rows;
    private String status;
    private String errorMessage;

    @JsonProperty("destination_addresses")
    public String[] getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(String[] destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    @JsonProperty("origin_addresses")
    public String[] getOriginAddresses() {
        return originAddresses;
    }

    public void setOriginAddresses(String[] originAddresses) {
        this.originAddresses = originAddresses;
    }

    @JsonProperty("rows")
    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("error_message")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "DistanceMatrixResponse{" +
                "destinationAddresses=" + Arrays.toString(destinationAddresses) +
                ", originAddresses=" + Arrays.toString(originAddresses) +
                ", rows=" + rows +
                ", status='" + status + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
