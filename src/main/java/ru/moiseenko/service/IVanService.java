package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Compartment;
import ru.moiseenko.entity.Order;
import ru.moiseenko.entity.Train;
import ru.moiseenko.entity.Van;
import java.util.List;

@Service
public interface IVanService {
    boolean updateVan(Van van);
    Train findTrainByVanId(int vanId);
    List<Compartment> allCompartments(int vanId);
    int countPlaceVan(int vanId);
    double averagePricePlaceVan(int vanId);
    List<Van> sortVan(List<Van>lstVan,String sortName,String sortBy);
    boolean updateVanCompany(Van van,String name);
    boolean saveVanCompany(Van van,String name, int price);
    List<Order> allOrders(int vanId);
}
