package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Compartment;
import ru.moiseenko.entity.Place;
import ru.moiseenko.repository.PlaceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceService implements IPlaceService{
    @Autowired
    PlaceRepository placeRepository;
    public Place findPlaceById(int placeId){
        Optional<Place> placeFromDb = placeRepository.findById(placeId);
        return placeFromDb.orElse(new Place());
    }
    public List<Place> allPlaces() {
        return (List<Place>) placeRepository.findAll();
    }
    public boolean deletePlace(int placeId) {
        if (placeRepository.findById(placeId).isPresent()) {
            placeRepository.deleteById(placeId);
            return true;
        }
        return false;
    }
    public boolean savePlace(Place place) {
        if (place == null) {
            return false;
        }
        placeRepository.save(place);
        return true;
    }
    @Override
    public boolean updatePlace(Place place) {
        if (place == null) {
            return true;
        }
        Place placeToUpdate = placeRepository.findById(place.getId()).get();
        placeToUpdate.setCompartmentId(place.getCompartmentId());
        placeToUpdate.setEngaged(place.isEngaged());
        placeToUpdate.setPrice(place.getPrice());
        placeToUpdate.setNumberPlace(place.getNumberPlace());
        placeRepository.updatePlace(placeToUpdate.getId(),placeToUpdate.getCompartmentId(),placeToUpdate.isEngaged(),
                placeToUpdate.getPrice(),placeToUpdate.getNumberPlace());
        return false;
    }

    @Override
    public Compartment findCompartmentByPlaceId(int placeId) {
        Optional<Compartment> compartmentFromDb = placeRepository.findCompartmentById(placeId);
        return compartmentFromDb.orElse(new Compartment());
    }
    @Override
    public List<Place> sortPlace(List<Place>lstPl, String sortName, String sortBy){
        List<Place> lstPlace = new ArrayList<>();
        if (sortName.equals("Отсутствует")){
            return lstPl;
        }
        if (sortName.equals("По индексу")){
            if (sortBy.equals("По возрастанию")){
                lstPlace=placeRepository.findPlaceOrderByNumberAsc();
            }
            else{
                lstPlace=placeRepository.findPlaceOrderByNumberDesc();
            }
        }
        if (sortName.equals("По цене")){
            if (sortBy.equals("По возрастанию")){
                lstPlace=placeRepository.findPlaceOrderByPriceAsc();
            }
            else{
                lstPlace=placeRepository.findPlaceOrderByPriceDesc();
            }
        }
        if (sortName.equals("По занятости")){
            if (sortBy.equals("По возрастанию")){
                lstPlace=placeRepository.findPlaceOrderByEngagedAsc();
            }
            else{
                lstPlace=placeRepository.findPlaceOrderByEngagedDesc();
            }
        }
        List<Place>lstCopyPlace=new ArrayList<>();
        for (int i=0;i<lstPlace.size();i++){
            for (int j=0;j<lstPl.size();j++){
                if(lstPlace.get(i).getId()==lstPl.get(j).getId()){
                    lstCopyPlace.add(lstPl.get(j));
                    break;
                }
            }
        }
        return lstCopyPlace;
    }
}
