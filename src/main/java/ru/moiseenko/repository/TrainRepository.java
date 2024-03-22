package ru.moiseenko.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moiseenko.entity.*;
import java.util.List;

@Repository
public interface TrainRepository extends CrudRepository<Train,Integer> {
    @Query("SELECT Vans.id, Vans.train_id, Vans.type_van FROM Vans JOIN Trains ON Vans.train_id=Trains.id WHERE" +
            " Vans.train_id=:id")
    List<Van> findVanByAll(@Param("id")int trainId);
    @Query("SELECT Tickets.id, Tickets.trainT_id FROM Tickets JOIN Trains ON Tickets.trainT_id=Trains.id WHERE" +
            " Tickets.trainT_id=:id")
    List<Ticket> findTicketByAll(@Param("id")int trainId);
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id," +
            " Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user," +
            " Orders.surname_user, Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode" +
            " FROM Orders JOIN Trains ON Orders.trainOr_id=Trains.id WHERE Orders.trainOr_id=:id")
    List<Order> findOrderByAll(@Param("id")int trainId);
    @Modifying
    @Query("UPDATE Trains SET Trains.name_train=:nameTrain, Trains.type_train=:typeTrain WHERE Trains.id=:id")
    void updateTrain(@Param("id")int id,@Param("nameTrain")String nameTrain,@Param("typeTrain")String typeTrain);
    @Query("SELECT Trains.id, Trains.name_train, Trains.type_train FROM Trains GROUP BY Trains.id, Trains.name_train," +
            " Trains.type_train ORDER BY Trains.name_train ASC")
    List<Train> findTrainOrderByNameAsc();
    @Query("SELECT Trains.id, Trains.name_train, Trains.type_train FROM Trains GROUP BY Trains.id, Trains.name_train," +
            " Trains.type_train ORDER BY Trains.name_train DESC")
    List<Train> findTrainOrderByNameDesc();
    @Query("SELECT Trains.id, Trains.name_train, Trains.type_train FROM Trains GROUP BY Trains.id, Trains.name_train," +
            " Trains.type_train ORDER BY Trains.type_train ASC")
    List<Train> findTrainOrderByTypeAsc();
    @Query("SELECT Trains.id, Trains.name_train, Trains.type_train FROM Trains GROUP BY Trains.id, Trains.name_train," +
            " Trains.type_train ORDER BY Trains.type_train DESC")
    List<Train> findTrainOrderByTypeDesc();
    @Query("SELECT Trains.id, Trains.name_train, Trains.type_train FROM Trains LEFT JOIN Vans ON Trains.id=Vans.train_id" +
            " GROUP BY Trains.id, Trains.name_train, Trains.type_train ORDER BY COUNT(Vans.id) ASC")
    List<Train> findTrainOrderByCountVanAsc();
    @Query("SELECT Trains.id, Trains.name_train, Trains.type_train FROM Trains LEFT JOIN Vans ON Trains.id=Vans.train_id" +
            " GROUP BY Trains.id, Trains.name_train, Trains.type_train ORDER BY COUNT(Vans.id) DESC")
    List<Train> findTrainOrderByCountVanDesc();
    @Query("SELECT Trains.id, Trains.name_train, Trains.type_train FROM Trains LEFT JOIN Vans ON Trains.id=Vans.train_id" +
            " LEFT JOIN Compartments ON Vans.id=Compartments.van_id LEFT JOIN Places ON Compartments.id=Places.compartment_id" +
            " GROUP BY Trains.id, Trains.name_train, Trains.type_train,Places.engaged HAVING COUNT(Vans.id)=0 or" +
            " COUNT(Compartments.id)=0 or COUNT(Places.id)=0 or Places.engaged='true' ORDER BY SUM(Places.price) ASC")
    List<Train> findTrainOrderByCountPlaceAsc();
    @Query("SELECT Trains.id, Trains.name_train, Trains.type_train FROM Trains LEFT JOIN Vans ON Trains.id=Vans.train_id" +
            " LEFT JOIN Compartments ON Vans.id=Compartments.van_id LEFT JOIN Places ON Compartments.id=Places.compartment_id" +
            " GROUP BY Trains.id, Trains.name_train, Trains.type_train,Places.engaged HAVING COUNT(Vans.id)=0 or" +
            " COUNT(Compartments.id)=0 or COUNT(Places.id)=0 or Places.engaged='true' ORDER BY SUM(Places.price) DESC")
    List<Train> findTrainOrderByCountPlaceDesc();
    @Query("SELECT Arrivals.id, Arrivals.terminal_id, Arrivals.ticket_id, Arrivals.date FROM Arrivals JOIN Tickets ON" +
            " Tickets.id=Arrivals.ticket_id JOIN Trains ON Tickets.trainT_id=Trains.id WHERE Trains.id=:id" +
            " ORDER BY Arrivals.date")
    List<Arrival> findArrivalByAll(@Param("id")int trainId);
    @Query("SELECT Trains.id,Trains.name_train,Trains.type_train FROM Trains GROUP BY Trains.id,Trains.name_train," +
            "Trains.type_train HAVING Trains.name_train=:name")
    List<Train> findTrainByName(@Param("name")String name);
}
