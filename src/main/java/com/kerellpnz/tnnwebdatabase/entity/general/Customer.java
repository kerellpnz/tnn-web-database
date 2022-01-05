package com.kerellpnz.tnnwebdatabase.entity.general;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    @Column(name = "Name")
    private String name;

    @Column(name = "ShortName")
    private String shortName;

    public Customer() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}
