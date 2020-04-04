package org.example.cafemanager.services.order.impls;

import org.example.cafemanager.domain.*;
import org.example.cafemanager.domain.enums.Status;
import org.example.cafemanager.dto.order.OrderDetails;
import org.example.cafemanager.dto.order.ProductInOrderReq;
import org.example.cafemanager.dto.order.UpdateProductInOrderDto;
import org.example.cafemanager.repositories.OrderRepository;
import org.example.cafemanager.repositories.ProductsInOrderRepository;
import org.example.cafemanager.services.exceptions.ChooseAtLeastOneException;
import org.example.cafemanager.services.exceptions.InstanceNotFoundException;
import org.example.cafemanager.services.order.OrderService;
import org.example.cafemanager.services.product.ProductService;
import org.example.cafemanager.services.table.TableService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private TableService tableService;
    private ProductService productService;
    private ProductsInOrderRepository productsInOrderRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductsInOrderRepository productsInOrderRepository,
            TableService tableService,
            ProductService productService) {

        this.orderRepository = orderRepository;
        this.tableService = tableService;
        this.productService = productService;
        this.productsInOrderRepository = productsInOrderRepository;
    }

    @Transactional
    @Override
    public Order createOrder(@NotNull OrderDetails orderDetails) {
        Assert.notNull(orderDetails, "OrderDetails can not be null");
        CafeTable table = tableService.getUserAssignedTable(orderDetails.getTableId(), orderDetails.getUser());
        if (null == table) {
            throw new InstanceNotFoundException("table");
        }

        Set<Product> productsSet = new HashSet<>();

        Order order = new Order();
        order.setCafeTable(table);

        Order openedOrder = orderRepository.findOrderByStatusAndCafeTable(Status.OPEN, order.getCafeTable());

        if (null != openedOrder) {
            openedOrder.setStatus(Status.CLOSED);
            orderRepository.save(openedOrder);
        }

        orderRepository.save(order);
        for (ProductInOrderReq pio : orderDetails.getProdsInOrder()) {
            Product product = productService.findOneById(pio.getProductId());
            if (null == product) {
                continue;
            }

            ProductsInOrder productsInOrder = new ProductsInOrder(pio.getAmount(), product, order);
            productsSet.add(product);
            productsInOrderRepository.save(productsInOrder);
        }

        if (0 == productsSet.size()) {
            throw new ChooseAtLeastOneException("product");
        }

        return order;
    }

    @Override
    @Transactional
    public void deleteProductInOrder(Long orderId, Long productInOrderId, User user) {
        Order order = getOrderByIdAndUser(orderId, user);

        ProductsInOrder pio = ordersProductInOrder(order, productInOrderId);

        order.removeProductInOrder(pio);
        productsInOrderRepository.delete(pio);
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId, User user) {
        Order order = getOrderByIdAndUser(orderId, user);
        if (null == order) {
            throw new InstanceNotFoundException("Order");
        }

        order.setProductsInOrders(new HashSet<>());
        orderRepository.delete(order);
    }

    @Override
    public ProductsInOrder updateProductInOrder(@NotNull UpdateProductInOrderDto productUpdate) {
        Assert.notNull(productUpdate, "UpdateProductInOrderDto can not be null");
        Order order = getOrderByIdAndUser(productUpdate.getOrderId(), productUpdate.getUser());
        if (null == order) {
            throw new InstanceNotFoundException("Order");
        }

        ProductsInOrder pio = ordersProductInOrder(order, productUpdate.getPioId());
        if (null == pio) {
            throw new InstanceNotFoundException("Product in order");
        }

        pio.setAmount(productUpdate.getAmount());

        productsInOrderRepository.save(pio);

        return pio;
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, @NotNull Status status, User user) {
        Assert.notNull(status, "Status can not be null");
        Order order = getOrderByIdAndUser(orderId, user);
        if (null == order) {
            throw new InstanceNotFoundException("Order");
        }

        boolean isRequestedStatusIsOpen = status.equals(Status.OPEN);
        if (order.getStatus().equals(status) && isRequestedStatusIsOpen) {
            return order;
        }

        if (!isRequestedStatusIsOpen) {
            order.setStatus(status);
            orderRepository.save(order);
            return order;
        }

        Order openedOrder = orderRepository.findOrderByStatusAndCafeTable(Status.OPEN, order.getCafeTable());

        if (null != openedOrder) {
            openedOrder.setStatus(Status.CLOSED);
            orderRepository.save(openedOrder);
        }

        order.setStatus(Status.OPEN);
        orderRepository.save(order);
        return order;
    }

    private ProductsInOrder ordersProductInOrder(@NotNull Order order, Long productInOrderId) {
        ProductsInOrder pio = order.getProductsInOrders().stream()
                .filter((productsInOrder) -> productsInOrder.getId().equals(productInOrderId)).findFirst().orElse(null);

        if (null == pio) {
            throw new InstanceNotFoundException("Product In Order");
        }

        return pio;
    }

    private Order getOrderByIdAndUser(Long orderId, User user) {
        Order order = orderRepository.getByIdAndCafeTable_User(orderId, user);

        if (null == order) {
            throw new InstanceNotFoundException("order");
        }

        return order;
    }
}
