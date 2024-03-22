package ru.moiseenko.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value="Trains")
public class Train {
    @Id
    private int id;
    @Column(value = "name_train")
    private String nameTrain;
    @Column(value = "type_train")
    private  String typeTrain;
    public Train(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameTrain() {
        return nameTrain;
    }

    public void setNameTrain(String nameTrain) {
        this.nameTrain = nameTrain;
    }

    public String getTypeTrain() {
        return typeTrain;
    }

    public void setTypeTrain(String typeTrain) {
        this.typeTrain = typeTrain;
    }
}
