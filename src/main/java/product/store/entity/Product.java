package product.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PRODUCT", uniqueConstraints = @UniqueConstraint(name = "uk_product_serial_number", columnNames = {"serial_number"}))
public class Product extends BaseEntity {

    @Column(name = "name", length = 1024, nullable = false)
    private String name;

    @Column(name = "serial_number", length = 128, nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "description", length = 4000)
    private String description;

    public Product() {

    }

    public Product(String name, String serialNumber, String description) {
        this.name = name;
        this.serialNumber = serialNumber;
        this.description = description;
    }

    public static Product of(String name, String serialNumber, String description) {
        return new Product(name, serialNumber, description);
    }

    public static Product of(Long id, String name, String serialNumber, String description) {
        Product product = Product.of(name,serialNumber, description);
        product.setId(id);
        return product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
