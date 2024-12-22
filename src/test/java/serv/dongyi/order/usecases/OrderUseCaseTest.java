package serv.dongyi.order.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import serv.dongyi.order.domain.Order;
import serv.dongyi.order.domain.Product;
import serv.dongyi.order.repository.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderUseCase orderUseCase;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        MockitoAnnotations.openMocks(this);
        orderUseCase = new OrderUseCase(restTemplateBuilder, orderRepository);
    }

    @Test
    void createOrder_Success() {
        String owner = "demo@gmail.com";
        List<Product> content = List.of(
                new Product("2", 299, Map.of(
                        "M", 2
                )),
                new Product("3", 249, Map.of(
                        "L", 3,
                        "XL", 2
                ))
        );

        Order mockOrder = new Order();
        mockOrder.setId("order-01");
        mockOrder.setOwner(owner);
        mockOrder.setContent(content);
        mockOrder.setStatus("created");

        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        // Act
        Order result = orderUseCase.createOrder(owner, content);

        // Assert
        assertNotNull(result);
        assertEquals("order-01", result.getId());
        assertEquals(owner, result.getOwner());
        assertEquals(content.size(), result.getContent().size());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrder_Success() {
        // Arrange
        String owner = "demo@gmail.com";
        String orderId = "order-01";
        Order order = new Order();
        order.setId(orderId);
        order.setOwner(owner);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        Order result = orderUseCase.getOrder(orderId, owner);

        // Assert
        assertEquals(orderId, result.getId());
        assertEquals(owner, result.getOwner());
    }

    @Test
    void getOrder_Fail_OrderNotFound() {
        // Arrange
        String owner = "demo@gmail.com";
        String orderId = "non-existent";
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderUseCase.getOrder(orderId, owner));
    }

    @Test
    void getOrder_Fail_UnauthorizedOwner() {
        // Arrange
        String orderId = "order-01";
        String owner = "wrong-owner@gmail.com";
        Order order = new Order();
        order.setId(orderId);
        order.setOwner("demo@gmail.com");
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderUseCase.getOrder(orderId, owner));
    }
}