package com.kerellpnz.tnnwebdatabase.entity.general;



import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "specifications")
public class Specification extends BaseEntity {

    @Column(name = "Number")
    private String number;

    @Column(name = "Program")
    private String program;

    @Column(name = "Facility")
    private String facility;

    public Specification() {

    }

    @OneToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="CustomerId")
    public Customer customer;

    @OneToMany(fetch= FetchType.EAGER, mappedBy="specification", cascade=CascadeType.ALL)
    private List<PID> pids;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<PID> getPids() {
        return pids;
    }

    public void setPids(List<PID> pids) {
        this.pids = pids;
    }

}
