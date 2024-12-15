package serv.dongyi.order.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class OrderControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Test
    void createOrder_Success() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String requestBody = """
            {
                "owner": "demo@gmail.com",
                "content": [
                    {"id": "p1", "price": 299, "quantity": 2},
                    {"id": "p2", "price": 249, "quantity": 1}
                ]
            }
        """;

        mockMvc.perform(post("/api/order-create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner").value("demo@gmail.com"))
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getOrder_Success() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Pre-insert test data into H2
        String requestBody = """
            {
                "owner": "demo@gmail.com",
                "content": [
                    {"id": "p1", "price": 299, "quantity": 2},
                    {"id": "p2", "price": 249, "quantity": 1}
                ]
            }
        """;
        MvcResult createResult = mockMvc.perform(post("/api/order-create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String responseContent = createResult.getResponse().getContentAsString();
        String orderId = JsonPath.parse(responseContent).read("$.id");

        mockMvc.perform(post("/api/order-get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                        {"id": "%s", "owner": "demo@gmail.com"}
                    """, orderId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.owner").value("demo@gmail.com"));
    }
}