package com.example.dom;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by nlabrot on 30/01/16.
 */
@Entity
@Table(name = "PIECEGROUP")
public class PieceGroup implements State {

    @Id
    private Long id;

    private String state;

    public PieceGroup() {
    }

    public PieceGroup(Long id, String state) {
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
