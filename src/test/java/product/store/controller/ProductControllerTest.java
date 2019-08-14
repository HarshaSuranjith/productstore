package product.store.controller;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import product.store.entity.Product;
import product.store.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProductControllerTest {

    private String baseUrl = "http://localhost:8080";
    private RestTemplate restTemplate = new RestTemplate();
    private String apiUrl = baseUrl + "/products";

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testGetAllProducts_success() {

        List<Product> products = restTemplate.getForObject(apiUrl, List.class);
        assertThat(products, is(notNullValue()));
        assertThat(products, hasSize(2));

    }

    @Test
    public void testCreateProduct() {
        Product sunlightSoap = Product.of("Sunlight", "ASXD1243432", "General soap product");
        Product product = restTemplate.postForObject(apiUrl, sunlightSoap, Product.class);

        assertThat(product, is(notNullValue()));
        assertThat(product.getId(), is(notNullValue()));
    }

    @Test
    public void testUpdateProduct() {
        String newSerialNumber = "12SFJU784";

        Product cocaCola = Product.of(1001L,"CocaCola", newSerialNumber, "");

        restTemplate.put(apiUrl, cocaCola);
        List<Product> products = restTemplate.getForObject(apiUrl, List.class);

        Product updatedCola = products.stream().filter(p-> p.getId().equals(1001L)).findFirst().get();

        assertThat(updatedCola.getSerialNumber(), is(newSerialNumber));
    }

}