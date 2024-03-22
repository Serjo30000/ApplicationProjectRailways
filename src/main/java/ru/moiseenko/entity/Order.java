package ru.moiseenko.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.sql.Date;


@Table(value="Orders")
public class Order {
    @Id
    private Integer id;
    @Column(value="ticketOr_id")
    private Integer ticketId;
    @Column(value="userOr_id")
    private Integer userId;
    @Column(value="arrivalOr_id")
    private Integer arrivalId;
    @Column(value="trainOr_id")
    private Integer trainId;
    @Column(value="vanOr_id")
    private Integer vanId;
    @Column(value="placeOr_id")
    private Integer placeId;
    @Column(value="compartmentOr_id")
    private Integer compartmentId;
    @Column(value="credit_card")
    private String creditCard;
    @Column(value="name_user")
    private String nameUser;
    @Column(value="surname_user")
    private String surnameUser;
    @Column(value="patronymic_user")
    private String patronymicUser;
    @Column(value="sex_user")
    private String sexUser;
    @Column(value="birthday_user")
    private Date birthdayUser;
    @Column(value="barcode")
    private String barcode;
    public Order(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(Integer arrivalId) {
        this.arrivalId = arrivalId;
    }

    public Integer getTrainId() {
        return trainId;
    }

    public void setTrainId(Integer trainId) {
        this.trainId = trainId;
    }

    public Integer getVanId() {
        return vanId;
    }

    public void setVanId(Integer vanId) {
        this.vanId = vanId;
    }

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public Integer getCompartmentId() {
        return compartmentId;
    }

    public void setCompartmentId(Integer compartmentId) {
        this.compartmentId = compartmentId;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getSurnameUser() {
        return surnameUser;
    }

    public void setSurnameUser(String surnameUser) {
        this.surnameUser = surnameUser;
    }

    public String getPatronymicUser() {
        return patronymicUser;
    }

    public void setPatronymicUser(String patronymicUser) {
        this.patronymicUser = patronymicUser;
    }

    public String getSexUser() {
        return sexUser;
    }

    public void setSexUser(String sexUser) {
        this.sexUser = sexUser;
    }


    public String getBarcode() {
        return barcode;
    }


    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Date getBirthdayUser() {
        return birthdayUser;
    }

    public void setBirthdayUser(Date birthdayUser) {
        this.birthdayUser = birthdayUser;
    }
}
