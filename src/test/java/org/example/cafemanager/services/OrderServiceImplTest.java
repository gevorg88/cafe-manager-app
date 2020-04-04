package org.example.cafemanager.services;

import org.example.cafemanager.EntitiesBuilder;
import org.example.cafemanager.Util;
import org.example.cafemanager.domain.*;
import org.example.cafemanager.domain.enums.Status;
import org.example.cafemanager.dto.order.OrderDetails;
import org.example.cafemanager.dto.order.UpdateProductInOrderDto;
import org.example.cafemanager.repositories.OrderRepository;
import org.example.cafemanager.repositories.ProductsInOrderRepository;
import org.example.cafemanager.services.exceptions.ChooseAtLeastOneException;
import org.example.cafemanager.services.exceptions.InstanceNotFoundException;
import org.example.cafemanager.services.order.impls.OrderServiceImpl;
import org.example.cafemanager.services.product.impls.ProductServiceImpl;
import org.example.cafemanager.services.table.impls.TableServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    TableServiceImpl tableService;

    @Mock
    ProductServiceImpl productService;

    @Mock
    ProductsInOrderRepository productsInOrderRepository;

    @Test
    public void createWithNullableRequest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(null));
    }

    @Test
    public void createWithoutTable() {
        OrderDetails orderDetails = EntitiesBuilder.createOrderDetails();
        Mockito.when(tableService.getUserAssignedTable(orderDetails.getTableId(), orderDetails.getUser()))
                .thenReturn(null);
        Assert.assertThrows(InstanceNotFoundException.class, () -> orderService.createOrder(orderDetails));
    }

    @Test
    public void createWithoutProducts() {
        OrderDetails orderDetails = EntitiesBuilder.createOrderDetails();
        CafeTable table = EntitiesBuilder.createCafeTable();
        Mockito.when(tableService.getUserAssignedTable(orderDetails.getTableId(), orderDetails.getUser()))
                .thenReturn(table);
        Mockito.when(productService.findOneById(orderDetails.getProdsInOrder().iterator().next().getProductId()))
                .thenReturn(null);
        Assert.assertThrows(ChooseAtLeastOneException.class, () -> orderService.createOrder(orderDetails));
    }

    @Test
    public void createWhereNotOpenOrder() {
        OrderDetails orderDetails = EntitiesBuilder.createOrderDetails();
        CafeTable table = EntitiesBuilder.createCafeTable();
        ProductsInOrder pio = EntitiesBuilder.createProductInOrder();
        pio.setId(Util.randomLong());
        Mockito.when(orderRepository.findOrderByStatusAndCafeTable(Status.OPEN, table)).thenReturn(null);
        Mockito.when(tableService.getUserAssignedTable(orderDetails.getTableId(), orderDetails.getUser()))
                .thenReturn(table);
        Mockito.when(productService.findOneById(orderDetails.getProdsInOrder().iterator().next().getProductId()))
                .thenReturn(EntitiesBuilder.createProduct());
        Assert.assertEquals(Status.OPEN, orderService.createOrder(orderDetails).getStatus());
    }

    @Test
    public void createWhereExistsOpenOrder() {
        OrderDetails orderDetails = EntitiesBuilder.createOrderDetails();
        CafeTable table = EntitiesBuilder.createCafeTable();
        Order order = EntitiesBuilder.createOrder();
        Mockito.when(orderRepository.findOrderByStatusAndCafeTable(Status.OPEN, table)).thenReturn(order);
        Mockito.when(tableService.getUserAssignedTable(orderDetails.getTableId(), orderDetails.getUser()))
                .thenReturn(table);
        Mockito.when(productService.findOneById(orderDetails.getProdsInOrder().iterator().next().getProductId()))
                .thenReturn(EntitiesBuilder.createProduct());
        Assert.assertEquals(Status.OPEN, orderService.createOrder(orderDetails).getStatus());
    }

    @Test
    public void deleteProductInOrderOrderNotFound() {
        User user = EntitiesBuilder.createUser();
        Mockito.when(orderRepository.getByIdAndCafeTable_User(1L, user)).thenReturn(null);
        Assert.assertThrows(InstanceNotFoundException.class, () -> orderService.deleteOrder(1L, user));
    }

    @Test
    public void deleteProductInOrderProductInOrderNotFound() {
        User user = EntitiesBuilder.createUser();
        Order order = EntitiesBuilder.createOrder();
        order.setProductsInOrders(Collections.emptySet());
        Mockito.when(orderRepository.getByIdAndCafeTable_User(order.getId(), user)).thenReturn(order);
        Assert.assertThrows(InstanceNotFoundException.class,
                () -> orderService.deleteProductInOrder(order.getId(), 1L, user));
    }

    @Test
    public void deleteOrderOrderNotFound() {
        User user = EntitiesBuilder.createUser();
        Order order = EntitiesBuilder.createOrder();
        Mockito.when(orderRepository.getByIdAndCafeTable_User(order.getId(), user)).thenReturn(null);
        Assert.assertThrows(InstanceNotFoundException.class, () -> orderService.deleteOrder(order.getId(), user));
    }

    @Test
    public void updateOrderStatusWithNullStatus() {
        User user = EntitiesBuilder.createUser();
        Assert.assertThrows(IllegalArgumentException.class, () -> orderService.updateOrderStatus(1L, null, user));
    }

    @Test
    public void updateOrderNotFound() {
        User user = EntitiesBuilder.createUser();
        Order order = EntitiesBuilder.createOrder();
        Mockito.when(orderRepository.getByIdAndCafeTable_User(order.getId(), user)).thenReturn(null);
        Assert.assertThrows(InstanceNotFoundException.class,
                () -> orderService.updateOrderStatus(order.getId(), Status.OPEN, user));
    }

    @Test
    public void updateOrderSameStatus() {
        User user = EntitiesBuilder.createUser();
        Order order = EntitiesBuilder.createOrder();
        Mockito.when(orderRepository.getByIdAndCafeTable_User(order.getId(), user)).thenReturn(order);
        Assert.assertEquals(Status.OPEN, orderService.updateOrderStatus(order.getId(), Status.OPEN, user).getStatus());
    }

    @Test
    public void updateOrderClosed() {
        User user = EntitiesBuilder.createUser();
        Order order = EntitiesBuilder.createOrder();
        Mockito.when(orderRepository.getByIdAndCafeTable_User(order.getId(), user)).thenReturn(order);
        order.setStatus(Status.CLOSED);
        Assert.assertEquals(Status.CLOSED,
                orderService.updateOrderStatus(order.getId(), Status.CLOSED, user).getStatus());
    }

    @Test
    public void updateOrderWithStatusOpen() {
        User user = EntitiesBuilder.createUser();
        Order order = EntitiesBuilder.createOrder();
        order.setStatus(Status.CLOSED);
        Order order2 = EntitiesBuilder.createOrder();
        Mockito.when(orderRepository.getByIdAndCafeTable_User(order.getId(), user)).thenReturn(order);
        Mockito.when(orderRepository.findOrderByStatusAndCafeTable(Status.OPEN, order.getCafeTable()))
                .thenReturn(order2);
        Assert.assertEquals(Status.OPEN, orderService.updateOrderStatus(order.getId(), Status.OPEN, user).getStatus());
        Assert.assertEquals(Status.CLOSED, order2.getStatus());
    }

    @Test
    public void updateProductInOrderWithOrderNotFound() {
        Assert.assertThrows(InstanceNotFoundException.class,
                () -> orderService.updateProductInOrder(EntitiesBuilder.createUpdateProductInOrderDto()));
    }

    @Test
    public void updateProductInOrderWithEmptyUpdateDto() {
        Assert.assertThrows(IllegalArgumentException.class, () -> orderService.updateProductInOrder(null));
    }

    @Test
    public void updateProductInOrderNotFound() {
        UpdateProductInOrderDto dto = EntitiesBuilder.createUpdateProductInOrderDto();
        Order order = EntitiesBuilder.createOrder();
        order.setId(dto.getOrderId());
        Mockito.when(orderRepository.getByIdAndCafeTable_User(dto.getOrderId(), dto.getUser())).thenReturn(order);
        order.setProductsInOrders(Collections.emptySet());
        Assert.assertThrows(InstanceNotFoundException.class, () -> orderService.updateProductInOrder(dto));
    }

    @Test
    public void updateProductInOrder() {
        UpdateProductInOrderDto dto = EntitiesBuilder.createUpdateProductInOrderDto();
        dto.setAmount(10);
        Order order = EntitiesBuilder.createOrder();
        order.setId(dto.getOrderId());
        ProductsInOrder pio = EntitiesBuilder.createProductInOrder();
        pio.setId(dto.getPioId());
        order.addProductsInOrders(pio);
        Mockito.when(orderRepository.getByIdAndCafeTable_User(dto.getOrderId(), dto.getUser())).thenReturn(order);
        Assert.assertEquals(dto.getAmount(), orderService.updateProductInOrder(dto).getAmount());
    }
}
