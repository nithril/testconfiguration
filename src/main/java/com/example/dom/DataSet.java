package com.example.dom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by nlabrot on 30/01/16.
 */
@Entity
@Table(name = "DATASET")
public class DataSet implements State {

    @Id
    private Long id;

    private String state;

    public DataSet() {
    }

    public DataSet(Long id, String state) {
        this.id = id;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
