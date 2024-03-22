package ru.moiseenko.service;

import org.springframework.stereotype.Service;
import ru.moiseenko.entity.Order;
import ru.moiseenko.entity.Ticket;
import java.awt.image.BufferedImage;
import java.util.List;

@Service
public interface IOrderService {
    boolean updateOrder(Order order);
    Ticket findTicketByOrderId(int orderId);
    String generateEAN13Code(int orderId);
    BufferedImage generateEAN13BarcodeImage(String barcodeText)throws Exception;
    List<Order> allOrdersByUser(int userId);
    List<Order> sortOrder(List<Order>lstOrd,String sortName,String sortBy);
}
