package product.store.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import product.store.entity.Product;
import product.store.exceptions.InvalidProductDetailsException;
import product.store.exceptions.ProductNotExistingException;
import product.store.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Product product) {

        // TODO: need to improve the error handling.
        Assert.notNull(product.getId(), "Product 'Id' must be not null");
        if (!product.isValid()) {
            throw new InvalidProductDetailsException();
        }

        if (!productRepository.existsById(product.getId())) {
            throw new ProductNotExistingException();
        }
        return this.productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updatePartial(Map<String, Object> values) {
        // TODO : implementation pending
        throw new UnsupportedOperationException("No implementation found");
    }

    @Override
    public Product updatePartial(Long Id, String name, String productCode, String description) {
        Product productInDb = this.productRepository.findById(Id).get();
        if (!StringUtils.isBlank(name)) {
            productInDb.setName(name);
        }

        if (!StringUtils.isBlank(productCode)) {
            productInDb.setProductCode(productCode);
        }

        if (!StringUtils.isBlank(description)) {
            productInDb.setDescription(description);
        }

        if (!productInDb.isValid()) {
            throw new InvalidProductDetailsException();
        }
        return this.productRepository.save(productInDb);

    }

    @Override
    @Transactional
    public Optional<Product> getProductById(Long productId) {
        return this.productRepository.findById(productId);
    }

    @Override
    @Transactional
    public Product getProductByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode);
    }

    @Transactional
    public boolean deleteProductByProductCode(Product product) {
        boolean isDeleted = false;
        try {
            Product productToDelete = getProductByProductCode(product.getProductCode());
            productRepository.delete(productToDelete);
            isDeleted = true;
        } catch (Exception exception) { //TODO: handling various exception types in a common error handler i.e:- IllegalArgumentException, DuplicateKeyException, EmptyResultDataAccessException etc..
            isDeleted = false;
            throw exception;
        }
        return isDeleted;
    }


}
