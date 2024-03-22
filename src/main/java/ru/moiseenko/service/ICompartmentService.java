package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Compartment;
import ru.moiseenko.entity.Place;
import ru.moiseenko.entity.Van;
import java.util.List;

@Service
public interface ICompartmentService {
    boolean updateCompartment(Compartment compartment);
    Van findVanByCompartmentId(int compartmentId);
    List<Place> allPlaces(int compartmentId);
    int countPlaceCompartment(int compartmentId);
    double averagePricePlaceCompartment(int compartmentId);
    List<Compartment> sortCompartment(List<Compartment>lstComp, String sortName, String sortBy);
}
