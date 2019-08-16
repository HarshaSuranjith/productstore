package product.store.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "PRODUCT", uniqueConstraints = @UniqueConstraint(name = "uk_product_code", columnNames = {"product_code"}))
public class Product extends BaseEntity {

    @Column(name = "name", length = 1024, nullable = false)
    private String name;

    @Column(name = "product_code", length = 128, nullable = false, unique = true)
    private String productCode;

    @Column(name = "description", length = 4000)
    private String description;

    public Product() {
    }

    public Product(String name, String productCode, String description) {
        this.name = name;
        this.productCode = productCode;
        this.description = description;
    }

    public static Product of(String name, String productCode, String description) {
        return new Product(name, productCode, description);
    }

    public static Product of(Long id, String name, String productCode, String description) {
        Product product = Product.of(name, productCode, description);
        product.setId(id);
        return product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isNew() {
        return getId() == null;
    }

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isValid() {
        // validation logic including specific business rules
        return name != null && !name.isEmpty() &&
                productCode != null && !productCode.isEmpty() && productCode.startsWith("PRO_");
    }

}
