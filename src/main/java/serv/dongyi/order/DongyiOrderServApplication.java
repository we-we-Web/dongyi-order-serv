package serv.dongyi.order;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class DongyiOrderServApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		Map<String, String> env = new HashMap<>();
		for (DotenvEntry entry : dotenv.entries()) {
			env.put(entry.getKey(), entry.getValue());
		}
		env.forEach(System::setProperty);
		SpringApplication.run(DongyiOrderServApplication.class, args);
	}

}
