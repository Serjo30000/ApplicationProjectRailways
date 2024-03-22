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
public interface CompartmentRepository extends CrudRepository<Compartment,Integer> {
    @Query("SELECT Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place FROM Places JOIN " +
        "Compartments ON Places.compartment_id=Compartments.id WHERE Places.compartment_id=:id")
    List<Place> findPlaceByAll(@Param("id")int compartmentId);
    @Query("SELECT Vans.id,Vans.train_id,Vans.type_van FROM Vans JOIN Compartments ON Compartments.van_id=Vans.id " +
        "WHERE Compartments.id=:id")
    Optional<Van> findVanById(@Param("id")int compartmentId);
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, " +
        "Orders.vanOr_id, Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Compartments ON Orders.compartmentOr_id=Compartments.id WHERE Orders.compartmentOr_id=:id")
    List<Order> findOrderByAll(@Param("id")int compartmentId);
    @Modifying
    @Query("UPDATE Compartments SET Compartments.van_id=:vanId, Compartments.type_compartment=:typeCompartment " +
        "WHERE Compartments.id=:id")
    void updateCompartment(@Param("id")int id,@Param("vanId")int vanId,@Param("typeCompartment")String typeCompartment);
    @Query("SELECT Compartments.id, Compartments.van_id, Compartments.type_compartment FROM Compartments GROUP BY " +
        "Compartments.id, Compartments.van_id, Compartments.type_compartment ORDER BY Compartments.type_compartment ASC")
    List<Compartment> findCompartmentOrderByTypeAsc();
    @Query("SELECT Compartments.id, Compartments.van_id, Compartments.type_compartment FROM Compartments GROUP BY " +
        "Compartments.id, Compartments.van_id, Compartments.type_compartment ORDER BY Compartments.type_compartment DESC")
    List<Compartment> findCompartmentOrderByTypeDesc();
    @Query("SELECT Compartments.id, Compartments.van_id, Compartments.type_compartment FROM Compartments JOIN Places " +
        "ON Compartments.id=Places.compartment_id GROUP BY Compartments.id, Compartments.van_id, Compartments.type_compartment " +
        "ORDER BY AVG(Places.price) ASC")
    List<Compartment> findCompartmentOrderByPriceAsc();
    @Query("SELECT Compartments.id, Compartments.van_id, Compartments.type_compartment FROM Compartments JOIN Places ON" +
        " Compartments.id=Places.compartment_id GROUP BY Compartments.id, Compartments.van_id, Compartments.type_compartment ORDER BY AVG(Places.price) DESC")
    List<Compartment> findCompartmentOrderByPriceDesc();
    @Query("SELECT Compartments.id, Compartments.van_id, Compartments.type_compartment FROM Compartments JOIN Places ON " +
        "Compartments.id=Places.compartment_id GROUP BY Compartments.id, Compartments.van_id, Compartments.type_compartment " +
        "ORDER BY Count(Places.id) ASC")
    List<Compartment> findCompartmentOrderByCountAsc();
    @Query("SELECT Compartments.id, Compartments.van_id, Compartments.type_compartment FROM Compartments JOIN Places ON " +
        "Compartments.id=Places.compartment_id GROUP BY Compartments.id, Compartments.van_id, Compartments.type_compartment " +
        "ORDER BY Count(Places.id) DESC")
    List<Compartment> findCompartmentOrderByCountDesc();
}
