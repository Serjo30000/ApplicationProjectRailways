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
public interface PlaceRepository extends CrudRepository<Place,Integer> {
    @Query("SELECT Compartments.id,Compartments.van_id,Compartments.type_compartment FROM Compartments JOIN Places ON " +
            "Places.compartment_id=Compartments.id WHERE Places.id=:id")
    Optional<Compartment> findCompartmentById(@Param("id")int placeId);
    @Query("SELECT Orders.id, Orders.ticketOr_id, Orders.userOr_id, Orders.arrivalOr_id, Orders.trainOr_id, Orders.vanOr_id," +
            " Orders.placeOr_id, Orders.compartmentOr_id, Orders.credit_card, Orders.name_user, Orders.surname_user, " +
            "Orders.patronymic_user, Orders.sex_user, Orders.birthday_user, Orders.barcode FROM Orders JOIN Places ON " +
            "Orders.placeOr_id=Places.id WHERE Orders.placeOr_id=:id")
    List<Order> findOrderByAll(@Param("id")int placeId);
    @Modifying
    @Query("UPDATE Places SET Places.compartment_id=:compartmentId, Places.engaged=:engaged, Places.price=:price, " +
            "Places.number_place=:numberPlace WHERE Places.id=:id")
    void updatePlace(@Param("id")int id,@Param("compartmentId")int compartmentId,@Param("engaged")boolean engaged,
                     @Param("price")int price,@Param("numberPlace")int numberPlace);
    @Query("SELECT Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place FROM Places " +
            "GROUP BY Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place ORDER BY " +
            "Places.engaged ASC")
    List<Place> findPlaceOrderByEngagedAsc();
    @Query("SELECT Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place FROM Places " +
            "GROUP BY Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place ORDER BY " +
            "Places.engaged DESC")
    List<Place> findPlaceOrderByEngagedDesc();
    @Query("SELECT Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place FROM Places " +
            "GROUP BY Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place ORDER BY " +
            "Places.price ASC")
    List<Place> findPlaceOrderByPriceAsc();
    @Query("SELECT Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place FROM Places " +
            "GROUP BY Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place ORDER BY " +
            "Places.price DESC")
    List<Place> findPlaceOrderByPriceDesc();
    @Query("SELECT Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place FROM Places " +
            "GROUP BY Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place ORDER BY " +
            "Places.number_place ASC")
    List<Place> findPlaceOrderByNumberAsc();
    @Query("SELECT Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place FROM Places " +
            "GROUP BY Places.id, Places.compartment_id, Places.engaged, Places.price, Places.number_place ORDER BY " +
            "Places.number_place DESC")
    List<Place> findPlaceOrderByNumberDesc();
}
