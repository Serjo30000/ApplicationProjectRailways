package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import ru.moiseenko.repository.ArrivalRepository;
import ru.moiseenko.repository.TicketRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArrivalService implements IArrivalService{
    @Autowired
    ArrivalRepository arrivalRepository;
    @Autowired
    TicketRepository ticketRepository;
    public Arrival findArrivalById(int arrivalId){
        Optional<Arrival> arrivalFromDb = arrivalRepository.findById(arrivalId);
        return arrivalFromDb.orElse(new Arrival());
    }
    public List<Arrival> allArrivals() {
        return (List<Arrival>) arrivalRepository.findAll();
    }
    public boolean deleteArrival(int arrivalId) {
        if (arrivalRepository.findById(arrivalId).isPresent()) {
            arrivalRepository.deleteById(arrivalId);
            return true;
        }
        return false;
    }
    public boolean saveArrival(Arrival arrival) {
        if (arrival == null || arrival.getDate()==null) {
            return false;
        }
        arrivalRepository.save(arrival);
        return true;
    }
    @Override
    public boolean saveArrivalCompany(Arrival arrival,String name) {
        if (arrival == null || arrival.getDate()==null) {
            return false;
        }
        List<Ticket>lstTick=ticketRepository.findTicketByName(name);
        for (Ticket ticket:lstTick){
            if(ticket.getId()==arrival.getTicketId()){
                arrivalRepository.save(arrival);
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean updateArrival(Arrival arrival) {
        if (arrival == null || arrival.getDate()==null) {
            return true;
        }
        Arrival arrivalToUpdate = arrivalRepository.findById(arrival.getId()).get();
        arrivalToUpdate.setTerminalId(arrival.getTerminalId());
        arrivalToUpdate.setTicketId(arrival.getTicketId());
        arrivalToUpdate.setDate(arrival.getDate());
        arrivalRepository.updateArrival(arrivalToUpdate.getId(),arrivalToUpdate.getTerminalId(),arrivalToUpdate.getTicketId()
                ,arrivalToUpdate.getDate());
        return false;
    }
    @Override
    public boolean updateArrivalCompany(Arrival arrival,String name) {
        if (arrival == null || arrival.getDate()==null) {
            return true;
        }
        Arrival arrivalToUpdate = arrivalRepository.findById(arrival.getId()).get();
        List<Ticket>lstTick=ticketRepository.findTicketByName(name);
        for (Ticket ticket:lstTick){
            if(ticket.getId()==arrival.getTicketId()){
                arrivalToUpdate.setTerminalId(arrival.getTerminalId());
                arrivalToUpdate.setTicketId(arrival.getTicketId());
                arrivalToUpdate.setDate(arrival.getDate());
                arrivalRepository.updateArrival(arrivalToUpdate.getId(),arrivalToUpdate.getTerminalId(),
                        arrivalToUpdate.getTicketId(),arrivalToUpdate.getDate());
                return true;
            }
        }
        return false;
    }
    @Override
    public Terminal findTerminalByArrivalId(int arrivalId) {
        Optional<Terminal> terminalFromDb = arrivalRepository.findTerminalById(arrivalId);
        return terminalFromDb.orElse(new Terminal());
    }
    @Override
    public Train findTrainByArrivalId(int arrivalId) {
        Optional<Train> trainFromDb = arrivalRepository.findTrainById(arrivalId);
        return trainFromDb.orElse(new Train());
    }
    @Override
    public Ticket findTicketByArrivalId(int arrivalId) {
        Optional<Ticket> ticketFromDb = arrivalRepository.findTicketById(arrivalId);
        return ticketFromDb.orElse(new Ticket());
    }
    @Override
    public List<Arrival> sortArrival(List<Arrival>lstArv, String sortName, String sortBy){
        List<Arrival> lstArrival = new ArrayList<>();
        if (sortName.equals("Отсутствует")){
            return lstArv;
        }
        if (sortName.equals("По дате")){
            if (sortBy.equals("По возрастанию")){
                lstArrival=arrivalRepository.findArrivalOrderByDateAsc();
            }
            else{
                lstArrival=arrivalRepository.findArrivalOrderByDateDesc();
            }
        }
        List<Arrival>lstCopyArrival=new ArrayList<>();
        for (int i=0;i<lstArrival.size();i++){
            for (int j=0;j<lstArv.size();j++){
                if(lstArrival.get(i).getId()==lstArv.get(j).getId()){
                    lstCopyArrival.add(lstArv.get(j));
                    break;
                }
            }
        }
        return lstCopyArrival;
    }
    @Override
    public List<Order> allOrders(int arrivalId) {
        return arrivalRepository.findOrderByAll(arrivalId);
    }
}
