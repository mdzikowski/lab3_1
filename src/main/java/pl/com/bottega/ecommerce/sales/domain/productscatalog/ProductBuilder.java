package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {

    private String name;
    private Id id;
    private Money price;
    private ProductType type;

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withType(ProductType type) {
        this.type = type;
        return this;
    }

    public ProductBuilder withPrice(Money price) {
        this.price = price;
        return this;
    }

    public ProductBuilder withId(Id id) {
        this.id = id;
        return this;
    }

    public Product build() {
        return new Product(id, price, name, type);
    }

    public Product buildNotAvailable() {
        Product p = new Product(id, price, name, type);
        p.markAsRemoved();
        return p;
    }
}