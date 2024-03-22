package ru.moiseenko.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value="Places")
public class Place {
    @Id
    private int id;
    @Column(value = "compartment_id")
    private int compartmentId;
    @Column(value = "engaged")
    private boolean engaged;
    @Column(value = "price")
    private int price;
    @Column(value = "number_place")
    private int numberPlace;
    public Place(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompartmentId() {
        return compartmentId;
    }

    public void setCompartmentId(int compartmentId) {
        this.compartmentId = compartmentId;
    }

    public boolean isEngaged() {
        return engaged;
    }

    public void setEngaged(boolean engaged) {
        this.engaged = engaged;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumberPlace() {
        return numberPlace;
    }

    public void setNumberPlace(int numberPlace) {
        this.numberPlace = numberPlace;
    }
}
