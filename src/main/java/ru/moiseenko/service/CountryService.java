package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import ru.moiseenko.repository.CountryRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CountryService implements ICountryService{
    @Autowired
    CountryRepository countryRepository;
    public Country findCountryById(int countryId){
        Optional<Country> countryFromDb = countryRepository.findById(countryId);
        return countryFromDb.orElse(new Country());
    }
    public List<Country> allCountries() {
        return (List<Country>) countryRepository.findAll();
    }
    public boolean deleteCountry(int countryId) {
        if (countryRepository.findById(countryId).isPresent()) {
            countryRepository.deleteById(countryId);
            return true;
        }
        return false;
    }
    public boolean saveCountry(Country country) {
        if (country == null || country.getCountryName().equals("")) {
            return false;
        }
        if(countryRepository.findByCountryName(country.getCountryName())!=null){
            return false;
        }
        countryRepository.save(country);
        return true;
    }
    @Override
    public boolean updateCountry(Country country) {
        if (country == null || country.getCountryName().equals("")) {
            return true;
        }
        Country countryToUpdate = countryRepository.findById(country.getId()).get();
        countryToUpdate.setCountryName(country.getCountryName());
        countryRepository.updateCountry(countryToUpdate.getId(),countryToUpdate.getCountryName());
        return false;
    }
    @Override
    public List<Town> allTowns(int countryId) {
        return countryRepository.findTownByAll(countryId);
    }
    @Override
    public Country findCountryByCountryName(String countryName) {
        Country country = countryRepository.findByCountryName(countryName);
        if (country == null) {
            return null;
        }
        return country;
    }
}
