package ru.moiseenko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.moiseenko.entity.*;
import ru.moiseenko.service.*;
import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.io.File;
import java.net.URLEncoder;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ClientController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TrainService trainService;
    @Autowired
    private ArrivalService arrivalService;
    @Autowired
    private TerminalService terminalService;
    @Autowired
    private TownService townService;
    @Autowired
    private VanService vanService;
    @Autowired
    private CompartmentService compartmentService;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CardService cardService;
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/mainClient")
    public String ticketList(Model model) {
        List<Ticket>lstT=ticketService.allTicketsByValidate();
        model.addAttribute("allTickets", lstT);
        model.addAttribute("ticketServ", ticketService);
        model.addAttribute("arrivalServ", arrivalService);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        model.addAttribute("trainServ", trainService);
        List<String> lstComp= Arrays.asList("Купе","Плацкарт","Спальный","Сиденья");
        model.addAttribute("lstViewCompartment", lstComp);
        return "mainClient";
    }
    @PostMapping(value = "/mainClient", params = { "sortName", "sortBy" })
    public String ticketSortList(@RequestParam(required = true, defaultValue = "",value="sortName") String sortName,
                                 @RequestParam(required = true, defaultValue = "",value="sortBy") String sortBy,
                                 @RequestParam(required = false, value="lstTick")List<Ticket>lstTick,  Model model) {
        if (lstTick==null){
            lstTick=new ArrayList<>();
        }
        List<Ticket>lstT=ticketService.sortTicket(lstTick,sortName,sortBy);
        model.addAttribute("allTickets",lstT );
        model.addAttribute("ticketServ", ticketService);
        model.addAttribute("arrivalServ", arrivalService);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        model.addAttribute("trainServ", trainService);
        List<String> lstComp= Arrays.asList("Купе","Плацкарт","Спальный","Сиденья");
        model.addAttribute("lstViewCompartment", lstComp);
        return "mainClient";
    }
    @PostMapping(value = "/mainClient", params = { "searchTownNameFirst", "searchTownNameSecond", "searchDateFirst", "searchDateSecond" })
    public String ticketSearchList(@RequestParam(required = true, defaultValue = "",value="searchTownNameFirst") String searchTownNameFirst,
                                   @RequestParam(required = true, defaultValue = "",value="searchTownNameSecond") String searchTownNameSecond,
                                   @RequestParam(required = true, defaultValue = "",value="searchDateFirst") Date searchDateFirst,
                                   @RequestParam(required = true, defaultValue = "",value="searchDateSecond") Date searchDateSecond,
                                   @RequestParam(required = false, value="lstTick")List<Ticket>lstTick, Model model) {
        if (lstTick==null){
            lstTick=new ArrayList<>();
        }
        List<Ticket>lstT=ticketService.searchTicket(lstTick,searchTownNameFirst,searchTownNameSecond,searchDateFirst,searchDateSecond);
        model.addAttribute("allTickets",lstT );
        model.addAttribute("ticketServ", ticketService);
        model.addAttribute("arrivalServ", arrivalService);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        model.addAttribute("trainServ", trainService);
        List<String> lstComp= Arrays.asList("Купе","Плацкарт","Спальный","Сиденья");
        model.addAttribute("lstViewCompartment", lstComp);
        return "mainClient";
    }
    @PostMapping(value = "/mainClient", params = { "filterChoice1", "filterChoice2", "filterChoice3" })
    public String ticketFilterList(@RequestParam(required = true, defaultValue = "0",value="filterChoice1") String filterChoice1,
                                   @RequestParam(required = true, defaultValue = "",value="filterChoice2") String filterChoice2,
                                   @RequestParam(required = true, defaultValue = "",value="filterChoice3") String filterChoice3,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice4") String filterChoice4,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice5") String filterChoice5,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice6") String filterChoice6,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice7") String filterChoice7,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice8") String filterChoice8,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice9") String filterChoice9,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice10") String filterChoice10,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice11") String filterChoice11,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice12") String filterChoice12,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice13") String filterChoice13,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice14") String filterChoice14,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice15") String filterChoice15,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice16") String filterChoice16,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice17") String filterChoice17,
                                   @RequestParam(required = true, defaultValue = "off",value="filterChoice18") String filterChoice18,
                                   @RequestParam(required = false, value="lstTick")List<Ticket>lstTick, Model model) {
        List<String>lstCondition=Arrays.asList(filterChoice1,filterChoice2,filterChoice3,filterChoice4,filterChoice5,filterChoice6,
                filterChoice7,filterChoice8,filterChoice9,filterChoice10,filterChoice11,filterChoice12,filterChoice13,filterChoice14,
                filterChoice15,filterChoice16,filterChoice17,filterChoice18);
        if (lstTick==null){
            lstTick=new ArrayList<>();
        }
        List<Ticket>lstT=ticketService.filterTicket(lstTick,lstCondition);
        model.addAttribute("allTickets",lstT );
        model.addAttribute("ticketServ", ticketService);
        model.addAttribute("arrivalServ", arrivalService);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        model.addAttribute("trainServ", trainService);
        List<String> lstComp= Arrays.asList("Купе","Плацкарт","Спальный","Сиденья");
        model.addAttribute("lstViewCompartment", lstComp);
        return "mainClient";
    }
    @PostMapping(value = "/mainClient")
    public String resetTicketList( Model model) {
        return "redirect:mainClient";
    }
    @GetMapping("/mainClient/scheduleTrain")
    public String trainList(Model model) {
        List<Train>lstT=trainService.allTrains();
        model.addAttribute("allTrains", lstT);
        model.addAttribute("trainServ", trainService);
        return "scheduleTrain";
    }
    @PostMapping(value = "/mainClient/scheduleTrain", params = { "sortName", "sortBy" })
    public String trainSortList(@RequestParam(required = true, defaultValue = "",value="sortName") String sortName,
                                @RequestParam(required = true, defaultValue = "",value="sortBy") String sortBy,
                                @RequestParam(required = false, value="lstTr")List<Train>lstTr,  Model model) {
        if (lstTr==null){
            lstTr=new ArrayList<>();
        }
        List<Train>lstT=trainService.sortTrain(lstTr,sortName,sortBy);
        model.addAttribute("allTrains", lstT);
        model.addAttribute("trainServ", trainService);
        return "scheduleTrain";
    }
    @PostMapping(value = "/mainClient/scheduleTrain")
    public String resetTrainList( Model model) {
        return "redirect:scheduleTrain";
    }
    @GetMapping("/mainClient/scheduleTrain/scheduleArrival")
    public String arrivalList(@RequestParam(required = true ) int trainId,Model model) {
        model.addAttribute("allArrivals", trainService.allArrivals(trainId));
        model.addAttribute("arrivalServ", arrivalService);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        return "scheduleArrival";
    }
    @GetMapping("/mainClient/scheduleTerminal")
    public String terminalList(Model model) {
        List<Terminal>lstT=terminalService.allTerminals();
        model.addAttribute("allTerminals", lstT);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        return "scheduleTerminal";
    }
    @PostMapping(value = "/mainClient/scheduleTerminal", params = { "sortName", "sortBy" })
    public String terminalSortList(@RequestParam(required = true, defaultValue = "",value="sortName") String sortName,
                                   @RequestParam(required = true, defaultValue = "",value="sortBy") String sortBy,
                                   @RequestParam(required = false, value="lstTer")List<Terminal>lstTer,  Model model) {
        if (lstTer==null){
            lstTer=new ArrayList<>();
        }
        List<Terminal>lstT=terminalService.sortTerminal(lstTer,sortName,sortBy);
        model.addAttribute("allTerminals", lstT);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        return "scheduleTerminal";
    }
    @PostMapping(value = "/mainClient/scheduleTerminal")
    public String resetTerminalList( Model model) {
        return "redirect:scheduleTerminal";
    }
    @GetMapping("/mainClient/scheduleTerminal/scheduleTrainArrival")
    public String trainArrivalList(@RequestParam(required = true ) int terminalId,Model model) {
        model.addAttribute("allArrivals", terminalService.allArrivals(terminalId));
        model.addAttribute("arrivalServ", arrivalService);
        return "scheduleTrainArrival";
    }
    @GetMapping("/mainClient/ticketArrival")
    public String ticketArrivalList(@RequestParam(required = true ) int ticketId,Model model) {
        model.addAttribute("allArrivals", ticketService.allArrivals(ticketId));
        model.addAttribute("arrivalServ", arrivalService);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        return "ticketArrival";
    }
    @GetMapping("/mainClient/buyTerminal")
    public String ticketBuyTerminalList(@RequestParam(required = true ) int ticketId,Model model) {
        model.addAttribute("allArrivals", ticketService.allArrivals(ticketId));
        model.addAttribute("arrivalServ", arrivalService);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        model.addAttribute("undoLink", ticketId);
        model.addAttribute("trainId", ticketService.findTrainByTicketId(ticketId).getId());
        model.addAttribute("orderTicket", new Order());
        return "buyTerminal";
    }
    @GetMapping("/mainClient/buyVan")
    public String ticketBuyVanList(@ModelAttribute("orderTicket") @Valid Order orderTicket, Model model) {
        model.addAttribute("allVans", trainService.allVans(orderTicket.getTrainId()));
        model.addAttribute("vanServ", vanService);
        model.addAttribute("undoLink", orderTicket.getTicketId());
        model.addAttribute("trainId", orderTicket.getTrainId());
        model.addAttribute("arrivalId", orderTicket.getArrivalId());
        return "buyVan";
    }
    @GetMapping("/mainClient/buyPlace")
    public String ticketBuyPlaceList(@ModelAttribute("orderTicket") @Valid Order orderTicket, Model model) {
        model.addAttribute("allCompartments", vanService.allCompartments(orderTicket.getVanId()));
        model.addAttribute("compartmentServ", compartmentService);
        model.addAttribute("placeServ", placeService);
        model.addAttribute("undoLink", orderTicket.getTicketId());
        model.addAttribute("trainId", orderTicket.getTrainId());
        model.addAttribute("arrivalId", orderTicket.getArrivalId());
        model.addAttribute("vanId", orderTicket.getVanId());
        model.addAttribute("vanObject", vanService.findVanById(orderTicket.getVanId()));
        return "buyPlace";
    }
    @GetMapping("/mainClient/buyInfo")
    public String ticketBuyInfoList(@ModelAttribute("orderTicket") @Valid Order orderTicket, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        model.addAttribute("undoLink", orderTicket.getTicketId());
        model.addAttribute("trainId", orderTicket.getTrainId());
        model.addAttribute("arrivalId", orderTicket.getArrivalId());
        model.addAttribute("vanId", orderTicket.getVanId());
        model.addAttribute("compartmentId", orderTicket.getCompartmentId());
        model.addAttribute("placeId", orderTicket.getPlaceId());
        model.addAttribute("userId", userService.findUserByName(currentPrincipalName).getId());
        return "buyInfo";
    }
    @GetMapping("/mainClient/buyCreditCard")
    public String ticketBuyCreditCardList(@ModelAttribute("orderTicket") @Valid Order orderTicket, Model model) {
        model.addAttribute("undoLink", orderTicket.getTicketId());
        model.addAttribute("trainId", orderTicket.getTrainId());
        model.addAttribute("arrivalId", orderTicket.getArrivalId());
        model.addAttribute("vanId", orderTicket.getVanId());
        model.addAttribute("compartmentId", orderTicket.getCompartmentId());
        model.addAttribute("placeId", orderTicket.getPlaceId());
        model.addAttribute("userId", orderTicket.getUserId());
        model.addAttribute("sexUser", orderTicket.getSexUser());
        model.addAttribute("birthdayUser", orderTicket.getBirthdayUser());
        model.addAttribute("nameUser", orderTicket.getNameUser());
        model.addAttribute("surnameUser", orderTicket.getSurnameUser());
        model.addAttribute("patronymicUser", orderTicket.getPatronymicUser());
        model.addAttribute("typeTrain", trainService.findTrainById(orderTicket.getTrainId()).getTypeTrain());
        model.addAttribute("nameTrain", trainService.findTrainById(orderTicket.getTrainId()).getNameTrain());
        model.addAttribute("typeVan", vanService.findVanById(orderTicket.getVanId()).getTypeVan());
        model.addAttribute("typeCompartment", compartmentService.findCompartmentById(orderTicket.getCompartmentId())
                .getTypeCompartment());
        model.addAttribute("numberPlace", placeService.findPlaceById(orderTicket.getPlaceId()).getNumberPlace());
        model.addAttribute("price", placeService.findPlaceById(orderTicket.getPlaceId()).getPrice());
        model.addAttribute("dateArrival", arrivalService.findArrivalById(orderTicket.getArrivalId()).getDate());
        model.addAttribute("nameTerminal",arrivalService.findTerminalByArrivalId(arrivalService
                .findArrivalById(orderTicket.getArrivalId()).getId()).getName());
        model.addAttribute("nameTown",terminalService.findTownByTerminalId(arrivalService.findTerminalByArrivalId(arrivalService
                .findArrivalById(orderTicket.getArrivalId()).getId()).getId()).getTownName());
        model.addAttribute("nameCountry",townService.findCountryByTownId(terminalService.findTownByTerminalId(arrivalService
                .findTerminalByArrivalId(arrivalService.findArrivalById(orderTicket.getArrivalId()).getId()).getId()).getId()).getCountryName());
        return "buyCreditCard";
    }
    @PostMapping(value="/mainClient/buyCreditCard",produces = "text/html; charset=utf-8")
    public String ticketBuyCreditCardCreate(@ModelAttribute("orderTicket") @Valid Order orderTicket,
                                            @RequestParam(required = true, defaultValue = "" ) String passwordCard,
                                            @RequestParam(required = true, defaultValue = "" ) Date dateCard,
                                            BindingResult bindingResult, Model model)throws Throwable {
        model.addAttribute("undoLink", orderTicket.getTicketId());
        model.addAttribute("trainId", orderTicket.getTrainId());
        model.addAttribute("arrivalId", orderTicket.getArrivalId());
        model.addAttribute("vanId", orderTicket.getVanId());
        model.addAttribute("compartmentId", orderTicket.getCompartmentId());
        model.addAttribute("placeId", orderTicket.getPlaceId());
        model.addAttribute("userId", orderTicket.getUserId());
        model.addAttribute("sexUser", orderTicket.getSexUser());
        model.addAttribute("birthdayUser", orderTicket.getBirthdayUser());
        model.addAttribute("nameUser", orderTicket.getNameUser());
        model.addAttribute("surnameUser", orderTicket.getSurnameUser());
        model.addAttribute("patronymicUser", orderTicket.getPatronymicUser());
        model.addAttribute("typeTrain", trainService.findTrainById(orderTicket.getTrainId()).getTypeTrain());
        model.addAttribute("nameTrain", trainService.findTrainById(orderTicket.getTrainId()).getNameTrain());
        model.addAttribute("typeVan", vanService.findVanById(orderTicket.getVanId()).getTypeVan());
        model.addAttribute("typeCompartment", compartmentService.findCompartmentById(orderTicket.getCompartmentId())
                .getTypeCompartment());
        model.addAttribute("numberPlace", placeService.findPlaceById(orderTicket.getPlaceId()).getNumberPlace());
        model.addAttribute("price", placeService.findPlaceById(orderTicket.getPlaceId()).getPrice());
        model.addAttribute("dateArrival", arrivalService.findArrivalById(orderTicket.getArrivalId()).getDate());
        model.addAttribute("nameTerminal",arrivalService.findTerminalByArrivalId(arrivalService
                .findArrivalById(orderTicket.getArrivalId()).getId()).getName());
        model.addAttribute("nameTown",terminalService.findTownByTerminalId(arrivalService
                .findTerminalByArrivalId(arrivalService.findArrivalById(orderTicket.getArrivalId()).getId()).getId()).getTownName());
        model.addAttribute("nameCountry",townService.findCountryByTownId(terminalService
                .findTownByTerminalId(arrivalService.findTerminalByArrivalId(arrivalService
                        .findArrivalById(orderTicket.getArrivalId()).getId()).getId()).getId()).getCountryName());
        Card card=cardService.findCardByNameCard(orderTicket.getCreditCard());
        Place place = placeService.findPlaceById(orderTicket.getPlaceId());
        if (bindingResult.hasErrors() || !place.isEngaged() ||
                !(card.getPriceCard()>placeService.findPlaceById(orderTicket.getPlaceId()).getPrice())) {
            return "redirect:/mainClient/buyCreditCard?error=true&nameUser="+URLEncoder.encode(orderTicket.getNameUser(), "UTF-8")+
                    "&surnameUser="+URLEncoder.encode(orderTicket.getSurnameUser(), "UTF-8")+"&patronymicUser="+
                    URLEncoder.encode(orderTicket.getPatronymicUser(), "UTF-8")+"&sexUser="+URLEncoder.encode(orderTicket.getSexUser(), "UTF-8")+
                    "&birthdayUser="+orderTicket.getBirthdayUser()+"&ticketId="+orderTicket.getTicketId()+"&trainId="+orderTicket.getTrainId()+
                    "&arrivalId="+orderTicket.getArrivalId()+"&vanId="+orderTicket.getVanId()+"&compartmentId="+orderTicket.getCompartmentId()+
                    "&placeId="+orderTicket.getPlaceId()+"&userId="+orderTicket.getUserId();
        }
        if(card.getNameCard().equals(orderTicket.getCreditCard())&&card.getPasswordCard().equals(passwordCard)&&
                card.getDateCard().equals(dateCard)&&(new Date(System.currentTimeMillis())).before(dateCard)&&
                Period.between(orderTicket.getBirthdayUser().toLocalDate(), LocalDate.now()).getYears()>=12){
            if (!orderService.saveOrder(orderTicket)){
                return "redirect:/mainClient/buyCreditCard?error=true&nameUser="+URLEncoder.encode(orderTicket.getNameUser(), "UTF-8")+
                        "&surnameUser="+URLEncoder.encode(orderTicket.getSurnameUser(), "UTF-8")+"&patronymicUser="+
                        URLEncoder.encode(orderTicket.getPatronymicUser(), "UTF-8")+"&sexUser="+
                        URLEncoder.encode(orderTicket.getSexUser(), "UTF-8")+"&birthdayUser="+
                        orderTicket.getBirthdayUser()+"&ticketId="+orderTicket.getTicketId()+
                        "&trainId="+orderTicket.getTrainId()+"&arrivalId="+orderTicket.getArrivalId()+
                        "&vanId="+orderTicket.getVanId()+"&compartmentId="+orderTicket.getCompartmentId()+
                        "&placeId="+orderTicket.getPlaceId()+"&userId="+orderTicket.getUserId();
            }
            Order order=orderService.allOrders().get(orderService.allOrders().size()-1);
            String ean13Code = orderService.generateEAN13Code(order.getId());
            ImageIO.write(orderService.generateEAN13BarcodeImage(ean13Code),"png",
                    new File("C:\\Users\\Serge\\IdeaProjects\\ApplicationProjectRailways\\src\\main\\resources\\static\\img\\barcodes\\"+
                            ean13Code));
            ImageIO.write(orderService.generateEAN13BarcodeImage(ean13Code),"png",
                    new File("C:\\Users\\Serge\\IdeaProjects\\ApplicationProjectRailways\\uploads\\img\\barcodes\\"+
                            ean13Code));
            order.setBarcode(ean13Code);
            orderService.updateOrder(order);
            card.setPriceCard(card.getPriceCard()-placeService.findPlaceById(orderTicket.getPlaceId()).getPrice());
            cardService.updateCard(card);
            place.setEngaged(false);
            placeService.updatePlace(place);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userService.findUserByName(trainService.findTrainById(order.getTrainId()).getNameTrain());
            if (user!=null){
                Notification notificationClient = notificationService.createNotification(currentPrincipalName,user.getId(),
                        "Вы успешно приобрели билет №"+order.getTicketId()+" по цене:"+place.getPrice()+
                                ", информацию о нём можете прочитать в 'Мои билеты'.");
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Билет №"+order.getTicketId()+" был куплен.");
                notificationService.saveNotification(notificationClient);
                notificationService.saveNotification(notificationCompany);
            }
            return "redirect:/mainClient/myTicket";
        }
        return "redirect:/mainClient/buyCreditCard?error=true&nameUser="+URLEncoder.encode(orderTicket.getNameUser(), "UTF-8")+
                "&surnameUser="+URLEncoder.encode(orderTicket.getSurnameUser(), "UTF-8")+"&patronymicUser="+
                URLEncoder.encode(orderTicket.getPatronymicUser(), "UTF-8")+"&sexUser="+
                URLEncoder.encode(orderTicket.getSexUser(), "UTF-8")+"&birthdayUser="+
                orderTicket.getBirthdayUser()+"&ticketId="+orderTicket.getTicketId()+"&trainId="+
                orderTicket.getTrainId()+"&arrivalId="+orderTicket.getArrivalId()+"&vanId="+
                orderTicket.getVanId()+"&compartmentId="+orderTicket.getCompartmentId()+"&placeId="+
                orderTicket.getPlaceId()+"&userId="+orderTicket.getUserId();
    }
    @GetMapping("/mainClient/myTicket")
    public String myTicketList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<Order>lstT=orderService.allOrdersByUser(userService.findUserByName(currentPrincipalName).getId());
        model.addAttribute("allOrders", lstT);
        model.addAttribute("ticketServ", ticketService);
        model.addAttribute("trainServ", trainService);
        model.addAttribute("compartmentServ", compartmentService);
        model.addAttribute("placeServ", placeService);
        model.addAttribute("arrivalServ", arrivalService);
        return "myTicket";
    }
    @PostMapping(value = "/mainClient/myTicket", params = { "sortName", "sortBy" })
    public String myTicketSortList(@RequestParam(required = true, defaultValue = "",value="sortName") String sortName,
                                   @RequestParam(required = true, defaultValue = "",value="sortBy") String sortBy,
                                   @RequestParam(required = false, value="lstOrd")List<Order>lstOrd,  Model model) {
        if (lstOrd==null){
            lstOrd=new ArrayList<>();
        }
        List<Order>lstT=orderService.sortOrder(lstOrd,sortName,sortBy);
        model.addAttribute("allOrders",lstT );
        model.addAttribute("ticketServ", ticketService);
        model.addAttribute("trainServ", trainService);
        model.addAttribute("compartmentServ", compartmentService);
        model.addAttribute("placeServ", placeService);
        model.addAttribute("arrivalServ", arrivalService);
        return "myTicket";
    }
    @PostMapping(value = "/mainClient/myTicket")
    public String resetMyTicketList( Model model) {
        return "redirect:/mainClient/myTicket";
    }
    @GetMapping("/mainClient/myTicket/viewMyTicket")
    public String getViewMyTicket(@RequestParam(required = true ) int orderId,Model model) {
        Order order = orderService.findOrderById(orderId);
        model.addAttribute("orderForm", order);
        model.addAttribute("ticketServ", ticketService);
        model.addAttribute("trainServ", trainService);
        model.addAttribute("compartmentServ", compartmentService);
        model.addAttribute("placeServ", placeService);
        model.addAttribute("arrivalServ", arrivalService);
        model.addAttribute("linkBarcode", "/img/barcodes/"+orderService.findOrderById(orderId).getBarcode());
        model.addAttribute("birthdayUser", order.getBirthdayUser());
        model.addAttribute("nameUser", order.getNameUser());
        model.addAttribute("surnameUser", order.getSurnameUser());
        model.addAttribute("patronymicUser", order.getPatronymicUser());
        model.addAttribute("dateArrival", arrivalService.findArrivalById(order.getArrivalId()).getDate());
        model.addAttribute("nameTerminal",arrivalService.findTerminalByArrivalId(arrivalService.findArrivalById(order
                .getArrivalId()).getId()).getName());
        model.addAttribute("nameTown",terminalService.findTownByTerminalId(arrivalService
                .findTerminalByArrivalId(arrivalService.findArrivalById(order.getArrivalId()).getId()).getId()).getTownName());
        model.addAttribute("nameCountry",townService.findCountryByTownId(terminalService
                .findTownByTerminalId(arrivalService.findTerminalByArrivalId(arrivalService
                        .findArrivalById(order.getArrivalId()).getId()).getId()).getId()).getCountryName());
        model.addAttribute("typeTrain", trainService.findTrainById(order.getTrainId()).getTypeTrain());
        model.addAttribute("nameTrain", trainService.findTrainById(order.getTrainId()).getNameTrain());
        model.addAttribute("typeVan", vanService.findVanById(order.getVanId()).getTypeVan());
        model.addAttribute("typeCompartment", compartmentService.findCompartmentById(order.getCompartmentId())
                .getTypeCompartment());
        model.addAttribute("numberPlace", placeService.findPlaceById(order.getPlaceId()).getNumberPlace());
        model.addAttribute("price", placeService.findPlaceById(order.getPlaceId()).getPrice());
        return "viewMyTicket";
    }
    @GetMapping("/mainClient/myTicket/deleteMyTicket")
    public String getDeleteMyTicket(@RequestParam(required = true ) int orderId,Model model) {
        model.addAttribute("orderId", orderId);
        return "deleteMyTicket";
    }
    @PostMapping("/mainClient/myTicket/deleteMyTicket")
    public String deleteMyTicket(@ModelAttribute("orderTicket") @Valid Order orderTicket,
                                 @RequestParam(required = true, defaultValue = "" ) String creditCard,
                                 @RequestParam(required = true, defaultValue = "" ) String passwordCard,
                                 @RequestParam(required = true, defaultValue = "" ) Date dateCard,
                                 BindingResult bindingResult,Model model) {
        model.addAttribute("orderId", orderTicket.getId());
        Order order=orderService.findOrderById(orderTicket.getId());
        Card card=cardService.findCardByNameCard(creditCard);
        Place place = placeService.findPlaceById(order.getPlaceId());
        if (bindingResult.hasErrors()||card==null) {
            return "redirect:/mainClient/myTicket/deleteMyTicket?error=true&orderId="+orderTicket.getId();
        }
        if(card.getDateCard().equals(dateCard)&&(new Date(System.currentTimeMillis())).before(dateCard)
                &&card.getPasswordCard().equals(passwordCard)&&card.getNameCard().equals(order.getCreditCard())){
            card.setPriceCard(card.getPriceCard()+place.getPrice());
            place.setEngaged(true);
            cardService.updateCard(card);
            placeService.updatePlace(place);
            File file = new File("C:\\Users\\Serge\\IdeaProjects\\ApplicationProjectRailways\\src\\main\\resources\\static\\img\\barcodes\\"+
                    order.getBarcode());
            file.delete();
            file.exists();
            File fileUpload = new File("C:\\Users\\Serge\\IdeaProjects\\ApplicationProjectRailways\\uploads\\img\\barcodes\\"+
                    order.getBarcode());
            fileUpload.delete();
            fileUpload.exists();
            orderService.deleteOrder(order.getId());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userService.findUserByName(trainService.findTrainById(order.getTrainId()).getNameTrain());
            if (user!=null){
                Notification notificationClient = notificationService.createNotification(currentPrincipalName,user.getId(),
                        "Вы успешно вернули билет № "+order.getTicketId()+", ваш возврат составил: "+place.getPrice());
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Билет №"+order.getTicketId()+" был возвращён.");
                notificationService.saveNotification(notificationClient);
                notificationService.saveNotification(notificationCompany);
            }
            return "redirect:/mainClient/myTicket";
        }
        return "redirect:/mainClient/myTicket/deleteMyTicket?error=true&orderId="+orderTicket.getId();
    }
}
