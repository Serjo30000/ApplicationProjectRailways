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
public interface TerminalRepository extends CrudRepository<Terminal,Integer> {
    @Query("SELECT Arrivals.id, Arrivals.terminal_id, Arrivals.ticket_id, Arrivals.date FROM Arrivals JOIN Terminals ON" +
            " Arrivals.terminal_id=Terminals.id WHERE Arrivals.terminal_id=:id ORDER BY Arrivals.date ASC")
    List<Arrival> findArrivalByAll(@Param("id")int terminalId);
    @Query("SELECT Towns.id,Towns.town_name,Towns.country_id FROM Towns JOIN Terminals ON Terminals.town_id=Towns.id" +
            " WHERE Terminals.id=:id")
    Optional<Town> findTownById(@Param("id")int terminalId);
    @Modifying
    @Query("UPDATE Terminals SET Terminals.name=:name, Terminals.town_id=:townId WHERE Terminals.id=:id")
    void updateTerminal(@Param("id")int id,@Param("name")String name,@Param("townId")int townId);
    @Query("SELECT Terminals.id, Terminals.name, Terminals.town_id FROM Terminals ORDER BY Terminals.name ASC")
    List<Terminal> findTerminalOrderByNameAsc();
    @Query("SELECT Terminals.id, Terminals.name, Terminals.town_id FROM Terminals ORDER BY Terminals.name DESC")
    List<Terminal> findTerminalOrderByNameDesc();
    @Query("SELECT Terminals.id, Terminals.name, Terminals.town_id FROM Terminals JOIN Towns ON Terminals.town_id=Towns.id" +
            " GROUP BY Terminals.id, Terminals.name, Terminals.town_id, Towns.town_name ORDER BY Towns.town_name ASC")
    List<Terminal> findTerminalOrderByNameTownAsc();
    @Query("SELECT Terminals.id, Terminals.name, Terminals.town_id FROM Terminals JOIN Towns ON Terminals.town_id=Towns.id" +
            " GROUP BY Terminals.id, Terminals.name, Terminals.town_id, Towns.town_name ORDER BY Towns.town_name DESC")
    List<Terminal> findTerminalOrderByNameTownDesc();
    @Query("SELECT Terminals.id, Terminals.name, Terminals.town_id FROM Terminals JOIN Towns ON Terminals.town_id=Towns.id" +
            " JOIN Countries ON Towns.country_id=Countries.id GROUP BY Terminals.id, Terminals.name, Terminals.town_id," +
            " Countries.country_name ORDER BY Countries.country_name ASC")
    List<Terminal> findTerminalOrderByNameCountryAsc();
    @Query("SELECT Terminals.id, Terminals.name, Terminals.town_id FROM Terminals JOIN Towns ON Terminals.town_id=Towns.id" +
            " JOIN Countries ON Towns.country_id=Countries.id GROUP BY Terminals.id, Terminals.name, Terminals.town_id," +
            " Countries.country_name ORDER BY Countries.country_name DESC")
    List<Terminal> findTerminalOrderByNameCountryDesc();
    @Query("SELECT Arrivals.id, Arrivals.terminal_id, Arrivals.ticket_id, Arrivals.date FROM Arrivals JOIN Terminals" +
            " ON Arrivals.terminal_id=Terminals.id JOIN Tickets ON Arrivals.ticket_id=Tickets.id JOIN Trains" +
            " ON Trains.id=Tickets.trainT_id GROUP BY Arrivals.id, Arrivals.terminal_id, Arrivals.ticket_id," +
            " Arrivals.date,Trains.name_train,Terminals.id HAVING Trains.name_train=:name and Terminals.id=:id")
    List<Arrival> findArrivalByName(@Param("id")int terminalId,@Param("name")String name);
}
