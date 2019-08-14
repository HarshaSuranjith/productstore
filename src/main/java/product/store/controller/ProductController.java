package product.store.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import product.store.entity.Product;
import product.store.service.ProductService;
import product.store.service.ProductServiceImpl;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "products", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductController {

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

    @PutMapping
    @ResponseBody
    public Product update(@RequestBody @Valid Product product) {
        return this.productService.update(product);
    }

    @PatchMapping("products")
    public ResponseEntity<?> update(@RequestBody Map<String, Object> values) {
        if (values.isEmpty()) {
            return ResponseEntity.badRequest().body("product information is empty");
        }
        Product updated = this.productService.updatePartial(values);
        return ResponseEntity.ok(updated);
    }

}
