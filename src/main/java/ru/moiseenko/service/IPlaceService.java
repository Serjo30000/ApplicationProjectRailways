package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Compartment;
import ru.moiseenko.entity.Place;
import java.util.List;

@Service
public interface IPlaceService {
    boolean updatePlace(Place place);
    Compartment findCompartmentByPlaceId(int placeId);
    List<Place> sortPlace(List<Place>lstPl, String sortName, String sortBy);
}
