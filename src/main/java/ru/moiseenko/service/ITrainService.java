package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import java.util.List;

@Service
public interface ITrainService {
    boolean updateTrain(Train train);
    List<Van> allVans(int trainId);
    List<Ticket> allTickets(int trainId);
    List<Order> allOrders(int trainId);
    int countPlaceTrain(int trainId);
    List<Train> sortTrain(List<Train>lstTr,String sortName,String sortBy);
    List<Arrival> allArrivals(int trainId);
    int countPlaceTrainAll(int trainId);
    double averagePricePlace(int trainId);
    List<Train> allTrainsByName(String name);
    boolean saveTrainCompany(Train train,String name);
}
