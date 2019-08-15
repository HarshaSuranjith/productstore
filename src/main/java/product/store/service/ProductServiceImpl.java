package product.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import product.store.entity.Product;
import product.store.repository.ProductRepository;

@Component
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Product product) {

        Assert.notNull(product.getId(), "Product 'Id' must be not null");
        return this.productRepository.save(product);

    }

    @Override
    @Transactional
    public Product updatePartial(Map<String, Object> values) {
        return null;
    }

    @Override
    @Transactional
    public Optional<Product> findById(Long productId) {
        return this.productRepository.findById(productId);
    }
}
