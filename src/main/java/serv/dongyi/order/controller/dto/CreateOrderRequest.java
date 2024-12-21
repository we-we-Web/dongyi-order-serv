package serv.dongyi.order.controller.dto;

import serv.dongyi.order.domain.Product;
import java.util.List;

public class CreateOrderRequest {
    private String owner;
    private List<Product> content;

    // Getter and Setter
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Product> getContent() {
        return content;
    }

    public void setContent(List<Product> content) {
        this.content = content;
    }
}