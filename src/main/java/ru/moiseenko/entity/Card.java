package ru.moiseenko.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.sql.Date;

@Table(value="Cards")
public class Card {
    @Id
    private int id;
    @Column(value = "name_card")
    private String nameCard;
    @Column(value = "password_card")
    private String passwordCard;
    @Column(value = "date_card")
    private Date dateCard;
    @Column(value = "price_card")
    private int priceCard;
    public Card(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    public String getPasswordCard() {
        return passwordCard;
    }

    public void setPasswordCard(String passwordCard) {
        this.passwordCard = passwordCard;
    }

    public int getPriceCard() {
        return priceCard;
    }

    public void setPriceCard(int priceCard) {
        this.priceCard = priceCard;
    }

    public Date getDateCard() {
        return dateCard;
    }

    public void setDateCard(Date dateCard) {
        this.dateCard = dateCard;
    }
}
