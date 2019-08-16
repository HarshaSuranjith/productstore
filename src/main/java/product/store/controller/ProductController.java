package product.store.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.store.entity.Product;
import product.store.service.ProductService;
import product.store.service.ProductServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/products", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController {

    private static final String RESPONSE_MESSAGE_KEY = "message";
    private static final String RESPONSE_MESSAGE_SUCCESS_VALUE = "product deleted";
    private static final String RESPONSE_MESSAGE_FAILED_VALUE = "product delete failed";

    private final ProductService productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @ApiOperation("Retrieve All Products in the System")
    @GetMapping
    @ResponseBody
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    @ApiOperation("Create a new Product in the System")
    @PostMapping
    @ResponseBody
    public Product createProduct(
            @ApiParam(value = "Details of the new Product")
            @RequestBody Product product) {
        return productService.createProduct(product);
    }

    @ApiOperation("update an existing product in the System")
    @PutMapping
    @ResponseBody
    public Product update(@RequestBody Product product) {
        return this.productService.update(product);
    }

    @ApiOperation("Update certain values/fields of a product in the System")
    @PatchMapping
    public ResponseEntity<?> update(@RequestParam Long Id,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String productCode,
                                    @RequestParam(required = false) String description) {
        if (StringUtils.isBlank(name) && StringUtils.isBlank(productCode) && StringUtils.isBlank(description)) {
            return ResponseEntity.badRequest().build();
        }
        Product update = this.productService.updatePartial(Id, name, productCode, description);
        return ResponseEntity.ok(update);
    }

    @ApiOperation(("Get details of a product using product Id"))
    @GetMapping(value = "/{productId}")
    public ResponseEntity<?> getById(@PathVariable Long productId) {
        Optional<Product> product = this.productService.getProductById(productId);
        if (!product.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product.get());
    }

    @ApiOperation("Deletes a Product in the System")
    @DeleteMapping
    public ResponseEntity<?> deleteProduct(
            @ApiParam(value = "the Product to delete")
            @RequestBody Product product) {
        boolean isDeleted = false;
        Map<String, Object> result = new HashMap<>();
        try {
            isDeleted = productService.deleteProductByProductCode(product);
        } catch (Exception ex) {
            throw ex;
        }

        if (isDeleted) {
            result.put(RESPONSE_MESSAGE_KEY, RESPONSE_MESSAGE_SUCCESS_VALUE);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put(RESPONSE_MESSAGE_KEY, RESPONSE_MESSAGE_FAILED_VALUE);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
