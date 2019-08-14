package product.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {

        Assert.notNull(product.getId(), "Product 'Id' must be not null");
        return this.productRepository.save(product);

    }

    @Override
    public Product updatePartial(Map<String, Object> values) {
        return null;
    }
}
