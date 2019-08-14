package product.store.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.store.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface ProductService {

    @Transactional(readOnly = true)
    List<Product> getAllProducts();

    @Transactional
    Product createProduct(Product product);

    @Transactional
    Product update(Product product);


    Product updatePartial(Map<String, Object> values);

    Optional<Product> findById(Long productId);
}
