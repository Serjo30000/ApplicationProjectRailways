package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import ru.moiseenko.repository.CompartmentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompartmentService implements ICompartmentService{
    @Autowired
    CompartmentRepository compartmentRepository;
    public Compartment findCompartmentById(int compartmentId){
        Optional<Compartment> compartmentFromDb = compartmentRepository.findById(compartmentId);
        return compartmentFromDb.orElse(new Compartment());
    }
    public List<Compartment> allCompartments() {
        return (List<Compartment>) compartmentRepository.findAll();
    }
    public boolean deleteCompartment(int compartmentId) {
        if (compartmentRepository.findById(compartmentId).isPresent()) {
            compartmentRepository.deleteById(compartmentId);
            return true;
        }
        return false;
    }
    public boolean saveCompartment(Compartment compartment) {
        if (compartment == null || compartment.getTypeCompartment().equals("")) {
            return false;
        }
        compartmentRepository.save(compartment);
        return true;
    }
    @Override
    public boolean updateCompartment(Compartment compartment) {
        if (compartment == null || compartment.getTypeCompartment().equals("")) {
            return true;
        }
        Compartment compartmentToUpdate = compartmentRepository.findById(compartment.getId()).get();
        compartmentToUpdate.setVanId(compartment.getVanId());
        compartmentToUpdate.setTypeCompartment(compartment.getTypeCompartment());
        compartmentRepository.updateCompartment(compartmentToUpdate.getId(),compartmentToUpdate.getVanId(),
                compartmentToUpdate.getTypeCompartment());
        return false;
    }

    @Override
    public Van findVanByCompartmentId(int compartmentId) {
        Optional<Van> vanFromDb = compartmentRepository.findVanById(compartmentId);
        return vanFromDb.orElse(new Van());
    }

    @Override
    public List<Place> allPlaces(int compartmentId) {
        return compartmentRepository.findPlaceByAll(compartmentId);
    }
    @Override
    public int countPlaceCompartment(int compartmentId){
        int countPlace=0;
        for (Place place:compartmentRepository.findPlaceByAll(compartmentId)){
            countPlace++;
        }
        return countPlace;
    }
    @Override
    public double averagePricePlaceCompartment(int compartmentId){
        double averagePlace=0;
        int countPlace=0;
        for (Place place:compartmentRepository.findPlaceByAll(compartmentId)){
            averagePlace+=place.getPrice();
            countPlace++;
        }
        averagePlace=averagePlace/countPlace;
        return averagePlace;
    }
    @Override
    public List<Compartment> sortCompartment(List<Compartment>lstComp, String sortName, String sortBy){
        List<Compartment> lstCompartment = new ArrayList<>();
        if (sortName.equals("Отсутствует")){
            return lstComp;
        }
        if (sortName.equals("По типу")){
            if (sortBy.equals("По возрастанию")){
                lstCompartment=compartmentRepository.findCompartmentOrderByTypeAsc();
            }
            else{
                lstCompartment=compartmentRepository.findCompartmentOrderByTypeDesc();
            }
        }
        if (sortName.equals("По цене")){
            if (sortBy.equals("По возрастанию")){
                lstCompartment=compartmentRepository.findCompartmentOrderByPriceAsc();
            }
            else{
                lstCompartment=compartmentRepository.findCompartmentOrderByPriceDesc();
            }
        }
        if (sortName.equals("По кол-ву мест")){
            if (sortBy.equals("По возрастанию")){
                lstCompartment=compartmentRepository.findCompartmentOrderByCountAsc();
            }
            else{
                lstCompartment=compartmentRepository.findCompartmentOrderByCountDesc();
            }
        }
        List<Compartment>lstCopyCompartment=new ArrayList<>();
        for (int i=0;i<lstCompartment.size();i++){
            for (int j=0;j<lstComp.size();j++){
                if(lstCompartment.get(i).getId()==lstComp.get(j).getId()){
                    lstCopyCompartment.add(lstComp.get(j));
                    break;
                }
            }
        }
        return lstCopyCompartment;
    }
}
