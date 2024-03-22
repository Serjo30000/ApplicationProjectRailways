package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.*;
import java.util.List;

@Service
public interface ITerminalService {
    boolean updateTerminal(Terminal terminal);
    Town findTownByTerminalId(int terminalId);
    List<Arrival> allArrivals(int terminalId);
    List<Terminal> sortTerminal(List<Terminal>lstTer, String sortName, String sortBy);
    List<Arrival> allArrivalsByName(int terminalId,String name);
}
