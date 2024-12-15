package serv.dongyi.order.domain;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    private String id;
    private String owner;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<Product> content;
    private String status;
    @ElementCollection
    @CollectionTable(name = "order_progress", joinColumns = @JoinColumn(name = "order_id"))
    private List<String> progress;
    @Column(name = "created_at")
    private String createdAt;

    public Order() {

    }

    public Order(String id, String owner, List<Product> content, String status, List<String> progress, String createdAt) {
        this.id = id;
        this.owner = owner;
        this.content = content;
        this.status = status;
        this.progress = progress;
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setContent(List<Product> content) {
        this.content = content;
    }

    public List<Product> getContent() {
        return content;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setProgress(List<String> progress) {
        this.progress = progress;
    }

    public List<String> getProgress() {
        return progress;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}