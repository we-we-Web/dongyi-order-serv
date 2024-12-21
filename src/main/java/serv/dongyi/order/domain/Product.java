package serv.dongyi.order.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "products")
public class Product {
    @Id
    private String id;
    private int price;
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Integer> spec;

    public Product() {

    }

    public Product(String id, int price, Map<String, Integer> spec) {
        this.id = id;
        this.price = price;
        this.spec = spec;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void  setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setSpec(Map<String, Integer> spec) {
        this.spec = spec;
    }

    public Map<String, Integer> getSpec() {
        return spec;
    }
}

@Converter
class MapToJsonConverter implements AttributeConverter<Map<String, Integer>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Integer> attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Could not convert Map to JSON", e);
        }
    }

    @Override
    public Map<String, Integer> convertToEntityAttribute(String content) {
        try {
            // 如果 content 為 null，則回傳一個空的 Map
            return content == null || content.isEmpty()
                    ? new HashMap<>()
                    : objectMapper.readValue(content, new TypeReference<Map<String, Integer>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Could not convert JSON to Map", e);
        }
    }
}