package ru.moiseenko.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moiseenko.entity.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends CrudRepository<Ticket,Integer> {
    @Query("SELECT Trains.id,Trains.name_train,Trains.type_train FROM Trains JOIN Tickets ON Tickets.trainT_id=Trains.id" +
            " WHERE Tickets.id=:id")
    Optional<Train> findTrainById(@Param("id")int ticketId);
    @Query("SELECT Arrivals.id, Arrivals.terminal_id, Arrivals.ticket_id , Arrivals.date  FROM Arrivals JOIN Tickets ON" +
            " Arrivals.ticket_id=Tickets.id WHERE Arrivals.ticket_id=:id ORDER BY Arrivals.date")
    List<Arrival> findArrivalByAll(@Param("id")int ticketId);
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id," +
            " Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user," +
            " Orders.surname_user, Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode" +
            " FROM Orders JOIN Tickets ON Orders.ticketOr_id=Tickets.id WHERE Orders.ticketOr_id=:id")
    List<Order> findOrderByAll(@Param("id")int ticketId);
    @Modifying
    @Query("UPDATE Tickets SET Tickets.trainT_id=:trainId WHERE Tickets.id=:id")
    void updateTicket(@Param("id")int id,@Param("trainId")int trainId);
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets LEFT JOIN Trains ON Trains.id=Tickets.trainT_id" +
            " LEFT JOIN Vans ON Vans.train_id=Trains.id LEFT JOIN Compartments ON Compartments.van_id=Vans.id" +
            " LEFT JOIN Places ON Places.compartment_id=Compartments.id GROUP BY Tickets.id,Tickets.trainT_id" +
            " ORDER BY AVG(Places.price) ASC")
    List<Ticket> findTicketOrderByPriceAsc();
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets LEFT JOIN Trains ON Trains.id=Tickets.trainT_id LEFT" +
            " JOIN Vans ON Vans.train_id=Trains.id LEFT JOIN Compartments ON Compartments.van_id=Vans.id LEFT JOIN" +
            " Places ON Places.compartment_id=Compartments.id GROUP BY Tickets.id,Tickets.trainT_id ORDER BY" +
            " AVG(Places.price) DESC")
    List<Ticket> findTicketOrderByPriceDesc();
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Arrivals ON Tickets.id=Arrivals.ticket_id GROUP" +
            " BY Tickets.id,Tickets.trainT_id ORDER BY Min(Arrivals.date) ASC")
    List<Ticket> findTicketOrderByMinDateAsc();
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Arrivals ON Tickets.id=Arrivals.ticket_id GROUP BY" +
            " Tickets.id,Tickets.trainT_id ORDER BY Min(Arrivals.date) DESC")
    List<Ticket> findTicketOrderByMinDateDesc();
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Arrivals ON Tickets.id=Arrivals.ticket_id GROUP BY" +
            " Tickets.id,Tickets.trainT_id ORDER BY Max(Arrivals.date) ASC")
    List<Ticket> findTicketOrderByMaxDateAsc();
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Arrivals ON Tickets.id=Arrivals.ticket_id GROUP BY" +
            " Tickets.id,Tickets.trainT_id ORDER BY Max(Arrivals.date) DESC")
    List<Ticket> findTicketOrderByMaxDateDesc();
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Arrivals ON Tickets.id=Arrivals.ticket_id GROUP BY" +
            " Tickets.id,Tickets.trainT_id ORDER BY DATEDIFF(MONTH,Max(Arrivals.date),Min(Arrivals.date)) ASC")
    List<Ticket> findTicketOrderByDateDiffAsc();
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Arrivals ON Tickets.id=Arrivals.ticket_id GROUP BY" +
            " Tickets.id,Tickets.trainT_id ORDER BY DATEDIFF(MONTH,Max(Arrivals.date),Min(Arrivals.date)) DESC")
    List<Ticket> findTicketOrderByDateDiffDesc();
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Trains ON Trains.id=Tickets.trainT_id JOIN Vans ON" +
            " Vans.train_id=Trains.id JOIN Compartments ON Compartments.van_id=Vans.id JOIN Places ON" +
            " Places.compartment_id=Compartments.id GROUP BY Tickets.id,Tickets.trainT_id HAVING AVG(Places.price)>=:price" +
            " and Tickets.id=:id")
    Ticket findFilterByPrice(@Param("id")int id,@Param("price")int price);
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Arrivals ON Tickets.id=Arrivals.ticket_id GROUP BY" +
            " Tickets.id,Tickets.trainT_id HAVING Min(Arrivals.date)>=:minDate and Tickets.id=:id")
    Ticket findFilterByMinDate(@Param("id")int id,@Param("minDate") String minDate);
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Arrivals ON Tickets.id=Arrivals.ticket_id GROUP BY" +
            " Tickets.id,Tickets.trainT_id HAVING Max(Arrivals.date)>=:maxDate and Tickets.id=:id")
    Ticket findFilterByMaxDate(@Param("id")int id,@Param("maxDate") String maxDate);
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Trains ON Tickets.trainT_id=Trains.id GROUP BY" +
            " Tickets.id,Tickets.trainT_id,Trains.name_train HAVING Trains.name_train=:nameTrain and Tickets.id=:id")
    Ticket findFilterByNameTrain(@Param("id")int id,@Param("nameTrain")String nameTrain);
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Trains ON Tickets.trainT_id=Trains.id GROUP BY" +
            " Tickets.id,Tickets.trainT_id,Trains.type_train HAVING Trains.type_train=:typeTrain and Tickets.id=:id")
    Ticket findFilterByTypeTrain(@Param("id")int id,@Param("typeTrain")String typeTrain);
    @Query("SELECT Tickets.id, Tickets.trainT_id FROM Tickets JOIN Trains ON Tickets.trainT_id=Trains.id GROUP BY" +
            " Tickets.id, Tickets.trainT_id, Trains.name_train HAVING Trains.name_train=:name")
    List<Ticket> findTicketByName(@Param("name")String name);
    @Query("SELECT Tickets.id, Tickets.trainT_id FROM Tickets LEFT JOIN Trains ON Trains.id=Tickets.trainT_id LEFT JOIN" +
            " Vans ON Vans.train_id=Trains.id LEFT JOIN Compartments ON Compartments.van_id=Vans.id LEFT JOIN Places ON" +
            " Places.compartment_id=Compartments.id GROUP BY Tickets.id, Tickets.trainT_id ORDER BY COUNT(Places.id) ASC")
    List<Ticket> findTicketOrderByCountPlaceAsc();
    @Query("SELECT Tickets.id, Tickets.trainT_id FROM Tickets LEFT JOIN Trains ON Trains.id=Tickets.trainT_id LEFT JOIN" +
            " Vans ON Vans.train_id=Trains.id LEFT JOIN Compartments ON Compartments.van_id=Vans.id LEFT JOIN Places ON" +
            " Places.compartment_id=Compartments.id GROUP BY Tickets.id, Tickets.trainT_id ORDER BY COUNT(Places.id) DESC")
    List<Ticket> findTicketOrderByCountPlaceDesc();
    @Query("SELECT Tickets.id, Tickets.trainT_id FROM Tickets LEFT JOIN Trains ON Trains.id=Tickets.trainT_id LEFT JOIN" +
            " Vans ON Vans.train_id=Trains.id LEFT JOIN Compartments ON Compartments.van_id=Vans.id LEFT JOIN Places ON" +
            " Places.compartment_id=Compartments.id GROUP BY Tickets.id, Tickets.trainT_id,Places.engaged HAVING COUNT(Vans.id)=0" +
            " or COUNT(Compartments.id)=0 or COUNT(Places.id)=0 or Places.engaged='true' ORDER BY COUNT(Places.id) ASC")
    List<Ticket> findTicketOrderByCountPlaceAllAsc();
    @Query("SELECT Tickets.id, Tickets.trainT_id FROM Tickets LEFT JOIN Trains ON Trains.id=Tickets.trainT_id LEFT JOIN" +
            " Vans ON Vans.train_id=Trains.id LEFT JOIN Compartments ON Compartments.van_id=Vans.id LEFT JOIN Places ON" +
            " Places.compartment_id=Compartments.id GROUP BY Tickets.id, Tickets.trainT_id,Places.engaged HAVING COUNT(Vans.id)=0" +
            " or COUNT(Compartments.id)=0 or COUNT(Places.id)=0 or Places.engaged='true' ORDER BY COUNT(Places.id) DESC")
    List<Ticket> findTicketOrderByCountPlaceAllDesc();
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Trains ON Trains.id=Tickets.trainT_id JOIN Vans ON" +
            " Trains.id=Vans.train_id JOIN Compartments ON Compartments.van_id=Vans.id JOIN Places ON" +
            " Compartments.id=Places.compartment_id JOIN Arrivals ON Arrivals.ticket_id=Tickets.id GROUP BY" +
            " Tickets.id,Tickets.trainT_id,Places.engaged HAVING SUM(Places.price)>0 and Places.engaged='true'" +
            " and COUNT(Arrivals.id)>0")
    List<Ticket> findTicketByValidate();
}
