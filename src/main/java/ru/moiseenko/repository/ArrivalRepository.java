package ru.moiseenko.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moiseenko.entity.*;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArrivalRepository extends CrudRepository<Arrival,Integer> {
    @Query("SELECT Terminals.id,Terminals.name,Terminals.town_id FROM Terminals JOIN Arrivals ON " +
        "Arrivals.terminal_id=Terminals.id WHERE Arrivals.id=:id")
    Optional<Terminal> findTerminalById(@Param("id")int arrivalId);
    @Query("SELECT Tickets.id,Tickets.trainT_id FROM Tickets JOIN Arrivals ON Arrivals.ticket_id=Tickets.id" +
        " WHERE Arrivals.id=:id")
    Optional<Ticket> findTicketById(@Param("id")int arrivalId);
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id," +
        " Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user," +
        " Orders.surname_user, Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders " +
        "JOIN Arrivals ON Orders.arrivalOr_id=Arrivals.id WHERE Orders.arrivalOr_id=:id")
    List<Order> findOrderByAll(@Param("id")int arrivalId);
    @Modifying
    @Query("UPDATE Arrivals SET Arrivals.terminal_id=:terminalId, Arrivals.ticket_id=:ticketId, Arrivals.date=:date" +
        " WHERE Arrivals.id=:id")
    void updateArrival(@Param("id")int id,@Param("terminalId")int terminalId,@Param("ticketId")int ticketId,@Param("date") Date date);
    @Query("SELECT Arrivals.id,Arrivals.terminal_id,Arrivals.ticket_id,Arrivals.date FROM Arrivals GROUP BY" +
        " Arrivals.id,Arrivals.terminal_id,Arrivals.ticket_id,Arrivals.date ORDER BY Arrivals.date ASC")
    List<Arrival> findArrivalOrderByDateAsc();
    @Query("SELECT Arrivals.id,Arrivals.terminal_id,Arrivals.ticket_id,Arrivals.date FROM Arrivals GROUP BY " +
        "Arrivals.id,Arrivals.terminal_id,Arrivals.ticket_id,Arrivals.date ORDER BY Arrivals.date DESC")
    List<Arrival> findArrivalOrderByDateDesc();
    @Query("SELECT Trains.id,Trains.name_train,Trains.type_train FROM Trains JOIN Tickets ON Trains.id=Tickets.trainT_id " +
        "JOIN Arrivals ON Tickets.id=Arrivals.ticket_id GROUP BY Trains.id,Trains.name_train,Trains.type_train,Arrivals.id HAVING " +
        "Arrivals.id=:id")
    Optional<Train> findTrainById(@Param("id")int arrivalId);
}
