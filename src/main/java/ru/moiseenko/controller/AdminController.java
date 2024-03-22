package ru.moiseenko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.moiseenko.entity.*;
import ru.moiseenko.service.*;
import javax.validation.Valid;
import java.io.File;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private TrainService trainService;
    @Autowired
    private VanService vanService;
    @Autowired
    private CompartmentService compartmentService;
    @Autowired
    private PlaceService placeService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private TerminalService terminalService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CountryService countryService;
    @Autowired
    private TownService townService;
    @Autowired
    private ArrivalService arrivalService;
    @Autowired
    private CardService cardService;
    @GetMapping("/adminPanel/adminUser")
    public String userList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        model.addAttribute("allUsers", userService.allUsersNotMe(currentPrincipalName));
        return "adminUser";
    }

    @PostMapping("/adminPanel/adminUser")
    public String  deleteUser(@RequestParam(required = true ) int userId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            User user = userService.findUserById(userId);
            List<Notification>lstNot= notificationService.allNotificationNames(user.getUsername());;
            for(Notification notification:lstNot){
                notificationService.deleteNotification(notification.getId());
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                    userService.findUserByName(currentPrincipalName).getId(),"Вы удалили пользователя.");
            notificationService.saveNotification(notificationAdmin);
            if (user.getRoles().iterator().next().getName().equals("ROLE_ADMIN")){

            }
            if (user.getRoles().iterator().next().getName().equals("ROLE_CLIENT")){
                List<Order>lstOrd=orderService.allOrdersByUser(userId);
                for(Order order:lstOrd){
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
                }
            }
            if (user.getRoles().iterator().next().getName().equals("ROLE_COMPANY")){
                List<Order>lstOrd=orderService.allOrders();
                List<Train>lstTr=trainService.allTrainsByName(user.getUsername());
                for(Order order:lstOrd){
                    if(trainService.findTrainById(order.getTrainId()).getNameTrain().equals(user.getUsername())){
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
                    }
                }
                for (Train train:lstTr){
                    trainService.deleteTrain(train.getId());
                }

            }
            userService.deleteUser(userId);
        }
        return "redirect:/adminPanel/adminUser";
    }

    @GetMapping("/adminPanel/adminUser/editAdminUser")
    public String getEditUser(@RequestParam(required = true ) int userId,Model model) {
        model.addAttribute("userForm", userService.findUserById(userId));
        return "editAdminUser";
    }
    @PostMapping("/adminPanel/adminUser/editAdminUser")
    public String editUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "editAdminUser";
        }

        if (!userService.updateUser(userForm)){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,userForm.getId(),
                    "Вы изменили роль пользователю на "+userForm.getRoles().iterator().next().getName()+".");
            Notification notificationUser = notificationService.createNotification(userForm.getUsername(),
                    userService.findUserByName(currentPrincipalName).getId(),
                    "Ваша роль изменилась на "+userForm.getRoles().iterator().next().getName()+".");
            notificationService.saveNotification(notificationAdmin);
            notificationService.saveNotification(notificationUser);
            return "redirect:/adminPanel/adminUser";
        }
        return "redirect:/adminPanel/adminUser";
    }
    @GetMapping("/adminPanel/adminUser/viewAdminUser")
    public String getViewUser(@RequestParam(required = true ) int userId,Model model) {
        model.addAttribute("userForm", userService.findUserById(userId));
        return "viewAdminUser";
    }

    @GetMapping("/adminPanel/adminTrain")
    public String trainList(Model model) {
        model.addAttribute("allTrains", trainService.allTrains());
        return "adminTrain";
    }
    @PostMapping("/adminPanel/adminTrain")
    public String  deleteTrain(@RequestParam(required = true ) int trainId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                    userService.findUserByName(currentPrincipalName).getId(),"Вы удалили поезд.");
            notificationService.saveNotification(notificationAdmin);
            User user = userService.findUserByName(trainService.findTrainById(trainId).getNameTrain());
            if (user!=null){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Ваш поезд №"+trainId+" был удалён.");
                notificationService.saveNotification(notificationCompany);
                List<Order> lstOrd=trainService.allOrders(trainId);
                for (Order order:lstOrd){
                    Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                            userService.findUserByName(currentPrincipalName).getId(),
                            "Поезд №"+trainId+" был удалён, ваш возврат составил: "+
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
        return "redirect:/adminPanel/adminTrain";
    }
    @GetMapping("/adminPanel/adminTrain/viewAdminTrain")
    public String getViewTrain(@RequestParam(required = true ) int trainId,Model model) {
        model.addAttribute("trainForm", trainService.findTrainById(trainId));
        return "viewAdminTrain";
    }
    @GetMapping("/adminPanel/adminVan")
    public String vanList(@RequestParam(required = true ) int trainId,Model model) {
        model.addAttribute("allVans", trainService.allVans(trainId));
        model.addAttribute("undoListCreate", trainId);
        return "adminVan";
    }
    @PostMapping("/adminPanel/adminVan")
    public String  deleteVan(@RequestParam(required = true ) int vanId,
                               @RequestParam(required = true, defaultValue = "" ) String action,
                               Model model) {
        int linkId=vanService.findTrainByVanId(vanId).getId();
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                    userService.findUserByName(currentPrincipalName).getId(),"Вы удалили вагон.");
            notificationService.saveNotification(notificationAdmin);
            User user = userService.findUserByName(vanService.findTrainByVanId(vanId).getNameTrain());
            if (user!=null){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Ваш вагон №"+vanId+" был удалён.");
                notificationService.saveNotification(notificationCompany);
                List<Order>lstOrd=vanService.allOrders(vanId);
                for (Order order:lstOrd){
                    Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                            userService.findUserByName(currentPrincipalName).getId(),"Вагон №"+vanId+" был удалён, ваш возврат составил: "+
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
        return "redirect:/adminPanel/adminVan?trainId="+linkId;
    }
    @GetMapping("/adminPanel/adminVan/viewAdminVan")
    public String getViewVan(@RequestParam(required = true ) int vanId,Model model) {
        model.addAttribute("vanForm", vanService.findVanById(vanId));
        return "viewAdminVan";
    }
    @GetMapping("/adminPanel/adminCompartment")
    public String compartmentList(@RequestParam(required = true ) int vanId,Model model) {
        model.addAttribute("allCompartments", vanService.allCompartments(vanId));
        model.addAttribute("undoLink", vanService.findTrainByVanId(vanId).getId());
        model.addAttribute("undoListCreate", vanId);
        return "adminCompartment";
    }
    @GetMapping("/adminPanel/adminCompartment/viewAdminCompartment")
    public String getViewCompartment(@RequestParam(required = true ) int compartmentId,Model model) {
        model.addAttribute("compartmentForm", compartmentService.findCompartmentById(compartmentId));
        return "viewAdminCompartment";
    }
    @GetMapping("/adminPanel/adminPlace")
    public String placeList(@RequestParam(required = true ) int compartmentId,Model model) {
        model.addAttribute("allPlaces", compartmentService.allPlaces(compartmentId));
        model.addAttribute("undoLink", compartmentService.findVanByCompartmentId(compartmentId).getId());
        model.addAttribute("undoListCreate", compartmentId);
        return "adminPlace";
    }
    @GetMapping("/adminPanel/adminPlace/viewAdminPlace")
    public String getViewPlace(@RequestParam(required = true ) int placeId,Model model) {
        model.addAttribute("placeForm", placeService.findPlaceById(placeId));
        return "viewAdminPlace";
    }
    @GetMapping("/adminPanel/adminNotification")
    public String notificationList(@RequestParam(required = true ) int userId,Model model) {
        model.addAttribute("allNotifications", userService.allNotifications(userId));
        model.addAttribute("undoListCreate", userId);
        return "adminNotification";
    }
    @PostMapping("/adminPanel/adminNotification")
    public String  deleteNotification(@RequestParam(required = true ) int notificationId,
                               @RequestParam(required = true, defaultValue = "" ) String action,
                               Model model) {
        int linkId=notificationService.findUserByNotificationId(notificationId).getId();
        if (action.equals("delete")){
            notificationService.deleteNotification(notificationId);
        }
        return "redirect:/adminPanel/adminNotification?userId="+linkId;
    }
    @GetMapping("/adminPanel/adminNotification/viewAdminNotification")
    public String getViewNotification(@RequestParam(required = true ) int notificationId,Model model) {
        model.addAttribute("notificationForm", notificationService.findNotificationById(notificationId));
        return "viewAdminNotification";
    }
    @GetMapping("/adminPanel/adminNotification/createAdminNotification")
    public String getCreateNotification(@RequestParam(required = true )int undoListCreate,Model model) {
        model.addAttribute("undoListCreate", undoListCreate);
        model.addAttribute("notificationForm", new Notification());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        model.addAttribute("sender", userService.findUserByName(currentPrincipalName).getId());
        model.addAttribute("userName", userService.findUserById(undoListCreate).getUsername());
        return "createAdminNotification";
    }
    @PostMapping("/adminPanel/adminNotification/createAdminNotification")
    public String addNotification(@ModelAttribute("notificationForm") @Valid Notification notificationForm,
                                  BindingResult bindingResult, Model model) {
        int linkId;
        if (bindingResult.hasErrors()) {
            return "createAdminNotification";
        }

        if (!notificationService.saveNotification(notificationForm)){
            return "createAdminNotification";
        }
        linkId=notificationService.findUserByNotificationId(notificationForm.getId()).getId();
        return "redirect:/adminPanel/adminNotification?userId="+linkId;
    }
    @GetMapping("/adminPanel/adminTicket")
    public String ticketList(Model model) {
        model.addAttribute("allTickets", ticketService.allTickets());
        return "adminTicket";
    }
    @PostMapping("/adminPanel/adminTicket")
    public String  deleteTicket(@RequestParam(required = true ) int ticketId,
                               @RequestParam(required = true, defaultValue = "" ) String action,
                               Model model) {
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                    userService.findUserByName(currentPrincipalName).getId(),"Вы удалили билет.");
            notificationService.saveNotification(notificationAdmin);
            User user = userService.findUserByName(ticketService.findTrainByTicketId(ticketId).getNameTrain());
            if (user!=null){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Ваш билет №"+ticketId+" был удалён.");
                notificationService.saveNotification(notificationCompany);
                List<Order>lstOrd=ticketService.allOrders(ticketId);
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
            ticketService.deleteTicket(ticketId);
        }
        return "redirect:/adminPanel/adminTicket";
    }
    @GetMapping("/adminPanel/adminTicket/viewAdminTicket")
    public String getViewTicket(@RequestParam(required = true ) int ticketId,Model model) {
        model.addAttribute("ticketForm", ticketService.findTicketById(ticketId));
        return "viewAdminTicket";
    }
    @GetMapping("/adminPanel/adminOrder")
    public String orderList(@RequestParam(required = true ) int ticketId,Model model) {
        model.addAttribute("allOrders", ticketService.allOrders(ticketId));
        return "adminOrder";
    }
    @PostMapping("/adminPanel/adminOrder")
    public String  deleteOrder(@RequestParam(required = true ) int orderId,
                             @RequestParam(required = true, defaultValue = "" ) String action,
                             Model model) {
        int linkId=orderService.findTicketByOrderId(orderId).getId();
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                    userService.findUserByName(currentPrincipalName).getId(),"Вы удалили заказ.");
            notificationService.saveNotification(notificationAdmin);
            User user = userService.findUserByName(trainService.findTrainById(orderService.findTicketByOrderId(orderId).getTrainId()).getNameTrain());
            if (user!=null){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Ваш заказ №"+orderId+" был удалён.");
                notificationService.saveNotification(notificationCompany);
                Order order=orderService.findOrderById(orderId);
                Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Заказ №"+orderId+" был удалён, ваш возврат составил: "+
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
            orderService.deleteOrder(orderId);
        }
        return "redirect:/adminPanel/adminOrder?ticketId="+linkId;
    }
    @GetMapping("/adminPanel/adminOrder/viewAdminOrder")
    public String getViewOrder(@RequestParam(required = true ) int orderId,Model model) {
        model.addAttribute("orderForm", orderService.findOrderById(orderId));
        return "viewAdminOrder";
    }
    @GetMapping("/adminPanel/adminCountry")
    public String countryList(Model model) {
        model.addAttribute("allCountries", countryService.allCountries());
        return "adminCountry";
    }
    @PostMapping("/adminPanel/adminCountry")
    public String  deleteCountry(@RequestParam(required = true ) int countryId,
                                @RequestParam(required = true, defaultValue = "" ) String action,
                                Model model) {
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                    userService.findUserByName(currentPrincipalName).getId(),"Вы удалили страну.");
            notificationService.saveNotification(notificationAdmin);
            List<User>lstUs=userService.allUsers();
            for (User user:lstUs){
                if (user.getRegNumber()!=0){
                    Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                            userService.findUserByName(currentPrincipalName).getId(),"Страна №"+countryId+" была удалена.");
                    notificationService.saveNotification(notificationCompany);
                    List<Order>lstOrd = orderService.allOrders();
                    for (Order order:lstOrd){
                        if (townService.findCountryByTownId(terminalService.findTownByTerminalId(arrivalService
                                .findTerminalByArrivalId(order.getArrivalId()).getId()).getId()).getId()==countryId &&
                                trainService.findTrainById(order.getTrainId()).getNameTrain().equals(user.getUsername())){
                            Notification notificationClient = notificationService.createNotification(userService
                                    .findUserById(order.getUserId()).getUsername(),userService.findUserByName(currentPrincipalName).getId(),
                                    "Страна №"+countryId+" была удалена, ваш возврат составил: "+
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
            }
            countryService.deleteCountry(countryId);
        }
        return "redirect:/adminPanel/adminCountry";
    }
    @GetMapping("/adminPanel/adminCountry/viewAdminCountry")
    public String getViewCountry(@RequestParam(required = true ) int countryId,Model model) {
        model.addAttribute("countryForm", countryService.findCountryById(countryId));
        return "viewAdminCountry";
    }
    @GetMapping("/adminPanel/adminCountry/editAdminCountry")
    public String getEditCountry(@RequestParam(required = true ) int countryId,Model model) {
        model.addAttribute("countryForm",countryService.findCountryById(countryId));
        return "editAdminCountry";
    }
    @PostMapping("/adminPanel/adminCountry/editAdminCountry")
    public String editCountry(@ModelAttribute("countryForm") @Valid Country countryForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "editAdminCountry";
        }
        if (countryService.findCountryByCountryName(countryForm.getCountryName())!=null &&
                !countryService.findCountryById(countryForm.getId()).getCountryName().equals(countryForm.getCountryName())){
            return "redirect:/adminPanel/adminCountry/editAdminCountry?error=true&countryId="+countryForm.getId();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                userService.findUserByName(currentPrincipalName).getId(),"Вы изменили страну.");
        notificationService.saveNotification(notificationAdmin);
        List<User>lstUs=userService.allUsers();
        for (User user:lstUs){
            if (user.getRegNumber()!=0){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Страна №"+countryForm.getId()+" была изменена.");
                notificationService.saveNotification(notificationCompany);
                List<Order>lstOrd = orderService.allOrders();
                for (Order order:lstOrd){
                    if (townService.findCountryByTownId(terminalService.findTownByTerminalId(arrivalService
                            .findTerminalByArrivalId(order.getArrivalId()).getId()).getId()).getId()==countryForm.getId() &&
                            trainService.findTrainById(order.getTrainId()).getNameTrain().equals(user.getUsername())){
                        Notification notificationClient = notificationService.createNotification(userService
                                .findUserById(order.getUserId()).getUsername(),userService.findUserByName(currentPrincipalName).getId(),
                                "Страна №"+countryForm.getId()+
                                        " была изменена, вы можете вернуть деньги в 'Мои билеты', если вам не подходит.");
                        notificationService.saveNotification(notificationClient);
                    }
                }
            }
        }
        if (!countryService.updateCountry(countryForm)){
            return "redirect:/adminPanel/adminCountry";
        }

        return "redirect:/adminPanel/adminCountry";
    }
    @GetMapping("/adminPanel/adminCountry/createAdminCountry")
    public String getCreateCountry(Model model) {
        model.addAttribute("countryForm", new Country());
        return "createAdminCountry";
    }
    @PostMapping("/adminPanel/adminCountry/createAdminCountry")
    public String addCountry(@ModelAttribute("countryForm") @Valid Country countryForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "createAdminCountry";
        }

        if (!countryService.saveCountry(countryForm)){
            return "redirect:/adminPanel/adminCountry/createAdminCountry?error=true";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Notification notificationCompany = notificationService.createNotification(currentPrincipalName,
                userService.findUserByName(currentPrincipalName).getId(),"Вы создали новую страну.");
        notificationService.saveNotification(notificationCompany);
        return "redirect:/adminPanel/adminCountry";
    }
    @GetMapping("/adminPanel/adminTown")
    public String townList(@RequestParam(required = true ) int countryId,Model model) {
        model.addAttribute("allTowns", countryService.allTowns(countryId));
        model.addAttribute("undoListCreate", countryId);
        return "adminTown";
    }
    @PostMapping("/adminPanel/adminTown")
    public String  deleteTown(@RequestParam(required = true ) int townId,
                               @RequestParam(required = true, defaultValue = "" ) String action,
                               Model model) {
        int linkId=townService.findCountryByTownId(townId).getId();
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                    userService.findUserByName(currentPrincipalName).getId(),"Вы удалили город.");
            notificationService.saveNotification(notificationAdmin);
            List<User>lstUs=userService.allUsers();
            for (User user:lstUs){
                if (user.getRegNumber()!=0){
                    Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                            userService.findUserByName(currentPrincipalName).getId(),"Город №"+townId+" был удалён.");
                    notificationService.saveNotification(notificationCompany);
                    List<Order>lstOrd = orderService.allOrders();
                    for (Order order:lstOrd){
                        if (terminalService.findTownByTerminalId(arrivalService.findTerminalByArrivalId(order.getArrivalId()).getId()).getId()==townId &&
                                trainService.findTrainById(order.getTrainId()).getNameTrain().equals(user.getUsername())){
                            Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                                    userService.findUserByName(currentPrincipalName).getId(),
                                    "Город №"+townId+" был удалён, ваш возврат составил: "+
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
            }
            townService.deleteTown(townId);
        }
        return "redirect:/adminPanel/adminTown?countryId="+linkId;
    }
    @GetMapping("/adminPanel/adminTown/viewAdminTown")
    public String getViewTown(@RequestParam(required = true ) int townId,Model model) {
        model.addAttribute("townForm", townService.findTownById(townId));
        return "viewAdminTown";
    }
    @GetMapping("/adminPanel/adminTown/editAdminTown")
    public String getEditTown(@RequestParam(required = true ) int townId,Model model) {
        model.addAttribute("townForm",townService.findTownById(townId));
        return "editAdminTown";
    }
    @PostMapping("/adminPanel/adminTown/editAdminTown")
    public String editTown(@ModelAttribute("townForm") @Valid Town townForm, BindingResult bindingResult, Model model) {
        int linkId;
        if (bindingResult.hasErrors()) {
            return "editAdminTown";
        }
        if (countryService.findCountryById(townForm.getCountryId()).getId()==0){
            return "redirect:/adminPanel/adminTown/editAdminTown?error=true&townId="+townForm.getId();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                userService.findUserByName(currentPrincipalName).getId(),"Вы изменили страну.");
        notificationService.saveNotification(notificationAdmin);
        List<User>lstUs=userService.allUsers();
        for (User user:lstUs){
            if (user.getRegNumber()!=0){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Город №"+townForm.getId()+" был изменён.");
                notificationService.saveNotification(notificationCompany);
                List<Order>lstOrd = orderService.allOrders();
                for (Order order:lstOrd){
                    if (terminalService.findTownByTerminalId(arrivalService.findTerminalByArrivalId(order
                            .getArrivalId()).getId()).getId()==townForm.getId() &&
                            trainService.findTrainById(order.getTrainId()).getNameTrain().equals(user.getUsername())){
                        Notification notificationClient = notificationService.createNotification(userService
                                .findUserById(order.getUserId()).getUsername(),userService.findUserByName(currentPrincipalName).getId(),
                                "Город №"+townForm.getId()+" был изменён, вы можете вернуть деньги в 'Мои билеты', если вам не подходит.");
                        notificationService.saveNotification(notificationClient);
                    }
                }
            }
        }
        if (!townService.updateTown(townForm)){
            linkId=townService.findCountryByTownId(townForm.getId()).getId();
            return "redirect:/adminPanel/adminTown?countryId="+linkId;
        }
        linkId=townService.findCountryByTownId(townForm.getId()).getId();
        return "redirect:/adminPanel/adminTown?countryId="+linkId;
    }
    @GetMapping("/adminPanel/adminTown/createAdminTown")
    public String getCreateTown(@RequestParam(required = true )int undoListCreate,Model model) {
        model.addAttribute("undoListCreate", undoListCreate);
        model.addAttribute("townForm", new Town());
        return "createAdminTown";
    }
    @PostMapping("/adminPanel/adminTown/createAdminTown")
    public String addTown(@ModelAttribute("townForm") @Valid Town townForm, BindingResult bindingResult, Model model) {
        int linkId;
        if (bindingResult.hasErrors()) {
            return "createAdminTown";
        }

        if (!townService.saveTown(townForm)){
            return "createAdminTown";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Notification notificationCompany = notificationService.createNotification(currentPrincipalName,
                userService.findUserByName(currentPrincipalName).getId(),"Вы создали новый город.");
        notificationService.saveNotification(notificationCompany);
        linkId=townService.findCountryByTownId(townForm.getId()).getId();
        return "redirect:/adminPanel/adminTown?countryId="+linkId;
    }
    @GetMapping("/adminPanel/adminTerminal")
    public String terminalList(@RequestParam(required = true ) int townId,Model model) {
        model.addAttribute("allTerminals", townService.allTerminals(townId));
        model.addAttribute("undoLink", townService.findCountryByTownId(townId).getId());
        model.addAttribute("undoListCreate", townId);
        return "adminTerminal";
    }
    @PostMapping("/adminPanel/adminTerminal")
    public String  deleteTerminal(@RequestParam(required = true ) int terminalId,
                                     @RequestParam(required = true, defaultValue = "" ) String action,
                                     Model model) {
        int linkId=terminalService.findTownByTerminalId(terminalId).getId();
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                    userService.findUserByName(currentPrincipalName).getId(),"Вы удалили вокзал.");
            notificationService.saveNotification(notificationAdmin);
            List<User>lstUs=userService.allUsers();
            for (User user:lstUs){
                if (user.getRegNumber()!=0){
                    Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                            userService.findUserByName(currentPrincipalName).getId(),"Вокзал №"+terminalId+" был удалён.");
                    notificationService.saveNotification(notificationCompany);
                    List<Order>lstOrd = orderService.allOrders();
                    for (Order order:lstOrd){
                        if (arrivalService.findTerminalByArrivalId(order.getArrivalId()).getId()==terminalId &&
                                trainService.findTrainById(order.getTrainId()).getNameTrain().equals(user.getUsername())){
                            Notification notificationClient = notificationService.createNotification(userService
                                    .findUserById(order.getUserId()).getUsername(),userService.findUserByName(currentPrincipalName).getId(),
                                    "Вокзал №"+terminalId+" был удалён, ваш возврат составил: "+
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
            }
            terminalService.deleteTerminal(terminalId);
        }
        return "redirect:/adminPanel/adminTerminal?townId="+linkId;
    }
    @GetMapping("/adminPanel/adminTerminal/viewAdminTerminal")
    public String getViewTerminal(@RequestParam(required = true ) int terminalId,Model model) {
        model.addAttribute("terminalForm", terminalService.findTerminalById(terminalId));
        return "viewAdminTerminal";
    }
    @GetMapping("/adminPanel/adminTerminal/editAdminTerminal")
    public String getEditTerminal(@RequestParam(required = true ) int terminalId,Model model) {
        model.addAttribute("terminalForm",terminalService.findTerminalById(terminalId));
        return "editAdminTerminal";
    }
    @PostMapping("/adminPanel/adminTerminal/editAdminTerminal")
    public String editTerminal(@ModelAttribute("terminalForm") @Valid Terminal terminalForm, BindingResult bindingResult, Model model) {
        int linkId;
        if (bindingResult.hasErrors()) {
            return "editAdminTerminal";
        }
        if (townService.findTownById(terminalForm.getTownId()).getId()==0){
            return "redirect:/adminPanel/adminTerminal/editAdminTerminal?error=true&terminalId="+terminalForm.getId();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                userService.findUserByName(currentPrincipalName).getId(),"Вы изменили вокзал.");
        notificationService.saveNotification(notificationAdmin);
        List<User>lstUs=userService.allUsers();
        for (User user:lstUs){
            if (user.getRegNumber()!=0){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Вокзал №"+terminalForm.getId()+" был изменён.");
                notificationService.saveNotification(notificationCompany);
                List<Order>lstOrd = orderService.allOrders();
                for (Order order:lstOrd){
                    if (arrivalService.findTerminalByArrivalId(order.getArrivalId()).getId()==terminalForm.getId() &&
                            trainService.findTrainById(order.getTrainId()).getNameTrain().equals(user.getUsername())){
                        Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                                userService.findUserByName(currentPrincipalName).getId(),"Вокзал №"+terminalForm.getId()+
                                        " был изменён, вы можете вернуть деньги в 'Мои билеты', если вам не подходит.");
                        notificationService.saveNotification(notificationClient);
                    }
                }
            }
        }

        if (!terminalService.updateTerminal(terminalForm)){
            linkId=terminalService.findTownByTerminalId(terminalForm.getId()).getId();
            return "redirect:/adminPanel/adminTerminal?townId="+linkId;
        }
        linkId=terminalService.findTownByTerminalId(terminalForm.getId()).getId();
        return "redirect:/adminPanel/adminTerminal?townId="+linkId;
    }
    @GetMapping("/adminPanel/adminTerminal/createAdminTerminal")
    public String getCreateTerminal(@RequestParam(required = true )int undoListCreate,Model model) {
        model.addAttribute("undoListCreate", undoListCreate);
        model.addAttribute("terminalForm", new Terminal());
        return "createAdminTerminal";
    }
    @PostMapping("/adminPanel/adminTerminal/createAdminTerminal")
    public String addTerminal(@ModelAttribute("terminalForm") @Valid Terminal terminalForm, BindingResult bindingResult, Model model) {
        int linkId;
        if (bindingResult.hasErrors()) {
            return "createAdminTerminal";
        }

        if (!terminalService.saveTerminal(terminalForm)){
            return "createAdminTerminal";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Notification notificationCompany = notificationService.createNotification(currentPrincipalName,
                userService.findUserByName(currentPrincipalName).getId(),"Вы создали новый вокзал.");
        notificationService.saveNotification(notificationCompany);
        linkId=terminalService.findTownByTerminalId(terminalForm.getId()).getId();
        return "redirect:/adminPanel/adminTerminal?townId="+linkId;
    }
    @GetMapping("/adminPanel/adminArrival")
    public String arrivalList(@RequestParam(required = true ) int terminalId,Model model) {
        model.addAttribute("allArrivals", terminalService.allArrivals(terminalId));
        model.addAttribute("undoLink", terminalService.findTownByTerminalId(terminalId).getId());
        model.addAttribute("undoListCreate", terminalId);
        return "adminArrival";
    }
    @PostMapping("/adminPanel/adminArrival")
    public String  deleteArrival(@RequestParam(required = true ) int arrivalId,
                               @RequestParam(required = true, defaultValue = "" ) String action,
                               Model model) {
        int linkId=arrivalService.findTerminalByArrivalId(arrivalId).getId();
        if (action.equals("delete")){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Notification notificationAdmin = notificationService.createNotification(currentPrincipalName,
                    userService.findUserByName(currentPrincipalName).getId(),"Вы удалили маршрут.");
            notificationService.saveNotification(notificationAdmin);
            User user = userService.findUserByName(arrivalService.findTrainByArrivalId(arrivalId).getNameTrain());
            if (user!=null){
                Notification notificationCompany = notificationService.createNotification(user.getUsername(),
                        userService.findUserByName(currentPrincipalName).getId(),"Ваш маршрут №"+arrivalId+" был удалён.");
                notificationService.saveNotification(notificationCompany);
                List<Order>lstOrd=arrivalService.allOrders(arrivalId);
                for (Order order:lstOrd){
                    Notification notificationClient = notificationService.createNotification(userService.findUserById(order.getUserId()).getUsername(),
                            userService.findUserByName(currentPrincipalName).getId(),
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
        return "redirect:/adminPanel/adminArrival?terminalId="+linkId;
    }
    @GetMapping("/adminPanel/adminArrival/viewAdminArrival")
    public String getViewArrival(@RequestParam(required = true ) int arrivalId,Model model) {
        model.addAttribute("arrivalForm", arrivalService.findArrivalById(arrivalId));
        return "viewAdminArrival";
    }
}