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
    Product getProductByProductCode(String productCode);

    @Transactional
    Product createProduct(Product product);

    @Transactional
    Product update(Product product);

    @Transactional
    Product updatePartial(Map<String, Object> values);

    @Transactional(readOnly = true)
    Optional<Product> getProductById(Long productId);

    @Transactional
    boolean deleteProductByProductCode(Product product);

    Product updatePartial(Long id, String name, String productCode, String description);
}
