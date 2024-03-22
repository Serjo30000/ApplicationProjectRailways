package ru.moiseenko.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moiseenko.entity.*;
import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country,Integer> {
    @Query("SELECT Towns.id, Towns.town_name, Towns.country_id FROM Towns JOIN Countries ON Towns.country_id=Countries.id " +
        "WHERE Towns.country_id=:id")
    List<Town> findTownByAll(@Param("id")int countryId);
    @Query("SELECT * FROM Countries WHERE country_name = :countryName")
    Country findByCountryName(@Param("countryName")String countryName);
    @Modifying
    @Query("UPDATE Countries SET Countries.country_name=:countryName WHERE Countries.id=:id")
    void updateCountry(@Param("id")int id,@Param("countryName")String countryName);
}
