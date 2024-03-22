package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import java.util.List;

@Service
public interface ITownService {
    boolean updateTown(Town town);
    Country findCountryByTownId(int townId);
    List<Terminal> allTerminals(int townId);
}
