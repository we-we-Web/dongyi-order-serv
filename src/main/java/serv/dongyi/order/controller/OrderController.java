package serv.dongyi.order.controller;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class OrderController {
    @GetMapping()
    public String Handle() throws ExecutionException, InterruptedException {
        return "hello dongyi order service";
    }
}