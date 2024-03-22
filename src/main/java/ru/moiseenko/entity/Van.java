package ru.moiseenko.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value="Vans")
public class Van {
    @Id
    private int id;
    @Column(value = "train_id")
    private  int trainId;
    @Column(value = "type_van")
    private String typeVan;

    public Van(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public String getTypeVan() {
        return typeVan;
    }

    public void setTypeVan(String typeVan) {
        this.typeVan = typeVan;
    }
}
