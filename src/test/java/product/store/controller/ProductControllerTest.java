package product.store.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import product.store.entity.Product;
import product.store.repository.ProductRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class ProductControllerTest {

    private static String baseUrl = "http://localhost:8088";
    private RestTemplate restTemplate = new RestTemplate();
    private String apiUrl = baseUrl + "/products";

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testAllProducts_success() throws IOException {
        ResponseEntity<?> response = restTemplate.getForEntity(apiUrl, String.class);
        List<Product> products = getProductsFromResponse(response);

        assertThat(products, is(notNullValue()));
        assertThat(products, hasSize(3));
    }

    @Test
    @Transactional
    public void testCreateProduct_success() {
        Product sunlightSoap = Product.of("Sunlight", "PRO_SUNLIGHT", "General soap product");
        Product product = restTemplate.postForObject(apiUrl, sunlightSoap, Product.class);

        assertThat(product, is(notNullValue()));
        assertThat(product.getId(), is(notNullValue()));
    }

    @Test
    public void testUpdateProduct_success() {
        String newProductCode = "PRO_COKENEW";

        Product cocacola = this.productRepository.findById(1001L).get();

        cocacola.setProductCode(newProductCode);

        HttpEntity<Product> request = new HttpEntity<>(cocacola);
        ResponseEntity<Product> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, request, Product.class);

        Product updatedCocaCola = response.getBody();
        assertThat(updatedCocaCola.getProductCode(), is(newProductCode));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateProduct_product_code_failure() {
        String newProductCode = "COKENEW";

        Product cocacola = this.productRepository.findById(1001L).get();

        cocacola.setProductCode(newProductCode);

        HttpEntity<Product> request = new HttpEntity<>(cocacola);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, request, String.class);

//        Product updatedCocaCola = response.getBody();
//        assertThat(updatedCocaCola.getProductCode(), is(newProductCode));

        System.out.println(response.getBody());
    }

    @Test
    public void testFindById() {
        String endpoint = apiUrl + "/1001";
        Product sugar = restTemplate.getForObject(endpoint, Product.class);

        assertThat(sugar.getId(), is(1001L));
        assertThat(sugar.getName(), is("Sugar"));
        assertThat(sugar.getProductCode(), is("PRO_SG"));
        assertThat(sugar.getDescription(), is(isEmptyString()));
    }

    @Test
    public void testDeleteProduct() {

        HttpEntity<Product> delete = new HttpEntity<>(Product.of("Sugar", "PRO_SG", ""));
        ResponseEntity<?> response = restTemplate.exchange(apiUrl, HttpMethod.DELETE, delete, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        Optional<Product> product = productRepository.findById(1001L);
        assertThat(product.isPresent(), is(Boolean.FALSE));

    }


    private List<Product> getProductsFromResponse(ResponseEntity<?> response) throws IOException {
        Assert.notNull(response.getBody());
        return getProductsFromJson((String) response.getBody());
    }

    private List<Product> getProductsFromJson(String jsonResponse) throws IOException {
        // @formatter:off
        List<Product> products = mapper.readValue(jsonResponse, new TypeReference<List<Product>>() {});
        // @formatter:on
        return products;
    }

    private Product getProductFromResponse(ResponseEntity<?> response) throws IOException {
        Assert.notNull(response.getBody());

        return getProductFromJson((String) response.getBody());
    }

    private Product getProductFromJson(String jsonResponse) throws IOException {
        return mapper.readValue(jsonResponse, Product.class);
    }

}