package sample.spring.service.repositories;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sample.spring.service.models.Product;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductRepository {

	static Map<Long, Product> productsData;

	static {
		productsData = new HashMap<>();
		productsData.put(1l, new Product(1l, "Product 1"));
		productsData.put(2l, new Product(2l, "Product 2"));
		productsData.put(3l, new Product(3l, "Product 3"));
		productsData.put(4l, new Product(4l, "Product 4"));
		productsData.put(5l, new Product(5l, "Product 5"));
		productsData.put(6l, new Product(6l, "Product 6"));
		productsData.put(7l, new Product(7l, "Product 7"));
		productsData.put(8l, new Product(8l, "Product 8"));
		productsData.put(9l, new Product(9l, "Product 9"));
		productsData.put(10l, new Product(10l, "Product 10"));
	}

	public Flux<Product> findAllProducts() {
		return Flux.fromIterable(productsData.values());
	}

	public Mono<Product> findProductById(Long id) {
		return Mono.just(productsData.get(id));
	}

}
