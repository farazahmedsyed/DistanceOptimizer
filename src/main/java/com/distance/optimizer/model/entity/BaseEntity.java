package com.distance.optimizer.model.entity;


import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author FarazAhmed
 */
public abstract class BaseEntity implements Serializable {
    @Id
    protected String id;
    protected Date updatedTime;
    protected Date createdTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

}
