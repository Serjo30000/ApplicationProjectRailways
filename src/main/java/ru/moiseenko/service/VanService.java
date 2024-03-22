package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import ru.moiseenko.repository.CompartmentRepository;
import ru.moiseenko.repository.PlaceRepository;
import ru.moiseenko.repository.TrainRepository;
import ru.moiseenko.repository.VanRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VanService implements IVanService {
    @Autowired
    VanRepository vanRepository;
    @Autowired
    CompartmentRepository compartmentRepository;
    @Autowired
    TrainRepository trainRepository;
    @Autowired
    PlaceRepository placeRepository;
    public Van findVanById(int vanId){
        Optional<Van> vanFromDb = vanRepository.findById(vanId);
        return vanFromDb.orElse(new Van());
    }
    public List<Van> allVans() {
        return (List<Van>) vanRepository.findAll();
    }
    public boolean deleteVan(int vanId) {
        if (vanRepository.findById(vanId).isPresent()) {
            vanRepository.deleteById(vanId);
            return true;
        }
        return false;
    }
    public boolean saveVan(Van van) {
        if (van == null || van.getTypeVan().equals("")) {
            return false;
        }
        vanRepository.save(van);
        return true;
    }
    @Override
    public boolean saveVanCompany(Van van,String name, int price) {
        if (van == null || van.getTypeVan().equals("")) {
            return false;
        }
        List<Train>lstTr= (List<Train>) trainRepository.findAll();
        for (Train tr:lstTr){
            if(tr.getId()==van.getTrainId() && tr.getNameTrain().equals(name)){
                if(tr.getTypeTrain().equals("Классический") || tr.getTypeTrain().equals("Скоростной")){
                    if(van.getTypeVan().equals("Купейный")){
                        vanRepository.save(van);
                        Van newVan = trainRepository.findVanByAll(tr.getId()).get(trainRepository.findVanByAll(tr.getId()).size()-1);
                        int pl=1;
                        for(int i=0;i<9;i++){
                            Compartment compartment = new Compartment();
                            compartment.setVanId(newVan.getId());
                            compartment.setTypeCompartment("Купе");
                            compartmentRepository.save(compartment);
                            Compartment newCompartment = vanRepository.findCompartmentByAll(newVan.getId())
                                    .get(vanRepository.findCompartmentByAll(newVan.getId()).size()-1);
                            for(int j=0;j<4;j++){
                                Place place = new Place();
                                place.setCompartmentId(newCompartment.getId());
                                place.setEngaged(true);
                                place.setPrice(price);
                                place.setNumberPlace(pl);
                                pl++;
                                placeRepository.save(place);
                            }
                        }
                        return true;
                    }
                    if(van.getTypeVan().equals("Плацкартный")){
                        vanRepository.save(van);
                        Van newVan = trainRepository.findVanByAll(tr.getId()).get(trainRepository.findVanByAll(tr.getId()).size()-1);
                        int pl=1;
                        for(int i=0;i<9;i++){
                            Compartment compartment = new Compartment();
                            compartment.setVanId(newVan.getId());
                            compartment.setTypeCompartment("Плацкарт");
                            compartmentRepository.save(compartment);
                            Compartment newCompartment = vanRepository.findCompartmentByAll(newVan.getId()).get(vanRepository.findCompartmentByAll(newVan.getId()).size()-1);
                            for(int j=0;j<6;j++){
                                Place place = new Place();
                                place.setCompartmentId(newCompartment.getId());
                                place.setEngaged(true);
                                place.setPrice(price);
                                place.setNumberPlace(pl);
                                pl++;
                                placeRepository.save(place);
                            }
                        }
                        return true;
                    }

                }
                if(tr.getTypeTrain().equals("Фирменный") && van.getTypeVan().equals("СВ")){
                    vanRepository.save(van);
                    Van newVan = trainRepository.findVanByAll(tr.getId()).get(trainRepository.findVanByAll(tr.getId()).size()-1);
                    int pl=1;
                    for(int i=0;i<9;i++){
                        Compartment compartment = new Compartment();
                        compartment.setVanId(newVan.getId());
                        compartment.setTypeCompartment("Спальный");
                        compartmentRepository.save(compartment);
                        Compartment newCompartment = vanRepository.findCompartmentByAll(newVan.getId()).get(vanRepository.findCompartmentByAll(newVan.getId()).size()-1);
                        for(int j=0;j<2;j++){
                            Place place = new Place();
                            place.setCompartmentId(newCompartment.getId());
                            place.setEngaged(true);
                            place.setPrice(price);
                            place.setNumberPlace(pl);
                            pl++;
                            placeRepository.save(place);
                        }
                    }
                    return true;
                }
                if(tr.getTypeTrain().equals("Двухэтажный") && van.getTypeVan().equals("Сидячий")){
                    vanRepository.save(van);
                    Van newVan = trainRepository.findVanByAll(tr.getId()).get(trainRepository.findVanByAll(tr.getId()).size()-1);
                    int pl=1;
                    for(int i=0;i<2;i++){
                        Compartment compartment = new Compartment();
                        compartment.setVanId(newVan.getId());
                        compartment.setTypeCompartment("Сиденья");
                        compartmentRepository.save(compartment);
                        Compartment newCompartment = vanRepository.findCompartmentByAll(newVan.getId()).get(vanRepository.findCompartmentByAll(newVan.getId()).size()-1);
                        for(int j=0;j<50;j++){
                            Place place = new Place();
                            place.setCompartmentId(newCompartment.getId());
                            place.setEngaged(true);
                            place.setPrice(price);
                            place.setNumberPlace(pl);
                            pl++;
                            placeRepository.save(place);
                        }
                    }
                    return true;
                }
                return false;
            }
        }
        vanRepository.save(van);
        return true;
    }
    @Override
    public boolean updateVan(Van van) {
        if (van == null || van.getTypeVan().equals("")) {
            return true;
        }
        Van vanToUpdate = vanRepository.findById(van.getId()).get();
        vanToUpdate.setTypeVan(van.getTypeVan());
        vanToUpdate.setTrainId(van.getTrainId());
        vanRepository.updateVan(vanToUpdate.getId(),vanToUpdate.getTrainId(),vanToUpdate.getTypeVan());
        return false;
    }
    @Override
    public boolean updateVanCompany(Van van,String name) {
        if (van == null || van.getTypeVan().equals("")) {
            return true;
        }
        Van vanToUpdate = vanRepository.findById(van.getId()).get();
        List<Train>lstTr= (List<Train>) trainRepository.findAll();
        for (Train tr:lstTr){
            if(tr.getId()==van.getTrainId() && tr.getNameTrain().equals(name)){
                boolean fl=false;
                if(tr.getTypeTrain().equals("Классический") && (van.getTypeVan().equals("Купейный")||van.getTypeVan().equals("Плацкартный"))){
                    fl=true;
                }
                if(tr.getTypeTrain().equals("Скоростной") && (van.getTypeVan().equals("Купейный")||van.getTypeVan().equals("Плацкартный"))){
                    fl=true;
                }
                if(tr.getTypeTrain().equals("Фирменный") && van.getTypeVan().equals("СВ")){
                    fl=true;
                }
                if(tr.getTypeTrain().equals("Двухэтажный") && van.getTypeVan().equals("Сидячий")){
                    fl=true;
                }
                if (fl==true){
                    vanToUpdate.setTypeVan(van.getTypeVan());
                    vanToUpdate.setTrainId(van.getTrainId());
                    vanRepository.updateVan(vanToUpdate.getId(),vanToUpdate.getTrainId(),vanToUpdate.getTypeVan());
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    @Override
    public Train findTrainByVanId(int vanId) {
        Optional<Train> trainFromDb = vanRepository.findTrainById(vanId);
        return trainFromDb.orElse(new Train());
    }

    @Override
    public List<Compartment> allCompartments(int vanId) {
        return vanRepository.findCompartmentByAll(vanId);
    }
    @Override
    public List<Order> allOrders(int vanId) {
        return vanRepository.findOrderByAll(vanId);
    }
    @Override
    public int countPlaceVan(int vanId){
        int countPlace=0;
        for (Compartment compartment:vanRepository.findCompartmentByAll(vanId)){
            for (Place place:compartmentRepository.findPlaceByAll(compartment.getId())){
                countPlace++;
            }
        }return countPlace;
    }
    @Override
    public double averagePricePlaceVan(int vanId){
        double averagePlace=0;
        int countPlace=0;
        for (Compartment compartment:vanRepository.findCompartmentByAll(vanId)){
            for (Place place:compartmentRepository.findPlaceByAll(compartment.getId())){
                averagePlace+=place.getPrice();
                countPlace++;
            }
        }
        averagePlace=averagePlace/countPlace;
        return averagePlace;
    }
    @Override
    public List<Van> sortVan(List<Van>lstVan,String sortName,String sortBy){
        List<Van> lstVn = new ArrayList<>();
        if (sortName.equals("Отсутствует")){
            return lstVan;
        }
        if (sortName.equals("По цене")){
            if (sortBy.equals("По возрастанию")){
                lstVn=vanRepository.findVanOrderByPriceAsc();
            }
            else{
                lstVn=vanRepository.findVanOrderByPriceDesc();
            }
        }
        if (sortName.equals("По кол-ву мест")){
            if (sortBy.equals("По возрастанию")){
                lstVn=vanRepository.findVanOrderByCountPlaceAsc();
            }
            else{
                lstVn=vanRepository.findVanOrderByCountPlaceDesc();
            }
        }
        if (sortName.equals("По типу")){
            if (sortBy.equals("По возрастанию")){
                lstVn=vanRepository.findVanOrderByTypeAsc();
            }
            else{
                lstVn=vanRepository.findVanOrderByTypeDesc();
            }
        }
        List<Van>lstCopyVan=new ArrayList<>();
        for (int i=0;i<lstVn.size();i++){
            for (int j=0;j<lstVan.size();j++){
                if(lstVn.get(i).getId()==lstVan.get(j).getId()){
                    lstCopyVan.add(lstVan.get(j));
                    break;
                }
            }
        }
        return lstCopyVan;
    }
}
