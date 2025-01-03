package serv.dongyi.order.controller;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import serv.dongyi.order.domain.Order;
import serv.dongyi.order.domain.Product;
import serv.dongyi.order.controller.dto.CreateOrderRequest;
import serv.dongyi.order.usecases.OrderUseCase;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderUseCase orderUseCase;

    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @GetMapping()
    public String Handle() throws ExecutionException, InterruptedException {
        return "hello dongyi order service";
    }

    @PostMapping("/order-create")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest requestBody) {
        String owner = (String) requestBody.getOwner();
        List<Product> content = (List<Product>) requestBody.getContent();
        try {
            Order order = orderUseCase.createOrder(owner, content);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/order-get")
    public ResponseEntity<Order> getOrder(@RequestBody Map<String, Object> requestBody) {
        String orderId = (String) requestBody.get("id");
        String owner = (String) requestBody.get("owner");
        try {
            Order order = orderUseCase.getOrder(orderId, owner);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/orders-get")
    public ResponseEntity<?> getAllOrders(@RequestBody Map<String, Object> requestBody) {
        String adminId = (String) requestBody.get("id");

        if (adminId == null || adminId.isEmpty()) {
            return ResponseEntity.status(400).body("Bad Request: Missing adminId");
        }

        try {
            List<String> orderIds = orderUseCase.getAllOrderIds(adminId);
            if (orderIds.isEmpty()) {
                return ResponseEntity.status(404).body("No orders found");
            }
            return ResponseEntity.ok(orderIds);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}