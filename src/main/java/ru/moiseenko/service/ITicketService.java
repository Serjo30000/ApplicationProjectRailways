package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Service
public interface ITicketService {
    boolean updateTicket(Ticket ticket);
    List<Order> allOrders(int ticketId);
    List<Arrival> allArrivals(int ticketId);
    Train findTrainByTicketId(int ticketId);
    Map<String,Double> averagePriceTrain(int ticketId);
    Map<String,Integer> averageCountTrain(int ticketId);
    List<Ticket> sortTicket(List<Ticket>lstTick,String sortName,String sortBy);
    List<Ticket> searchTicket(List<Ticket>lstTick,String searchTownNameFirst, String searchTownNameSecond,
                              Date searchDateFirst, Date searchDateSecond);
    List<Ticket> filterTicket(List<Ticket>lstTick,List<String>lstCondition);
    List<Ticket> allTicketsByName(String name);
    boolean saveTicketCompany(Ticket ticket,String name);
    boolean updateTicketCompany(Ticket ticket,String name);
    List<Ticket> allTicketsByValidate();
}
