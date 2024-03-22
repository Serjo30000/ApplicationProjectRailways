package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import ru.moiseenko.repository.TownRepository;
import java.util.List;
import java.util.Optional;

@Service
public class TownService implements ITownService{
    @Autowired
    TownRepository townRepository;
    public Town findTownById(int townId){
        Optional<Town> townFromDb = townRepository.findById(townId);
        return townFromDb.orElse(new Town());
    }
    public List<Town> allTowns() {
        return (List<Town>) townRepository.findAll();
    }
    public boolean deleteTown(int townId) {
        if (townRepository.findById(townId).isPresent()) {
            townRepository.deleteById(townId);
            return true;
        }
        return false;
    }
    public boolean saveTown(Town town) {
        if (town == null || town.getTownName().equals("")) {
            return false;
        }
        townRepository.save(town);
        return true;
    }
    @Override
    public boolean updateTown(Town town) {
        if (town == null || town.getTownName().equals("")) {
            return true;
        }
        Town townToUpdate = townRepository.findById(town.getId()).get();
        townToUpdate.setTownName(town.getTownName());
        townToUpdate.setCountryId(town.getCountryId());
        townRepository.updateTown(townToUpdate.getId(),townToUpdate.getTownName(),townToUpdate.getCountryId());
        return false;
    }

    @Override
    public Country findCountryByTownId(int townId) {
        Optional<Country> countryFromDb = townRepository.findCountryById(townId);
        return countryFromDb.orElse(new Country());
    }

    @Override
    public List<Terminal> allTerminals(int townId) {
        return townRepository.findTerminalByAll(townId);
    }
}
