package serv.dongyi.order.usecases;

import org.springframework.stereotype.Service;
import serv.dongyi.order.domain.Order;
import serv.dongyi.order.domain.Product;
import serv.dongyi.order.repository.OrderRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderUseCase {
    private final OrderRepository orderRepository;

    public OrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(String owner, List<Product> content) {
//        if (!orderRepository.ownerExists(owner)) {
//            throw new IllegalArgumentException("Owner not found");
//        }

        String now = ZonedDateTime.now().toString();
        Order order = new Order(
                UUID.randomUUID().toString(),
                owner,
                content,
                "created",
                List.of("created " + now),
                now
        );
        return orderRepository.save(order);
    }

    public Order getOrder(String orderId, String owner) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getOwner().equals(owner))
                .orElseThrow(() -> new IllegalArgumentException("Order not found or unauthorized"));
    }
}