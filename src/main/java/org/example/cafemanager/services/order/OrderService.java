package org.example.cafemanager.services.order;

import org.example.cafemanager.domain.Order;
import org.example.cafemanager.domain.ProductsInOrder;
import org.example.cafemanager.domain.User;
import org.example.cafemanager.domain.enums.Status;
import org.example.cafemanager.dto.order.OrderDetails;
import org.example.cafemanager.dto.order.UpdateProductInOrderDto;

public interface OrderService {
    Order createOrder(OrderDetails orderDetails);

    void deleteProductInOrder(Long orderId, Long productInOrderId, User user);

    ProductsInOrder updateProductInOrder(UpdateProductInOrderDto productUpdate);

    Order updateOrderStatus(Long orderId, Status status, User user);

    void deleteOrder(Long orderId, User user);
}
