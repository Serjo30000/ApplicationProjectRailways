package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import ru.moiseenko.repository.CompartmentRepository;
import ru.moiseenko.repository.TrainRepository;
import ru.moiseenko.repository.VanRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainService implements ITrainService{
    @Autowired
    TrainRepository trainRepository;
    @Autowired
    VanRepository vanRepository;
    @Autowired
    CompartmentRepository compartmentRepository;
    public Train findTrainById(int trainId){
        Optional<Train> trainFromDb = trainRepository.findById(trainId);
        return trainFromDb.orElse(new Train());
    }
    public List<Train> allTrains() {
        return (List<Train>) trainRepository.findAll();
    }
    public boolean deleteTrain(int trainId) {
        if (trainRepository.findById(trainId).isPresent()) {
            trainRepository.deleteById(trainId);
            return true;
        }
        return false;
    }
    public boolean saveTrain(Train train) {
        if (train == null || train.getNameTrain().equals("") || train.getTypeTrain().equals("")) {
            return false;
        }
        trainRepository.save(train);
        return true;
    }
    @Override
    public boolean saveTrainCompany(Train train,String name) {
        if (train == null || train.getNameTrain().equals("") || train.getTypeTrain().equals("") || !train.getNameTrain().equals(name)) {
            return false;
        }
        trainRepository.save(train);
        return true;
    }
    @Override
    public boolean updateTrain(Train train) {
        if (train == null || train.getTypeTrain().equals("") || train.getNameTrain().equals("")) {
            return true;
        }
        Train trainToUpdate = trainRepository.findById(train.getId()).get();
        trainToUpdate.setNameTrain(train.getNameTrain());
        trainToUpdate.setTypeTrain(train.getTypeTrain());
        trainRepository.updateTrain(trainToUpdate.getId(),trainToUpdate.getNameTrain(),trainToUpdate.getTypeTrain());
        return false;
    }
    @Override
    public List<Van> allVans(int trainId) {
        return trainRepository.findVanByAll(trainId);
    }
    @Override
    public List<Ticket> allTickets(int trainId){
        return trainRepository.findTicketByAll(trainId);
    }
    @Override
    public List<Order> allOrders(int trainId){
        return trainRepository.findOrderByAll(trainId);
    }
    @Override
    public List<Arrival> allArrivals(int trainId){
        return trainRepository.findArrivalByAll(trainId);
    }
    @Override
    public List<Train> allTrainsByName(String name) {
        return trainRepository.findTrainByName(name);
    }
    @Override
    public int countPlaceTrain(int trainId){
        int countPlace=0;
        for (Van van:trainRepository.findVanByAll(trainId)){
            for (Compartment compartment:vanRepository.findCompartmentByAll(van.getId())){
                for (Place place:compartmentRepository.findPlaceByAll(compartment.getId())){
                    if (place.isEngaged()){
                        countPlace++;
                    }
                }
            }
        }
        return countPlace;
    }
    @Override
    public int countPlaceTrainAll(int trainId){
        int countPlace=0;
        for (Van van:trainRepository.findVanByAll(trainId)){
            for (Compartment compartment:vanRepository.findCompartmentByAll(van.getId())){
                for (Place place:compartmentRepository.findPlaceByAll(compartment.getId())){
                    countPlace++;
                }
            }
        }
        return countPlace;
    }
    @Override
    public double averagePricePlace(int trainId){
        double averagePlace=0;
        int countPlace=0;
        for (Van van:trainRepository.findVanByAll(trainId)){
            for (Compartment compartment:vanRepository.findCompartmentByAll(van.getId())){
                for (Place place:compartmentRepository.findPlaceByAll(compartment.getId())){
                    averagePlace+=place.getPrice();
                    countPlace++;
                }
            }
        }
        averagePlace=averagePlace/countPlace;
        return Math.ceil(averagePlace);
    }
    @Override
    public List<Train> sortTrain(List<Train>lstTr,String sortName,String sortBy){
        List<Train> lstTrain = new ArrayList<>();
        if (sortName.equals("Отсутствует")){
            return lstTr;
        }
        if (sortName.equals("По названию")){
            if (sortBy.equals("По возрастанию")){
                lstTrain=trainRepository.findTrainOrderByNameAsc();
            }
            else{
                lstTrain=trainRepository.findTrainOrderByNameDesc();
            }
        }
        if (sortName.equals("По типу")){
            if (sortBy.equals("По возрастанию")){
                lstTrain=trainRepository.findTrainOrderByTypeAsc();
            }
            else{
                lstTrain=trainRepository.findTrainOrderByTypeDesc();
            }
        }
        if (sortName.equals("По кол-ву вагонов")){
            if (sortBy.equals("По возрастанию")){
                lstTrain=trainRepository.findTrainOrderByCountVanAsc();
            }
            else{
                lstTrain=trainRepository.findTrainOrderByCountVanDesc();
            }
        }
        if (sortName.equals("По кол-ву мест")){
            if (sortBy.equals("По возрастанию")){
                lstTrain=trainRepository.findTrainOrderByCountPlaceAsc();
            }
            else{
                lstTrain=trainRepository.findTrainOrderByCountPlaceDesc();
            }
        }
        List<Train>lstCopyTrain=new ArrayList<>();
        for (int i=0;i<lstTrain.size();i++){
            for (int j=0;j<lstTr.size();j++){
                if(lstTrain.get(i).getId()==lstTr.get(j).getId()){
                    lstCopyTrain.add(lstTr.get(j));
                    break;
                }
            }
        }
        return lstCopyTrain;
    }
}
