package ru.moiseenko.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.moiseenko.entity.Card;

public interface CardRepository extends CrudRepository<Card,Integer> {
    @Modifying
    @Query("UPDATE Cards SET Cards.price_card=:price WHERE Cards.id=:id")
    void updateCard(@Param("id")int id, @Param("price")int price);
    @Query("SELECT * FROM Cards WHERE Cards.name_card = :nameCard")
    Card findByNameCard(@Param("nameCard")String nameCard);
}
