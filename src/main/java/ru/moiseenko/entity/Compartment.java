package ru.moiseenko.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value="Compartments")
public class Compartment {
    @Id
    private int id;
    @Column(value = "van_id")
    private int vanId;
    @Column(value = "type_compartment")
    private String typeCompartment;
    public Compartment(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVanId() {
        return vanId;
    }

    public void setVanId(int vanId) {
        this.vanId = vanId;
    }

    public String getTypeCompartment() {
        return typeCompartment;
    }

    public void setTypeCompartment(String typeCompartment) {
        this.typeCompartment = typeCompartment;
    }
}
