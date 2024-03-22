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
public interface TownRepository extends CrudRepository<Town,Integer> {
    @Query("SELECT Terminals.id, Terminals.name, Terminals.town_id FROM Terminals JOIN Towns ON" +
            " Terminals.town_id=Towns.id WHERE Terminals.town_id=:id")
    List<Terminal> findTerminalByAll(@Param("id")int townId);
    @Query("SELECT Countries.id,Countries.country_name FROM Countries JOIN Towns ON Towns.country_id=Countries.id WHERE" +
            " Towns.id=:id")
    Optional<Country> findCountryById(@Param("id")int townId);
    @Modifying
    @Query("UPDATE Towns SET Towns.town_name=:townName, Towns.country_id=:countryId WHERE Towns.id=:id")
    void updateTown(@Param("id")int id,@Param("townName")String townName,@Param("countryId")int countryId);
}
