package gm.bookstore;

import gm.bookstore.view.BookForm;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class BookstoreApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext contextSpring = new SpringApplicationBuilder(BookstoreApplication.class)
															.headless(false)
															.web(WebApplicationType.NONE)
															.run(args);

	// Ejecutar el código para cargar el form
		EventQueue.invokeLater(() -> {
			// Obtener el objeto form a través de Spring
			BookForm bookForm = contextSpring.getBean(BookForm.class);
			bookForm.setVisible(true);
		});

	}

}
