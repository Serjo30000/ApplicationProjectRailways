package ru.moiseenko.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(value="Notifications")
public class Notification {
    @Id
    private int id;
    @Column(value = "text_notification")
    private String textNotification;
    @Column(value = "id_users")
    private int idUser;
    @Column(value = "recipient")
    private String recipient;
    public Notification(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextNotification() {
        return textNotification;
    }

    public void setTextNotification(String textNotification) {
        this.textNotification = textNotification;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
