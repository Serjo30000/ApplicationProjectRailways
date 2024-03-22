package ru.moiseenko.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import ru.moiseenko.repository.TerminalRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TerminalService implements ITerminalService{
    @Autowired
    TerminalRepository terminalRepository;
    public Terminal findTerminalById(int terminalId){
        Optional<Terminal> terminalFromDb = terminalRepository.findById(terminalId);
        return terminalFromDb.orElse(new Terminal());
    }

    public List<Terminal> allTerminals() {
        return (List<Terminal>) terminalRepository.findAll();
    }

    public boolean deleteTerminal(int terminalId) {
        if (terminalRepository.findById(terminalId).isPresent()) {
            terminalRepository.deleteById(terminalId);
            return true;
        }
        return false;
    }
    public boolean saveTerminal(Terminal terminal) {
        if (terminal == null || terminal.getName().equals("")) {
            return false;
        }
        terminalRepository.save(terminal);
        return true;
    }
    @Override
    public boolean updateTerminal(Terminal terminal) {
        if (terminal == null || terminal.getName().equals("")) {
            return true;
        }
        Terminal terminalToUpdate = terminalRepository.findById(terminal.getId()).get();
        terminalToUpdate.setName(terminal.getName());
        terminalToUpdate.setTownId(terminal.getTownId());
        terminalRepository.updateTerminal(terminalToUpdate.getId(),terminalToUpdate.getName(),terminalToUpdate.getTownId());
        return false;
    }
    @Override
    public Town findTownByTerminalId(int terminalId) {
        Optional<Town> townFromDb = terminalRepository.findTownById(terminalId);
        return townFromDb.orElse(new Town());
    }
    @Override
    public List<Arrival> allArrivals(int terminalId){
        return terminalRepository.findArrivalByAll(terminalId);
    }
    @Override
    public List<Terminal> sortTerminal(List<Terminal>lstTer, String sortName, String sortBy){
        List<Terminal> lstTerminal = new ArrayList<>();
        if (sortName.equals("Отсутствует")){
            return lstTer;
        }
        if (sortName.equals("По названию")){
            if (sortBy.equals("По возрастанию")){
                lstTerminal=terminalRepository.findTerminalOrderByNameAsc();
            }
            else{
                lstTerminal=terminalRepository.findTerminalOrderByNameDesc();
            }
        }
        if (sortName.equals("По городу")){
            if (sortBy.equals("По возрастанию")){
                lstTerminal=terminalRepository.findTerminalOrderByNameTownAsc();
            }
            else{
                lstTerminal=terminalRepository.findTerminalOrderByNameTownDesc();
            }
        }
        if (sortName.equals("По стране")){
            if (sortBy.equals("По возрастанию")){
                lstTerminal=terminalRepository.findTerminalOrderByNameCountryAsc();
            }
            else{
                lstTerminal=terminalRepository.findTerminalOrderByNameCountryDesc();
            }
        }
        List<Terminal>lstCopyTerminal=new ArrayList<>();
        for (int i=0;i<lstTerminal.size();i++){
            for (int j=0;j<lstTer.size();j++){
                if(lstTerminal.get(i).getId()==lstTer.get(j).getId()){
                    lstCopyTerminal.add(lstTer.get(j));
                    break;
                }
            }
        }
        return lstCopyTerminal;
    }
    @Override
    public List<Arrival> allArrivalsByName(int terminalId,String name){
        return terminalRepository.findArrivalByName(terminalId,name);
    }
}
