package sample.spring.service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sample.spring.service.models.Product;
import sample.spring.service.repositories.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {

	private ProductRepository productRepository;

	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@GetMapping
	private Flux<Product> getAllProducts() {
		return productRepository.findAllProducts();
	}

	@GetMapping("/{id}")
	private Mono<Product> getAllProducts(@PathVariable Long id) {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return productRepository.findProductById(id);
	}

}
