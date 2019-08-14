package product.store.controller;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.client.RestTemplate;
import product.store.entity.Product;
import product.store.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class ProductControllerTest {

    private String baseUrl = "http://localhost:8088";
    private RestTemplate restTemplate = new RestTemplate();
    private String apiUrl = baseUrl + "/products";

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testAllProducts_success() {
        List<Product> products = restTemplate.getForObject(apiUrl, List.class);
        assertThat(products, is(notNullValue()));
        assertThat(products, hasSize(2));
    }

    @Test
    @Transactional
    public void testCreateProduct_success() {
        Product sunlightSoap = Product.of("Sunlight", "ASXD1243432", "General soap product");
        Product product = restTemplate.postForObject(apiUrl, sunlightSoap, Product.class);

        assertThat(product, is(notNullValue()));
        assertThat(product.getId(), is(notNullValue()));
    }

    @Test
    public void testUpdateProduct_success() {
        String newSerialNumber = "12SFJU784";

        Product cocacola = this.productRepository.findById(1001L).get();

        cocacola.setSerialNumber(newSerialNumber);

        restTemplate.put(apiUrl, cocacola);

        Product updatedCocaCola = this.productRepository.findById(1001L).get();
        assertThat(updatedCocaCola.getSerialNumber(), is(newSerialNumber));

    }

    @Test
    public void testFindById() {
        String endpoint = apiUrl + "/1001";
        Product sugar = restTemplate.getForObject(endpoint, Product.class);

        assertThat(sugar.getId(), is(1001L));
        assertThat(sugar.getName(), is("Sugar"));
        assertThat(sugar.getSerialNumber(), is("123465XX112"));
        assertThat(sugar.getDescription(), is(isEmptyString()));
    }

}