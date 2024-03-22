package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Country;
import ru.moiseenko.entity.Town;
import java.util.List;

@Service
public interface ICountryService {
    boolean updateCountry(Country country);
    List<Town> allTowns(int countryId);
    Country findCountryByCountryName(String countryName);
}
