package ru.moiseenko.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moiseenko.entity.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order,Integer> {
    @Query("SELECT Tickets.id, Tickets.trainT_id FROM Tickets JOIN Orders ON Orders.ticketOr_id=Tickets.id WHERE Orders.id=:id")
    Optional<Ticket> findTicketById(@Param("id")int orderId);
    @Query("SELECT Users.id, Users.username,Users.password,Users.phone,Users.email,Users.founder,Users.reg_number,Users.location " +
        "FROM Users JOIN Orders ON Orders.userOr_id=Users.id WHERE Orders.id=:id")
    Optional<User> findUserById(@Param("id")int orderId);
    @Query("SELECT Arrivals.id, Arrivals.terminal_id, Arrivals.ticket_id, Arrivals.date FROM Arrivals JOIN Orders ON " +
        "Orders.arrivalOr_id=Arrivals.id WHERE Orders.id=:id")
    Optional<Arrival> findArrivalById(@Param("id")int orderId);
    @Query("SELECT Trains.id, Trains.name_train, Trains.type_train FROM Trains JOIN Orders ON Orders.trainOr_id=Trains.id" +
        " WHERE Orders.id=:id")
    Optional<Train> findTrainById(@Param("id")int orderId);
    @Query("SELECT Vans.id, Vans.train_id, Vans.type_van FROM Vans JOIN Orders ON Orders.vanOr_id=Vans.id WHERE Orders.id=:id")
    Optional<Van> findVanById(@Param("id")int orderId);
    @Query("SELECT Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place FROM Places JOIN " +
        "Orders ON Orders.placeOr_id=Places.id WHERE Orders.id=:id")
    Optional<Place> findPlaceById(@Param("id")int orderId);
    @Query("SELECT Compartments.id, Compartments.van_id, Compartments.type_compartment FROM Compartments JOIN Orders ON " +
        "Orders.compartmentOr_id=Compartments.id WHERE Orders.id=:id")
    Optional<Compartment> findCompartmentById(@Param("id")int orderId);
    @Modifying
    @Query("UPDATE Orders SET Orders.ticketOr_id=:ticketId, Orders.userOr_id=:userId, Orders.arrivalOr_id=:arrivalId, " +
        "Orders.trainOr_id=:trainId, Orders.vanOr_id=:vanId, Orders.placeOr_id=:placeId, Orders.compartmentOr_id=:compartmentId," +
            " Orders.credit_card=:creditCard, Orders.name_user=:nameUser, Orders.surname_user=:surnameUser, Orders.patronymic_user=:patronymicUser," +
            " Orders.sex_user=:sexUser, Orders.birthday_user=:birthdayUser, Orders.barcode=:barcode WHERE Orders.id=:id")
    void updateOrder(@Param("id")Integer id, @Param("ticketId")Integer ticketId, @Param("userId")Integer userId,
                     @Param("arrivalId")Integer arrivalId, @Param("trainId")Integer trainId, @Param("vanId")Integer vanId,
                     @Param("placeId")Integer placeId, @Param("compartmentId")Integer compartmentId, @Param("creditCard")String creditCard,
                     @Param("nameUser")String nameUser, @Param("surnameUser")String surnameUser, @Param("patronymicUser")String patronymicUser,
                     @Param("sexUser")String sexUser, @Param("birthdayUser") Date birthdayUser, @Param("barcode")String barcode);
    @Query("SELECT * FROM Orders WHERE Orders.userOr_id=:id")
    List<Order> findOrderByUser(@Param("id")int userId);
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, " +
        "Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, " +
        "Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders ORDER BY Orders.ticketOr_id ASC")
    List<Order> findOrderOrderByTicketAsc();
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, " +
        "Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, " +
        "Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders ORDER BY Orders.ticketOr_id DESC")
    List<Order> findOrderOrderByTicketDesc();
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id, " +
        "Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user, " +
        "Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Trains ON Orders.trainOr_id=Trains.id GROUP BY " +
        "Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id, Orders.placeOr_id, " +
        "Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user, Orders.sex_user, " +
        "Orders.birthday_user, Orders.barcode,Trains.name_train Order BY Trains.name_train ASC")
    List<Order> findOrderOrderByTrainAsc();
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id, " +
        "Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user, " +
        "Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Trains ON Orders.trainOr_id=Trains.id GROUP " +
        "BY Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id, Orders.placeOr_id," +
        " Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user, " +
        "Orders.sex_user, Orders.birthday_user, Orders.barcode,Trains.name_train Order BY Trains.name_train DESC")
    List<Order> findOrderOrderByTrainDesc();
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, " +
        "Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, " +
        "Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Arrivals ON " +
        "Orders.arrivalOr_id=Arrivals.id GROUP BY Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, " +
        "Orders.trainOr_id, Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, " +
        "Orders.surname_user, Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode,Arrivals.date " +
        "Order BY Arrivals.date ASC")
    List<Order> findOrderOrderByArrivalAsc();
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, " +
        "Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, " +
        "Orders.surname_user, Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN " +
        "Arrivals ON Orders.arrivalOr_id=Arrivals.id GROUP BY Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, " +
        "Orders.trainOr_id, Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, " +
        "Orders.surname_user, Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode,Arrivals.date " +
        "Order BY Arrivals.date DESC")
    List<Order> findOrderOrderByArrivalDesc();
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id, " +
        "Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user, " +
        "Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Compartments ON Orders.compartmentOr_id=Compartments.id " +
        "GROUP BY Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id, " +
        "Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user," +
        " Orders.sex_user, Orders.birthday_user, Orders.barcode,Compartments.type_compartment Order BY Compartments.type_compartment ASC")
    List<Order> findOrderOrderByCompartmentAsc();
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id," +
        " Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user," +
        " Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Compartments ON Orders.compartmentOr_id=Compartments.id" +
        " GROUP BY Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id," +
        " Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user," +
        " Orders.sex_user, Orders.birthday_user, Orders.barcode,Compartments.type_compartment Order BY Compartments.type_compartment DESC")
    List<Order> findOrderOrderByCompartmentDesc();
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id," +
        " Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user," +
        " Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Places ON Orders.placeOr_id=Places.id GROUP BY Orders.id," +
        " Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id, Orders.placeOr_id," +
        " Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user," +
        " Orders.sex_user, Orders.birthday_user, Orders.barcode,Places.number_place Order BY Places.number_place ASC")
    List<Order> findOrderOrderByPlaceNumberAsc();
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id," +
        " Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user," +
        " Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Places ON Orders.placeOr_id=Places.id" +
        " GROUP BY Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id," +
        " Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user," +
        " Orders.sex_user, Orders.birthday_user, Orders.barcode,Places.number_place Order BY Places.number_place DESC")
    List<Order> findOrderOrderByPlaceNumberDesc();
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id," +
        " Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user," +
        " Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Places ON Orders.placeOr_id=Places.id GROUP BY Orders.id," +
        " Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id, Orders.placeOr_id," +
        " Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user, Orders.sex_user," +
        " Orders.birthday_user, Orders.barcode,Places.price Order BY Places.price ASC")
    List<Order> findOrderOrderByPlacePriceAsc();
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id," +
        " Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user," +
        " Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Places ON Orders.placeOr_id=Places.id GROUP BY Orders.id," +
        " Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id," +
        " Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user, Orders.sex_user, Orders.birthday_user," +
        " Orders.barcode,Places.price Order BY Places.price DESC")
    List<Order> findOrderOrderByPlacePriceDesc();
}
