package serv.dongyi.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serv.dongyi.order.domain.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Order save(Order order);
    Optional<Order> findById(String orderId);
//    boolean ownerExists(String ownerId);
}