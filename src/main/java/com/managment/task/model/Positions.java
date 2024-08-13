package com.managment.task.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "positions")
public class Positions {
    @Id
    @Column(name = "positionid")
    private int positionId;
    @Column(name = "positionname")
    private String positionName;
    @Column(name = "departmentid")
    private int departmentId;

    public Positions() {}

    public Positions(int positionId, String positionName, int departmentId) {
        this.positionId = positionId;
        this.positionName = positionName;
        this.departmentId = departmentId;
    }
    
    public int getPositionId() {
        return positionId;
    }
    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }
    public String getPositionName() {
        return positionName;
    }
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
    public int getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

}
