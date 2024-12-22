package serv.dongyi.order.usecases;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import serv.dongyi.order.domain.Order;
import serv.dongyi.order.domain.Product;
import serv.dongyi.order.repository.OrderRepository;

@Service
public class OrderUseCase {
    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final EmailService emailService;

    public OrderUseCase(RestTemplateBuilder builder, OrderRepository orderRepository, EmailService emailService) {
        this.restTemplate = builder.build();
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }

    public Order createOrder(String owner, List<Product> content) {
        if (!checkStock(content)) {
            throw new IllegalStateException("stock was not enough");
        }

        String now = ZonedDateTime.now().toString();
        Order order = new Order(
                UUID.randomUUID().toString(),
                owner,
                content,
                "created",
                List.of("created " + now),
                now
        );

        order =  orderRepository.save(order);
        emailService.sendOrderConfirmationEmail(order.getOwner(), order.getId());
        return order;
    }

    public Order getOrder(String orderId, String owner) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getOwner().equals(owner))
                .orElseThrow(() -> new IllegalArgumentException("Order not found or unauthorized"));
    }

    private boolean checkStock(List<Product> content) {
        List<Map<String, Object>> requestBody = content.stream()
                .map(product -> Map.of(
                        "id", product.getId(),
                        "spec", product.getSpec()
                ))
                .collect(Collectors.toList());
        String url = "https://dongyi-api.hnd1.zeabur.app/product/api/product/stock_upd";
        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    new HttpEntity<>(requestBody),
                    Boolean.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Boolean result = response.getBody();
                return Boolean.TRUE.equals(result);
            }
        } catch (Exception e) {
            throw new IllegalStateException("與 Product Service 溝通失敗", e);
        }

        return false;
    }
}