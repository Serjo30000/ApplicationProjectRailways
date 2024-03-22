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
import javax.validation.Valid;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CompanyController {
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
    private NotificationService notificationService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CardService cardService;
    @GetMapping("/companyPanel/companyTerminal")
    public String terminalCompanyList(Model model) {
        List<Terminal> lstT=terminalService.allTerminals();
        model.addAttribute("allTerminals", lstT);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        return "companyTerminal";
    }
    @PostMapping(value = "/companyPanel/companyTerminal", params = { "sortName", "sortBy" })
    public String terminalSortCompanyList(@RequestParam(required = true, defaultValue = "",value="sortName") String sortName,
                                          @RequestParam(required = true, defaultValue = "",value="sortBy") String sortBy,
                                          @RequestParam(required = false, value="lstTer")List<Terminal>lstTer, Model model) {
        if (lstTer==null){
            lstTer=new ArrayList<>();
        }
        List<Terminal>lstT=terminalService.sortTerminal(lstTer,sortName,sortBy);
        model.addAttribute("allTerminals", lstT);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        return "companyTerminal";
    }
    @PostMapping(value = "/companyPanel/companyTerminal")
    public String resetTerminalCompanyList( Model model) {
        return "redirect:companyTerminal";
    }
    @GetMapping("/companyPanel/companyTerminal/viewCompanyTerminal")
    public String getViewTerminalCompany(@RequestParam(required = true ) int terminalId,Model model) {
        model.addAttribute("terminalForm", terminalService.findTerminalById(terminalId));
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("townServ", townService);
        return "viewCompanyTerminal";
    }
    @GetMapping("/companyPanel/companyTerminal/companyArrival")
    public String arrivalCompanyList(@RequestParam(required = true ) int terminalId,Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        model.addAttribute("allArrivals", terminalService.allArrivalsByName(terminalId,currentPrincipalName));
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("undoListCreate", terminalId);
        return "companyArrival";
    }
    @PostMapping(value="/companyPanel/companyTerminal/companyArrival", params = { "arrivalId" })
    public String  deleteArrivalCompany(@RequestParam(required = true ) int arrivalId,
                             @RequestParam(required = true, defaultValue = "" ) String action,
                             Model model) {
        int linkId=arrivalService.findTerminalByArrivalId(arrivalId).getId();
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userService.findUserByName(arrivalService.findTrainByArrivalId(arrivalId).getNameTrain());
            if (user!=null){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Ваш маршрут №"+
                                arrivalId+" был удалён.");
                notificationService.saveNotification(notificationCompany);
                List<Order>lstOrd=arrivalService.allOrders(arrivalId);
                for (Order order:lstOrd){
                    Notification notificationClient = notificationService.createNotification(userService
                            .findUserById(order.getUserId()).getUsername(),userService.findUserByName(currentPrincipalName).getId(),
                            "Маршрут №"+arrivalId+" был удалён, ваш возврат составил: "+
                                    placeService.findPlaceById(order.getPlaceId()).getPrice());
                    Place place = placeService.findPlaceById(order.getPlaceId());
                    place.setEngaged(true);
                    Card card = cardService.findCardByNameCard(order.getCreditCard());
                    if (card!=null){
                        card.setPriceCard(card.getPriceCard()+placeService.findPlaceById(order.getPlaceId()).getPrice());
                        cardService.updateCard(card);
                    }
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
                    notificationService.saveNotification(notificationClient);
                }
            }
            arrivalService.deleteArrival(arrivalId);
        }
        return "redirect:/companyPanel/companyTerminal/companyArrival?terminalId="+linkId;
    }
    @GetMapping("/companyPanel/companyTerminal/companyArrival/viewCompanyArrival")
    public String getViewCompanyArrival(@RequestParam(required = true ) int arrivalId,Model model) {
        model.addAttribute("arrivalForm", arrivalService.findArrivalById(arrivalId));
        return "viewCompanyArrival";
    }
    @GetMapping("/companyPanel/companyTerminal/companyArrival/editCompanyArrival")
    public String getEditCompanyArrival(@RequestParam(required = true ) int arrivalId,Model model) {
        model.addAttribute("arrivalForm",arrivalService.findArrivalById(arrivalId));
        return "editCompanyArrival";
    }
    @PostMapping("/companyPanel/companyTerminal/companyArrival/editCompanyArrival")
    public String editCompanyArrival(@ModelAttribute("arrivalForm") @Valid Arrival arrivalForm, BindingResult bindingResult, Model model) {
        int linkId;
        if (bindingResult.hasErrors()) {
            return "editCompanyArrival";
        }
        if (terminalService.findTerminalById(arrivalForm.getTerminalId()).getId()==0){
            return "redirect:/companyPanel/companyTerminal/companyArrival/editCompanyArrival?error=true&arrivalId="+arrivalForm.getId();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userService.findUserByName(arrivalService.findTrainByArrivalId(arrivalForm.getId()).getNameTrain());
        if (user!=null){
            Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                    userService.findUserByName(currentPrincipalName).getId(),"Ваш маршрут №"+
                            arrivalForm.getId()+" был изменён.");
            notificationService.saveNotification(notificationCompany);
            List<Order>lstOrd = arrivalService.allOrders(arrivalForm.getId());
            for (Order order:lstOrd){
                Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Маршрут №"+arrivalForm.getId()+
                                " был изменён, вы можете вернуть деньги в 'Мои билеты', если вам не подходит.");
                notificationService.saveNotification(notificationClient);
            }
        }
        if (!arrivalService.updateArrivalCompany(arrivalForm,currentPrincipalName)){
            return "redirect:/companyPanel/companyTerminal/companyArrival/editCompanyArrival?error=true&arrivalId="+arrivalForm.getId();
        }
        linkId=arrivalService.findTerminalByArrivalId(arrivalForm.getId()).getId();
        return "redirect:/companyPanel/companyTerminal/companyArrival?terminalId="+linkId;
    }
    @GetMapping("/companyPanel/companyTerminal/companyArrival/createCompanyArrival")
    public String getCreateCompanyArrival(@RequestParam(required = true )int undoListCreate,Model model) {
        model.addAttribute("undoListCreate", undoListCreate);
        model.addAttribute("arrivalForm", new Arrival());
        return "createCompanyArrival";
    }
    @PostMapping("/companyPanel/companyTerminal/companyArrival/createCompanyArrival")
    public String addCompanyArrival(@ModelAttribute("arrivalForm") @Valid Arrival arrivalForm, BindingResult bindingResult, Model model) {
        int linkId;
        if (bindingResult.hasErrors()) {
            return "createCompanyArrival";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (ticketService.findTicketById(arrivalForm.getTicketId()).getId()==0){
            return "redirect:/companyPanel/companyTerminal/companyArrival/createCompanyArrival?error=true&undoListCreate="+
                    arrivalForm.getTerminalId();
        }
        if (trainService.findTrainById(ticketService.findTicketById(arrivalForm.getTicketId()).getTrainId()).getId()==0 ||
                !trainService.findTrainById(ticketService.findTicketById(arrivalForm.getTicketId()).getTrainId())
                        .getNameTrain().equals(currentPrincipalName)){
            return "redirect:/companyPanel/companyTerminal/companyArrival/createCompanyArrival?error=true&undoListCreate="+
                    arrivalForm.getTerminalId();
        }
        if (!arrivalService.saveArrivalCompany(arrivalForm,currentPrincipalName)){
            return "redirect:/companyPanel/companyTerminal/companyArrival/createCompanyArrival?error=true&undoListCreate="+
                    arrivalForm.getTerminalId();
        }
        Notification notificationCompany = notificationService.createNotification(currentPrincipalName,
                userService.findUserByName(currentPrincipalName).getId(),"Вы создали новый маршрут.");
        notificationService.saveNotification(notificationCompany);
        linkId=arrivalService.findTerminalByArrivalId(arrivalForm.getId()).getId();
        return "redirect:/companyPanel/companyTerminal/companyArrival?terminalId="+linkId;
    }
    @PostMapping(value = "/companyPanel/companyTerminal/companyArrival", params = { "sortName", "sortBy" })
    public String arrivalCompanySortList(@RequestParam(required = true, defaultValue = "",value="sortName") String sortName,
                                         @RequestParam(required = true, defaultValue = "",value="sortBy") String sortBy,
                                         @RequestParam(required = true ) int terminalId,
                                         @RequestParam(required = false, value="lstArv")List<Arrival>lstArv,  Model model) {
        if (lstArv==null){
            lstArv=new ArrayList<>();
        }
        List<Arrival>lstT=arrivalService.sortArrival(lstArv,sortName,sortBy);
        model.addAttribute("allArrivals", lstT);
        model.addAttribute("terminalServ", terminalService);
        model.addAttribute("undoListCreate", terminalId);
        return "companyArrival";
    }
    @PostMapping(value = "/companyPanel/companyTerminal/companyArrival")
    public String resetArrivalCompanyList( Model model,@RequestParam(required = true ) int terminalId) {

        return "redirect:/companyPanel/companyTerminal/companyArrival?terminalId="+terminalId;
    }
    @GetMapping("/companyPanel/companyTicket")
    public String ticketCompanyList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<Ticket> lstT=ticketService.allTicketsByName(currentPrincipalName);
        model.addAttribute("allTickets", lstT);
        model.addAttribute("trainServ", trainService);
        model.addAttribute("ticketServ", ticketService);
        model.addAttribute("todayData",new Date(System.currentTimeMillis()));
        return "companyTicket";
    }
    @PostMapping(value="/companyPanel/companyTicket", params = { "ticketId" })
    public String  deleteTicketCompany(@RequestParam(required = true ) int ticketId,
                                        @RequestParam(required = true, defaultValue = "" ) String action,
                                        Model model) {
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userService.findUserByName(ticketService.findTrainByTicketId(ticketId).getNameTrain());
            if (user!=null){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Ваш билет №"+ticketId+" был удалён.");
                notificationService.saveNotification(notificationCompany);
                List<Order>lstOrd=ticketService.allOrders(ticketId);
                if (ticketService.allArrivals(ticketId)!=null && ticketService.allArrivals(ticketId).size()>0 &&
                        ticketService.allArrivals(ticketId).get(ticketService.allArrivals(ticketId).size()-1).getDate()
                                .before(new Date(System.currentTimeMillis()))){
                    for (Order order:lstOrd){
                        Notification notificationClient = notificationService.createNotification(userService
                                .findUserById(order.getUserId()).getUsername(),userService.findUserByName(currentPrincipalName).getId(),
                                "Билет №"+ticketId+" был удалён, как просрочился.");
                        Place place = placeService.findPlaceById(order.getPlaceId());
                        place.setEngaged(true);
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
                        notificationService.saveNotification(notificationClient);
                    }
                }
                else{
                    for (Order order:lstOrd){
                        Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                                userService.findUserByName(currentPrincipalName).getId(),"Билет №"+ticketId+" был удалён, ваш возврат составил: "+
                                        placeService.findPlaceById(order.getPlaceId()).getPrice());
                        Place place = placeService.findPlaceById(order.getPlaceId());
                        place.setEngaged(true);
                        Card card = cardService.findCardByNameCard(order.getCreditCard());
                        if (card!=null){
                            card.setPriceCard(card.getPriceCard()+placeService.findPlaceById(order.getPlaceId()).getPrice());
                            cardService.updateCard(card);
                        }
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
                        notificationService.saveNotification(notificationClient);
                    }
                }

            }
            ticketService.deleteTicket(ticketId);
        }
        return "redirect:/companyPanel/companyTicket";
    }
    @GetMapping("/companyPanel/companyTicket/viewCompanyTicket")
    public String getViewTicketCompany(@RequestParam(required = true ) int ticketId,Model model) {
        model.addAttribute("ticketForm", ticketService.findTicketById(ticketId));
        model.addAttribute("trainServ", trainService);
        model.addAttribute("ticketServ", ticketService);
        model.addAttribute("todayData",new Date(System.currentTimeMillis()));
        return "viewCompanyTicket";
    }
    @PostMapping(value = "/companyPanel/companyTicket", params = { "sortName", "sortBy" })
    public String ticketSortCompanyList(@RequestParam(required = true, defaultValue = "",value="sortName") String sortName,
                                        @RequestParam(required = true, defaultValue = "",value="sortBy") String sortBy,
                                        @RequestParam(required = false, value="lstTick")List<Ticket>lstTick, Model model) {
        if (lstTick==null){
            lstTick=new ArrayList<>();
        }
        List<Ticket>lstT=ticketService.sortTicket(lstTick,sortName,sortBy);
        model.addAttribute("allTickets", lstT);
        model.addAttribute("trainServ", trainService);
        model.addAttribute("ticketServ", ticketService);
        model.addAttribute("todayData",new Date(System.currentTimeMillis()));
        return "companyTicket";
    }
    @PostMapping(value = "/companyPanel/companyTicket")
    public String resetTicketCompanyList( Model model) {
        return "redirect:companyTicket";
    }
    @GetMapping("/companyPanel/companyTicket/editCompanyTicket")
    public String getEditCompanyTicket(@RequestParam(required = true ) int ticketId,Model model) {
        model.addAttribute("ticketForm",ticketService.findTicketById(ticketId));
        return "editCompanyTicket";
    }
    @PostMapping("/companyPanel/companyTicket/editCompanyTicket")
    public String editCompanyTicket(@ModelAttribute("vanForm") @Valid Ticket ticketForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "editCompanyTicket";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if ((trainService.findTrainById(ticketForm.getTrainId()).getId()==0 ||
                !trainService.findTrainById(ticketForm.getTrainId()).getNameTrain().equals(currentPrincipalName) ||
                trainService.allTickets(ticketForm.getTrainId()).size()>=1)&&
                ticketService.findTicketById(ticketForm.getId()).getTrainId()!=ticketForm.getTrainId()){
            return "redirect:/companyPanel/companyTicket/editCompanyTicket?error=true&ticketId="+ticketForm.getId();
        }
        User user = userService.findUserByName(ticketService.findTrainByTicketId(ticketForm.getId()).getNameTrain());
        if (user!=null){
            Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                    userService.findUserByName(currentPrincipalName).getId(),"Ваш билет №"+ticketForm.getId()+" был изменён.");
            notificationService.saveNotification(notificationCompany);
            List<Order>lstOrd=ticketService.allOrders(ticketForm.getId());
            if (ticketService.allArrivals(ticketForm.getId())!=null && ticketService.allArrivals(ticketForm.getId()).size()>0 &&
                    ticketService.allArrivals(ticketForm.getId()).get(ticketService.allArrivals(ticketForm.getId()).size()-1).getDate()
                            .before(new Date(System.currentTimeMillis()))){
                for (Order order:lstOrd){
                    Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                            userService.findUserByName(currentPrincipalName).getId(),"Билет №"+ticketForm.getId()+
                                    " был изменён, так как просрочился.");
                    Place place = placeService.findPlaceById(order.getPlaceId());
                    place.setEngaged(true);
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
                    notificationService.saveNotification(notificationClient);
                }
            }
            else{
                for (Order order:lstOrd){
                    Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                            userService.findUserByName(currentPrincipalName).getId(),"Билет №"+ticketForm.getId()+
                                    " был изменён, ваш возврат составил: "+placeService.findPlaceById(order.getPlaceId()).getPrice());
                    Place place = placeService.findPlaceById(order.getPlaceId());
                    place.setEngaged(true);
                    Card card = cardService.findCardByNameCard(order.getCreditCard());
                    if (card!=null){
                        card.setPriceCard(card.getPriceCard()+placeService.findPlaceById(order.getPlaceId()).getPrice());
                        cardService.updateCard(card);
                    }
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
                    notificationService.saveNotification(notificationClient);
                }
            }
        }
        if (!ticketService.updateTicketCompany(ticketForm,currentPrincipalName)){
            return "redirect:/companyPanel/companyTicket/editCompanyTicket?error=true&ticketId="+ticketForm.getId();
        }
        return "redirect:/companyPanel/companyTicket";
    }
    @GetMapping("/companyPanel/companyTicket/createCompanyTicket")
    public String getCreateCompanyTicket(Model model) {
        model.addAttribute("ticketForm", new Ticket());
        return "createCompanyTicket";
    }
    @PostMapping("/companyPanel/companyTicket/createCompanyTicket")
    public String addCompanyTicket(@ModelAttribute("ticketForm") @Valid Ticket ticketForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "createCompanyTicket";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (trainService.findTrainById(ticketForm.getTrainId()).getId()==0 ||
                !trainService.findTrainById(ticketForm.getTrainId()).getNameTrain().equals(currentPrincipalName) ||
                trainService.allTickets(ticketForm.getTrainId()).size()>=1){
            return "redirect:/companyPanel/companyTicket/createCompanyTicket?error=true";
        }
        if (!ticketService.saveTicketCompany(ticketForm,currentPrincipalName)){
            return "redirect:/companyPanel/companyTicket/createCompanyTicket?error=true";
        }
        Notification notificationCompany = notificationService.createNotification(currentPrincipalName,
                userService.findUserByName(currentPrincipalName).getId(),"Вы создали новый билет.");
        notificationService.saveNotification(notificationCompany);
        return "redirect:/companyPanel/companyTicket";
    }
    @GetMapping("/companyPanel/companyTrain")
    public String trainCompanyList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        List<Train> lstT=trainService.allTrainsByName(currentPrincipalName);
        model.addAttribute("allTrains", lstT);
        model.addAttribute("trainServ", trainService);
        return "companyTrain";
    }
    @PostMapping(value="/companyPanel/companyTrain", params = { "trainId" })
    public String  deleteTrainCompany(@RequestParam(required = true ) int trainId,
                                       @RequestParam(required = true, defaultValue = "" ) String action,
                                       Model model) {
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userService.findUserByName(trainService.findTrainById(trainId).getNameTrain());
            if (user!=null){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Ваш поезд №"+trainId+" был удалён.");
                notificationService.saveNotification(notificationCompany);
                List<Order>lstOrd=trainService.allOrders(trainId);
                for (Order order:lstOrd){
                    Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                            userService.findUserByName(currentPrincipalName).getId(),"Поезд №"+trainId+" был удалён, ваш возврат составил: "+
                                    placeService.findPlaceById(order.getPlaceId()).getPrice());
                    Place place = placeService.findPlaceById(order.getPlaceId());
                    place.setEngaged(true);
                    Card card = cardService.findCardByNameCard(order.getCreditCard());
                    if (card!=null){
                        card.setPriceCard(card.getPriceCard()+placeService.findPlaceById(order.getPlaceId()).getPrice());
                        cardService.updateCard(card);
                    }
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
                    notificationService.saveNotification(notificationClient);
                }
            }
            trainService.deleteTrain(trainId);
        }
        return "redirect:/companyPanel/companyTrain";
    }
    @GetMapping("/companyPanel/companyTrain/viewCompanyTrain")
    public String getViewTrainCompany(@RequestParam(required = true ) int trainId,Model model) {
        model.addAttribute("trainForm", trainService.findTrainById(trainId));
        model.addAttribute("trainServ", trainService);
        return "viewCompanyTrain";
    }
    @GetMapping("/companyPanel/companyTrain/createCompanyTrain")
    public String getCreateCompanyTrain(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Train train=new Train();
        train.setNameTrain(currentPrincipalName);
        model.addAttribute("trainForm", train);
        return "createCompanyTrain";
    }
    @PostMapping("/companyPanel/companyTrain/createCompanyTrain")
    public String addCompanyTrain(@ModelAttribute("trainForm") @Valid Train trainForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "createCompanyTrain";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (!trainService.saveTrainCompany(trainForm,currentPrincipalName)){
            return "createCompanyTrain";
        }
        Notification notificationCompany = notificationService.createNotification(currentPrincipalName,
                userService.findUserByName(currentPrincipalName).getId(),"Вы создали новый поезд.");
        notificationService.saveNotification(notificationCompany);
        return "redirect:/companyPanel/companyTrain";
    }
    @PostMapping(value = "/companyPanel/companyTrain", params = { "sortName", "sortBy" })
    public String trainSortCompanyList(@RequestParam(required = true, defaultValue = "",value="sortName") String sortName,
                                       @RequestParam(required = true, defaultValue = "",value="sortBy") String sortBy,
                                       @RequestParam(required = false, value="lstTr")List<Train>lstTr, Model model) {
        if (lstTr==null){
            lstTr=new ArrayList<>();
        }
        List<Train>lstT=trainService.sortTrain(lstTr,sortName,sortBy);
        model.addAttribute("allTrains", lstT);
        model.addAttribute("trainServ", trainService);
        return "companyTrain";
    }
    @PostMapping(value = "/companyPanel/companyTrain")
    public String resetTrainCompanyList( Model model) {
        return "redirect:companyTrain";
    }
    @GetMapping("/companyPanel/companyVan")
    public String vanCompanyList(@RequestParam(required = true ) int trainId,Model model) {
        model.addAttribute("allVans", trainService.allVans(trainId));
        model.addAttribute("vanServ", vanService);
        model.addAttribute("undoListCreate", trainId);
        return "companyVan";
    }
    @PostMapping(value="/companyPanel/companyVan", params = { "vanId" })
    public String  deleteVanCompany(@RequestParam(required = true ) int vanId,
                                        @RequestParam(required = true, defaultValue = "" ) String action,
                                        Model model) {
        int linkId=vanService.findTrainByVanId(vanId).getId();
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userService.findUserByName(vanService.findTrainByVanId(vanId).getNameTrain());
            if (user!=null){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Ваш вагон №"+vanId+" был удалён.");
                notificationService.saveNotification(notificationCompany);
                List<Order>lstOrd=vanService.allOrders(vanId);
                for (Order order:lstOrd){
                    Notification notificationClient = notificationService.createNotification(
                            userService.findUserById(order.getUserId()).getUsername(),
                            userService.findUserByName(currentPrincipalName).getId(),
                            "Вагон №"+vanId+" был удалён, ваш возврат составил: "+
                                    placeService.findPlaceById(order.getPlaceId()).getPrice());
                    Place place = placeService.findPlaceById(order.getPlaceId());
                    place.setEngaged(true);
                    Card card = cardService.findCardByNameCard(order.getCreditCard());
                    if (card!=null){
                        card.setPriceCard(card.getPriceCard()+placeService.findPlaceById(order.getPlaceId()).getPrice());
                        cardService.updateCard(card);
                    }
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
                    notificationService.saveNotification(notificationClient);
                }
            }
            vanService.deleteVan(vanId);
        }
        return "redirect:/companyPanel/companyVan?trainId="+linkId;
    }
    @GetMapping("/companyPanel/companyVan/viewCompanyVan")
    public String getViewCompanyVan(@RequestParam(required = true ) int vanId,Model model) {
        model.addAttribute("vanForm", vanService.findVanById(vanId));
        model.addAttribute("vanServ", vanService);
        return "viewCompanyVan";
    }
    @PostMapping(value = "/companyPanel/companyVan", params = { "sortName", "sortBy" })
    public String vanCompanySortList(@RequestParam(required = true, defaultValue = "",value="sortName") String sortName,
                                     @RequestParam(required = true, defaultValue = "",value="sortBy") String sortBy,
                                     @RequestParam(required = true ) int trainId,
                                     @RequestParam(required = false, value="lstVan")List<Van>lstVan,  Model model) {
        if (lstVan==null){
            lstVan=new ArrayList<>();
        }
        List<Van>lstT=vanService.sortVan(lstVan,sortName,sortBy);
        model.addAttribute("allVans", lstT);
        model.addAttribute("vanServ", vanService);
        model.addAttribute("undoListCreate", trainId);
        return "companyVan";
    }
    @PostMapping(value = "/companyPanel/companyVan")
    public String resetVanCompanyList( Model model,@RequestParam(required = true ) int trainId) {

        return "redirect:/companyPanel/companyVan?trainId="+trainId;
    }
    @GetMapping("/companyPanel/companyVan/editCompanyVan")
    public String getEditCompanyVan(@RequestParam(required = true ) int vanId,Model model) {
        model.addAttribute("vanForm",vanService.findVanById(vanId));
        return "editCompanyVan";
    }
    @PostMapping("/companyPanel/companyVan/editCompanyVan")
    public String editCompanyVan(@ModelAttribute("vanForm") @Valid Van vanForm, BindingResult bindingResult, Model model) {
        int linkId;
        if (bindingResult.hasErrors()) {
            return "editCompanyVan";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (vanService.findTrainByVanId(vanForm.getId()).getId()==0 ||
                !vanService.findTrainByVanId(vanForm.getId()).getNameTrain().equals(currentPrincipalName)){
            return "redirect:/companyPanel/companyVan/editCompanyVan?error=true&vanId="+vanForm.getId();
        }
        User user = userService.findUserByName(vanService.findTrainByVanId(vanForm.getId()).getNameTrain());
        if (user!=null){
            Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                    userService.findUserByName(currentPrincipalName).getId(),
                    "Ваш вагон №"+vanForm.getId()+" был изменён.");
            notificationService.saveNotification(notificationCompany);
            List<Order>lstOrd=vanService.allOrders(vanForm.getId());
            for (Order order:lstOrd){
                Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Вагон №"+vanForm.getId()+
                                " был изменён, ваш возврат составил: "+placeService.findPlaceById(order.getPlaceId()).getPrice());
                Place place = placeService.findPlaceById(order.getPlaceId());
                place.setEngaged(true);
                Card card = cardService.findCardByNameCard(order.getCreditCard());
                if (card!=null){
                    card.setPriceCard(card.getPriceCard()+placeService.findPlaceById(order.getPlaceId()).getPrice());
                    cardService.updateCard(card);
                }
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
                notificationService.saveNotification(notificationClient);
            }
        }
        if (!vanService.updateVanCompany(vanForm,currentPrincipalName)){
            return "redirect:/companyPanel/companyVan/editCompanyVan?error=true&vanId="+vanForm.getId();
        }
        linkId=vanService.findTrainByVanId(vanForm.getId()).getId();
        return "redirect:/companyPanel/companyVan?trainId="+linkId;
    }
    @GetMapping("/companyPanel/companyVan/createCompanyVan")
    public String getCreateCompanyVan(@RequestParam(required = true )int undoListCreate,Model model) {
        model.addAttribute("typeTrain", trainService.findTrainById(undoListCreate).getTypeTrain());
        model.addAttribute("typeOne", "Классический");
        model.addAttribute("typeTwo", "Скоростной");
        model.addAttribute("typeThree", "Фирменный");
        model.addAttribute("typeFour", "Двухэтажный");
        model.addAttribute("undoListCreate", undoListCreate);
        model.addAttribute("vanForm", new Van());
        return "createCompanyVan";
    }
    @PostMapping("/companyPanel/companyVan/createCompanyVan")
    public String addCompanyVan(@ModelAttribute("vanForm") @Valid Van vanForm,
                                @RequestParam(required = true )int price, BindingResult bindingResult,
                                Model model) {
        int linkId;
        model.addAttribute("typeTrain", trainService.findTrainById(vanForm.getTrainId()).getTypeTrain());
        model.addAttribute("typeOne", "Классический");
        model.addAttribute("typeTwo", "Скоростной");
        model.addAttribute("typeThree", "Фирменный");
        model.addAttribute("typeFour", "Двухэтажный");
        if (bindingResult.hasErrors()) {
            return "createCompanyVan";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        if (!vanService.saveVanCompany(vanForm,currentPrincipalName,price)){
            model.addAttribute("undoListCreate", vanForm.getTrainId());
            model.addAttribute("vanForm", new Van());
            return "createCompanyVan";
        }
        Notification notificationCompany = notificationService.createNotification(currentPrincipalName,
                userService.findUserByName(currentPrincipalName).getId(),"Вы создали новый вагон.");
        notificationService.saveNotification(notificationCompany);
        linkId=vanService.findTrainByVanId(vanForm.getId()).getId();
        return "redirect:/companyPanel/companyVan?trainId="+linkId;
    }
    @GetMapping("/companyPanel/companyCompartment")
    public String compartmentCompanyList(@RequestParam(required = true ) int vanId,Model model) {
        model.addAttribute("allCompartments", vanService.allCompartments(vanId));
        model.addAttribute("undoLink", vanService.findTrainByVanId(vanId).getId());
        model.addAttribute("undoListCreate", vanId);
        model.addAttribute("compartmentServ", compartmentService);
        return "companyCompartment";
    }
    @GetMapping("/companyPanel/companyCompartment/viewCompanyCompartment")
    public String getViewCompanyCompartment(@RequestParam(required = true ) int compartmentId,Model model) {
        model.addAttribute("compartmentForm", compartmentService.findCompartmentById(compartmentId));
        model.addAttribute("compartmentServ", compartmentService);
        return "viewCompanyCompartment";
    }
    @PostMapping(value = "/companyPanel/companyCompartment", params = { "sortName", "sortBy" })
    public String compartmentCompanySortList(@RequestParam(required = true, defaultValue = "",value="sortName") String sortName,
                                             @RequestParam(required = true, defaultValue = "",value="sortBy") String sortBy,
                                             @RequestParam(required = true ) int vanId,
                                             @RequestParam(required = false, value="lstComp")List<Compartment>lstComp,
                                             Model model) {
        if (lstComp==null){
            lstComp=new ArrayList<>();
        }
        List<Compartment>lstT=compartmentService.sortCompartment(lstComp,sortName,sortBy);
        model.addAttribute("allCompartments", lstT);
        model.addAttribute("compartmentServ", compartmentService);
        model.addAttribute("undoListCreate", vanId);
        model.addAttribute("undoLink", vanService.findVanById(vanId).getTrainId());
        return "companyCompartment";
    }
    @PostMapping(value = "/companyPanel/companyCompartment")
    public String resetCompartmentCompanyList( Model model,@RequestParam(required = true ) int vanId) {
        return "redirect:/companyPanel/companyCompartment?vanId="+vanId;
    }
    @GetMapping("/companyPanel/companyPlace")
    public String placeCompanyList(@RequestParam(required = true ) int compartmentId,Model model) {
        model.addAttribute("allPlaces", compartmentService.allPlaces(compartmentId));
        model.addAttribute("undoLink", compartmentService.findVanByCompartmentId(compartmentId).getId());
        model.addAttribute("undoListCreate", compartmentId);
        return "companyPlace";
    }
    @GetMapping("/companyPanel/companyPlace/viewCompanyPlace")
    public String getViewCompanyPlace(@RequestParam(required = true ) int placeId,Model model) {
        model.addAttribute("placeForm", placeService.findPlaceById(placeId));
        return "viewCompanyPlace";
    }
    @PostMapping(value = "/companyPanel/companyPlace", params = { "sortName", "sortBy" })
    public String placeCompanySortList(@RequestParam(required = true, defaultValue = "",value="sortName") String sortName,
                                       @RequestParam(required = true, defaultValue = "",value="sortBy") String sortBy,
                                       @RequestParam(required = true ) int compartmentId,
                                       @RequestParam(required = false, value="lstPl")List<Place>lstPl,  Model model) {
        if (lstPl==null){
            lstPl=new ArrayList<>();
        }
        List<Place>lstT=placeService.sortPlace(lstPl,sortName,sortBy);
        model.addAttribute("allPlaces", lstT);
        model.addAttribute("undoListCreate", compartmentId);
        model.addAttribute("undoLink", compartmentService.findCompartmentById(compartmentId).getVanId());
        return "companyPlace";
    }
    @PostMapping(value = "/companyPanel/companyPlace")
    public String resetPlaceCompanyList( Model model,@RequestParam(required = true ) int compartmentId) {
        return "redirect:/companyPanel/companyPlace?compartmentId="+compartmentId;
    }
}
