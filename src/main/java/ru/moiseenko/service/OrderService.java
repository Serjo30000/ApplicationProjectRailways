package ru.moiseenko.service;

import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Order;
import ru.moiseenko.entity.Ticket;
import ru.moiseenko.repository.OrderRepository;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.sourceforge.barbecue.Barcode;
@Service
public class OrderService implements IOrderService{
    @Autowired
    OrderRepository orderRepository;
    public Order findOrderById(int orderId){
        Optional<Order> orderFromDb = orderRepository.findById(orderId);
        return orderFromDb.orElse(new Order());
    }
    public List<Order> allOrders() {
        return (List<Order>) orderRepository.findAll();
    }
    public boolean deleteOrder(int orderId) {
        if (orderRepository.findById(orderId).isPresent()) {
            orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
    public boolean saveOrder(Order order) {
        if (order == null) {
            return false;
        }
        orderRepository.save(order);
        return true;
    }
    @Override
    public boolean updateOrder(Order order) {
        if (order == null) {
            return true;
        }
        Order orderToUpdate = orderRepository.findById(order.getId()).get();
        orderToUpdate.setTicketId(order.getTicketId());
        orderToUpdate.setUserId(order.getUserId());
        orderToUpdate.setArrivalId(order.getArrivalId());
        orderToUpdate.setTrainId(order.getTrainId());
        orderToUpdate.setVanId(order.getVanId());
        orderToUpdate.setPlaceId(order.getPlaceId());
        orderToUpdate.setCompartmentId(order.getCompartmentId());
        orderToUpdate.setCreditCard(order.getCreditCard());
        orderToUpdate.setNameUser(order.getNameUser());
        orderToUpdate.setSurnameUser(order.getSurnameUser());
        orderToUpdate.setPatronymicUser(order.getPatronymicUser());
        orderToUpdate.setSexUser(order.getSexUser());
        orderToUpdate.setBirthdayUser(order.getBirthdayUser());
        orderToUpdate.setBarcode(order.getBarcode());
        orderRepository.updateOrder(orderToUpdate.getId(),orderToUpdate.getTicketId(),orderToUpdate.getUserId(),
                orderToUpdate.getArrivalId(),orderToUpdate.getTrainId(),orderToUpdate.getVanId(),
                orderToUpdate.getPlaceId(),orderToUpdate.getCompartmentId(),orderToUpdate.getCreditCard(),
                orderToUpdate.getNameUser(),orderToUpdate.getSurnameUser(),orderToUpdate.getPatronymicUser(),
                orderToUpdate.getSexUser(),orderToUpdate.getBirthdayUser(),orderToUpdate.getBarcode());
        return false;
    }

    @Override
    public Ticket findTicketByOrderId(int orderId) {
        Optional<Ticket> ticketFromDb = orderRepository.findTicketById(orderId);
        return ticketFromDb.orElse(new Ticket());
    }
    @Override
    public String generateEAN13Code(int orderId){
        int indexCode=orderId+100000000;
        return "60"+indexCode+"4";
    }

    @Override
    public BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
        Barcode barcode = BarcodeFactory.createEAN13(barcodeText);
        return BarcodeImageHandler.getImage(barcode);
    }
    @Override
    public List<Order> allOrdersByUser(int userId) {
        return orderRepository.findOrderByUser(userId);
    }
    @Override
    public List<Order> sortOrder(List<Order>lstOrd,String sortName,String sortBy){
        List<Order> lstOrder = new ArrayList<>();
        if (sortName.equals("Отсутствует")){
            return lstOrd;
        }
        if (sortName.equals("По номеру билета")){
            if (sortBy.equals("По возрастанию")){
                lstOrder=orderRepository.findOrderOrderByTicketAsc();
            }
            else{
                lstOrder=orderRepository.findOrderOrderByTicketDesc();
            }
        }
        if (sortName.equals("По названию поезда")){
            if (sortBy.equals("По возрастанию")){
                lstOrder=orderRepository.findOrderOrderByTrainAsc();
            }
            else{
                lstOrder=orderRepository.findOrderOrderByTrainDesc();
            }
        }
        if (sortName.equals("По дате")){
            if (sortBy.equals("По возрастанию")){
                lstOrder=orderRepository.findOrderOrderByArrivalAsc();
            }
            else{
                lstOrder=orderRepository.findOrderOrderByArrivalDesc();
            }
        }
        if (sortName.equals("По типу отдела")){
            if (sortBy.equals("По возрастанию")){
                lstOrder=orderRepository.findOrderOrderByCompartmentAsc();
            }
            else{
                lstOrder=orderRepository.findOrderOrderByCompartmentDesc();
            }
        }
        if (sortName.equals("По номеру места")){
            if (sortBy.equals("По возрастанию")){
                lstOrder=orderRepository.findOrderOrderByPlaceNumberAsc();
            }
            else{
                lstOrder=orderRepository.findOrderOrderByPlaceNumberDesc();
            }
        }
        if (sortName.equals("По стоимости")){
            if (sortBy.equals("По возрастанию")){
                lstOrder=orderRepository.findOrderOrderByPlacePriceAsc();
            }
            else{
                lstOrder=orderRepository.findOrderOrderByPlacePriceDesc();
            }
        }
        List<Order>lstCopyOrder=new ArrayList<>();
        for (int i=0;i<lstOrder.size();i++){
            for (int j=0;j<lstOrd.size();j++){
                if(lstOrder.get(i).getId().equals(lstOrd.get(j).getId())){
                    lstCopyOrder.add(lstOrd.get(j));
                    break;
                }
            }
        }
        return lstCopyOrder;
    }
}
