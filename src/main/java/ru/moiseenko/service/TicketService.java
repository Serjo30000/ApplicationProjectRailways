package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import ru.moiseenko.repository.*;
import java.sql.Date;
import java.util.*;

@Service
public class TicketService implements ITicketService{
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    TrainRepository trainRepository;
    @Autowired
    VanRepository vanRepository;
    @Autowired
    CompartmentRepository compartmentRepository;
    @Autowired
    TerminalRepository terminalRepository;
    @Autowired
    ArrivalRepository arrivalRepository;
    public Ticket findTicketById(int ticketId){
        Optional<Ticket> ticketFromDb = ticketRepository.findById(ticketId);
        return ticketFromDb.orElse(new Ticket());
    }

    public List<Ticket> allTickets() {
        return (List<Ticket>) ticketRepository.findAll();
    }

    public boolean deleteTicket(int ticketId) {
        if (ticketRepository.findById(ticketId).isPresent()) {
            ticketRepository.deleteById(ticketId);
            return true;
        }
        return false;
    }
    public boolean saveTicket(Ticket ticket) {
        if (ticket == null) {
            return false;
        }
        ticketRepository.save(ticket);
        return true;
    }
    @Override
    public boolean saveTicketCompany(Ticket ticket,String name) {
        if (ticket == null) {
            return false;
        }
        List<Train>lstTr= (List<Train>) trainRepository.findAll();
        for (Train tr:lstTr){
            if(tr.getId()==ticket.getTrainId() && tr.getNameTrain().equals(name)){
                ticketRepository.save(ticket);
                return true;
            }
        }
        return true;
    }
    @Override
    public boolean updateTicket(Ticket ticket) {
        if (ticket == null) {
            return true;
        }
        Ticket ticketToUpdate = ticketRepository.findById(ticket.getId()).get();
        ticketToUpdate.setTrainId(ticket.getTrainId());
        ticketRepository.updateTicket(ticketToUpdate.getId(),ticketToUpdate.getTrainId());
        return false;
    }
    @Override
    public boolean updateTicketCompany(Ticket ticket,String name) {
        if (ticket == null) {
            return true;
        }
        Ticket ticketToUpdate = ticketRepository.findById(ticket.getId()).get();
        List<Train>lstTr= (List<Train>) trainRepository.findAll();
        for (Train tr:lstTr){
            if(tr.getId()==ticket.getTrainId() && tr.getNameTrain().equals(name)){
                ticketToUpdate.setTrainId(ticket.getTrainId());
                ticketRepository.updateTicket(ticketToUpdate.getId(),ticketToUpdate.getTrainId());
                return true;
            }
        }
        return false;
    }
    @Override
    public List<Order> allOrders(int ticketId) {
        return ticketRepository.findOrderByAll(ticketId);
    }
    @Override
    public List<Arrival> allArrivals(int ticketId) {
        return ticketRepository.findArrivalByAll(ticketId);
    }
    @Override
    public List<Ticket> allTicketsByName(String name) {
        return ticketRepository.findTicketByName(name);
    }
    @Override
    public List<Ticket> allTicketsByValidate() {
        return ticketRepository.findTicketByValidate();
    }
    @Override
    public Train findTrainByTicketId(int ticketId) {
        Optional<Train> trainFromDb = ticketRepository.findTrainById(ticketId);
        return trainFromDb.orElse(new Train());
    }
    @Override
    public Map<String,Double> averagePriceTrain(int ticketId){
        Map<String,Double> mapAveragePrice=new HashMap<>();
        Map<String,Integer> mapAverageCount=new HashMap<>();
        Train train = ticketRepository.findTrainById(ticketId).get();
        for (Van vn:trainRepository.findVanByAll(train.getId())){
            for (Compartment comp:vanRepository.findCompartmentByAll(vn.getId())){
                boolean fl1=true;
                String str1="";
                double place=0.0;
                int count=0;
                if (mapAveragePrice.size()==0){
                    str1=comp.getTypeCompartment();
                }
                for (String key:mapAveragePrice.keySet()){
                    if (key.equals(comp.getTypeCompartment())){
                        fl1=false;
                    }
                    str1=comp.getTypeCompartment();
                }
                for(Place pl: compartmentRepository.findPlaceByAll(comp.getId())){
                    if (pl.isEngaged()==true){
                        place+=pl.getPrice();
                        count++;
                    }
                }
                if(fl1==true){
                    mapAverageCount.put(str1, count);
                    mapAveragePrice.put(str1, place);
                }
                else{
                    mapAverageCount.put(str1,mapAverageCount.get(str1)+count);
                    mapAveragePrice.put(str1,mapAveragePrice.get(str1)+place);
                }
            }
        }
        for (String key:mapAveragePrice.keySet()){
            mapAveragePrice.put(key,Math.ceil(mapAveragePrice.get(key)/mapAverageCount.get(key)));
        }
        return mapAveragePrice;
    }
    @Override
    public Map<String,Integer> averageCountTrain(int ticketId){
        Map<String,Integer> mapAverageCount=new HashMap<>();
        Train train = ticketRepository.findTrainById(ticketId).get();
        for (Van vn:trainRepository.findVanByAll(train.getId())){
            for (Compartment comp:vanRepository.findCompartmentByAll(vn.getId())){
                boolean fl1=true;
                String str1="";
                int count=0;
                if (mapAverageCount.size()==0){
                    str1=comp.getTypeCompartment();
                }
                for (String key:mapAverageCount.keySet()){
                    if (key.equals(comp.getTypeCompartment())){
                        fl1=false;
                    }
                    str1=comp.getTypeCompartment();
                }
                for(Place pl: compartmentRepository.findPlaceByAll(comp.getId())){
                    if(pl.isEngaged()==true){
                        count++;
                    }
                }
                if(fl1==true){
                    mapAverageCount.put(str1, count);
                }
                else{
                    mapAverageCount.put(str1,mapAverageCount.get(str1)+count);
                }
            }
        }
        return mapAverageCount;
    }
    @Override
    public List<Ticket> sortTicket(List<Ticket>lstTick,String sortName,String sortBy){
        List<Ticket> lstTicket = new ArrayList<>();
        if (sortName.equals("Отсутствует")){
            return lstTick;
        }
        if (sortName.equals("По времени отправления")){
            if (sortBy.equals("По возрастанию")){
                lstTicket=ticketRepository.findTicketOrderByMinDateAsc();
            }
            else{
                lstTicket=ticketRepository.findTicketOrderByMinDateDesc();
            }
        }
        if (sortName.equals("По времени прибытия")){
            if (sortBy.equals("По возрастанию")){
                lstTicket=ticketRepository.findTicketOrderByMaxDateAsc();
            }
            else{
                lstTicket=ticketRepository.findTicketOrderByMaxDateDesc();
            }
        }
        if (sortName.equals("По времени в пути")){
            if (sortBy.equals("По возрастанию")){
                lstTicket=ticketRepository.findTicketOrderByDateDiffAsc();
            }
            else{
                lstTicket=ticketRepository.findTicketOrderByDateDiffDesc();
            }
        }
        if (sortName.equals("По стоимости")){
            if (sortBy.equals("По возрастанию")){
                lstTicket=ticketRepository.findTicketOrderByPriceAsc();
            }
            else{
                lstTicket=ticketRepository.findTicketOrderByPriceDesc();
            }
        }
        if (sortName.equals("По количеству мест")){
            if (sortBy.equals("По возрастанию")){
                lstTicket=ticketRepository.findTicketOrderByCountPlaceAllAsc();
            }
            else{
                lstTicket=ticketRepository.findTicketOrderByCountPlaceAllDesc();
            }
        }
        if (sortName.equals("По занятым местам")){
            if (sortBy.equals("По возрастанию")){
                lstTicket=ticketRepository.findTicketOrderByCountPlaceAsc();
            }
            else{
                lstTicket=ticketRepository.findTicketOrderByCountPlaceDesc();
            }
        }
        List<Ticket>lstCopyTicket=new ArrayList<>();
        for (int i=0;i<lstTicket.size();i++){
            for (int j=0;j<lstTick.size();j++){
                if(lstTicket.get(i).getId()==lstTick.get(j).getId()){
                    lstCopyTicket.add(lstTick.get(j));
                    break;
                }
            }
        }
        return lstCopyTicket;
    }
    @Override
    public List<Ticket> searchTicket(List<Ticket>lstTick,String searchTownNameFirst, String searchTownNameSecond,
                                     Date searchDateFirst, Date searchDateSecond){
        List<Ticket>lstT=new ArrayList<>();
        for (Ticket ticket : lstTick){
            boolean f1=false;
            boolean f2=false;
            for (int j=0;j<ticketRepository.findArrivalByAll(ticket.getId()).size();j++){
                if (searchDateFirst.before(ticketRepository.findArrivalByAll(ticket.getId()).get(j).getDate())
                        && searchDateSecond.after(ticketRepository.findArrivalByAll(ticket.getId()).get(j).getDate())
                        && terminalRepository.findTownById(arrivalRepository
                        .findTerminalById(ticketRepository.findArrivalByAll(ticket.getId()).get(j).getId())
                        .get().getId()).get().getTownName().equals(searchTownNameFirst)){
                    f1=true;
                }
                if (searchDateFirst.before(ticketRepository.findArrivalByAll(ticket.getId()).get(j).getDate())
                        && searchDateSecond.after(ticketRepository.findArrivalByAll(ticket.getId()).get(j).getDate())
                        && terminalRepository.findTownById(arrivalRepository.findTerminalById(ticketRepository
                        .findArrivalByAll(ticket.getId()).get(j).getId()).get().getId()).get().getTownName()
                        .equals(searchTownNameSecond)){
                    f2=true;
                }
            }
            if (f1==true && f2==true){
                lstT.add(ticket);
            }
        }
        return lstT;
    }
    @Override
    public List<Ticket> filterTicket(List<Ticket>lstTick,List<String>lstCondition){
        List<Ticket>lstT=new ArrayList<>();
        for (Ticket ticket: lstTick){
            boolean f1=true;
            if (ticketRepository.findFilterByPrice(ticket.getId(), Integer.parseInt(lstCondition.get(0)))==null){
                f1=false;
            }
            if (ticketRepository.findFilterByMinDate(ticket.getId(), lstCondition.get(1))==null){
                f1=false;
            }
            if (ticketRepository.findFilterByMaxDate(ticket.getId(), lstCondition.get(2))==null){
                f1=false;
            }
            if (lstCondition.get(3).equals("off") && f1==true){
                boolean f2=false;
                if (lstCondition.get(4).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"Арктика")==null){
                    f1=false;
                }
                else if(lstCondition.get(4).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"Арктика")!=null){
                    f2=true;
                }
                if (lstCondition.get(5).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"Волга")==null){
                    f1=false;
                }
                else if(lstCondition.get(5).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"Волга")!=null){
                    f2=true;
                }
                if (lstCondition.get(6).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"ГРАНД")==null){
                    f1=false;
                }
                else if(lstCondition.get(6).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"ГРАНД")!=null){
                    f2=true;
                }
                if (lstCondition.get(7).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"Красная Стрела")==null){
                    f1=false;
                }
                else if(lstCondition.get(7).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"Красная Стрела")!=null){
                    f2=true;
                }
                if (lstCondition.get(8).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"ЛАСТОЧКА")==null){
                    f1=false;
                }
                else if(lstCondition.get(8).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"ЛАСТОЧКА")!=null){
                    f2=true;
                }
                if (lstCondition.get(9).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"МЕГАПОЛИС")==null){
                    f1=false;
                }
                else if(lstCondition.get(9).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"МЕГАПОЛИС")!=null){
                    f2=true;
                }
                if (lstCondition.get(10).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"САПСАН")==null){
                    f1=false;
                }
                else if(lstCondition.get(10).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"САПСАН")!=null){
                    f2=true;
                }
                if (lstCondition.get(11).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"Бетанкур")==null){
                    f1=false;
                }
                else if(lstCondition.get(11).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"Бетанкур")!=null){
                    f2=true;
                }
                if (lstCondition.get(12).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"Экспресс")==null){
                    f1=false;
                }
                else if(lstCondition.get(12).equals("on") && ticketRepository.findFilterByNameTrain(ticket.getId(),"Экспресс")!=null){
                    f2=true;
                }
                if (f2==true){
                    f1=true;
                }
            }
            if (lstCondition.get(13).equals("off") && f1==true){
                boolean f2=false;
                if (lstCondition.get(14).equals("on") && ticketRepository.findFilterByTypeTrain(ticket.getId(),"Классический")==null){
                    f1=false;
                }
                else if(lstCondition.get(14).equals("on") && ticketRepository.findFilterByTypeTrain(ticket.getId(),"Классический")!=null){
                    f2=true;
                }
                if (lstCondition.get(15).equals("on") && ticketRepository.findFilterByTypeTrain(ticket.getId(),"Скоростной")==null){
                    f1=false;
                }
                else if(lstCondition.get(15).equals("on") && ticketRepository.findFilterByTypeTrain(ticket.getId(),"Скоростной")!=null){
                    f2=true;
                }
                if (lstCondition.get(16).equals("on") && ticketRepository.findFilterByTypeTrain(ticket.getId(),"Фирменный")==null){
                    f1=false;
                }
                else if(lstCondition.get(16).equals("on") && ticketRepository.findFilterByTypeTrain(ticket.getId(),"Фирменный")!=null){
                    f2=true;
                }
                if (lstCondition.get(17).equals("on") && ticketRepository.findFilterByTypeTrain(ticket.getId(),"Двухэтажный")==null){
                    f1=false;
                }
                else if(lstCondition.get(17).equals("on") && ticketRepository.findFilterByTypeTrain(ticket.getId(),"Двухэтажный")!=null){
                    f2=true;
                }
                if (f2==true){
                    f1=true;
                }
            }
            if(f1==true){
                lstT.add(ticket);
            }
        }
        return lstT;
    }
}