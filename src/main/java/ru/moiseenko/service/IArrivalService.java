package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import java.util.List;

@Service
public interface IArrivalService {
    boolean updateArrival(Arrival arrival);
    Terminal findTerminalByArrivalId(int arrivalId);
    Train findTrainByArrivalId(int arrivalId);
    List<Arrival> sortArrival(List<Arrival>lstArv, String sortName, String sortBy);
    boolean saveArrivalCompany(Arrival arrival,String name);
    boolean updateArrivalCompany(Arrival arrival,String name);
    Ticket findTicketByArrivalId(int arrivalId);
    List<Order> allOrders(int arrivalId);
}
