package ru.moiseenko.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moiseenko.entity.Compartment;
import ru.moiseenko.entity.Order;
import ru.moiseenko.entity.Train;
import ru.moiseenko.entity.Van;
import java.util.List;
import java.util.Optional;

@Repository
public interface VanRepository extends CrudRepository<Van,Integer> {
    @Query("SELECT Compartments.id, Compartments.van_id, Compartments.type_compartment FROM Compartments JOIN Vans " +
            "ON Compartments.van_id=Vans.id WHERE Compartments.van_id=:id")
    List<Compartment> findCompartmentByAll(@Param("id")int vanId);
    @Query("SELECT Trains.id,Trains.name_train,Trains.type_train FROM Trains JOIN Vans " +
            "ON Vans.train_id=Trains.id WHERE Vans.id=:id")
    Optional<Train>findTrainById(@Param("id")int vanId);
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, " +
            "Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, " +
            "Orders.surname_user, Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode " +
            "FROM Orders JOIN Vans ON Orders.vanOr_id=Vans.id WHERE Orders.vanOr_id=:id")
    List<Order> findOrderByAll(@Param("id")int vanId);
    @Modifying
    @Query("UPDATE Vans SET Vans.train_id=:trainId, Vans.type_van=:typeVan WHERE Vans.id=:id")
    void updateVan(@Param("id")int id,@Param("trainId")int trainId,@Param("typeVan")String typeVan);
    @Query("SELECT Vans.id, Vans.train_id, Vans.type_van FROM Vans GROUP BY Vans.id, Vans.train_id, " +
            "Vans.type_van ORDER BY Vans.type_van ASC")
    List<Van> findVanOrderByTypeAsc();
    @Query("SELECT Vans.id, Vans.train_id, Vans.type_van FROM Vans GROUP BY Vans.id, Vans.train_id, " +
            "Vans.type_van ORDER BY Vans.type_van DESC")
    List<Van> findVanOrderByTypeDesc();
    @Query("SELECT Vans.id, Vans.train_id, Vans.type_van FROM Vans JOIN Compartments ON Compartments.van_id=Vans.id " +
            "JOIN Places ON Places.compartment_id=Compartments.id GROUP BY Vans.id, Vans.train_id, Vans.type_van " +
            "ORDER BY SUM(Places.price) ASC")
    List<Van> findVanOrderByPriceAsc();
    @Query("SELECT Vans.id, Vans.train_id, Vans.type_van FROM Vans JOIN Compartments ON Compartments.van_id=Vans.id " +
            "JOIN Places ON Places.compartment_id=Compartments.id GROUP BY Vans.id, Vans.train_id, Vans.type_van " +
            "ORDER BY SUM(Places.price) DESC")
    List<Van> findVanOrderByPriceDesc();
    @Query("SELECT Vans.id, Vans.train_id, Vans.type_van FROM Vans JOIN Compartments ON Compartments.van_id=Vans.id " +
            "JOIN Places ON Places.compartment_id=Compartments.id GROUP BY Vans.id, Vans.train_id, Vans.type_van " +
            "ORDER BY SUM(Places.id) ASC")
    List<Van> findVanOrderByCountPlaceAsc();
    @Query("SELECT Vans.id, Vans.train_id, Vans.type_van FROM Vans JOIN Compartments ON Compartments.van_id=Vans.id " +
            "JOIN Places ON Places.compartment_id=Compartments.id GROUP BY Vans.id, Vans.train_id, Vans.type_van " +
            "ORDER BY SUM(Places.id) DESC")
    List<Van> findVanOrderByCountPlaceDesc();
}
