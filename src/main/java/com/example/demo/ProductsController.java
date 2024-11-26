package com.example.demo;

import ch.qos.logback.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@RestController("/products")
public class ProductsController {

    public static final List<Product> PRODUCTS = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody final Product productToAdd) {
        if (StringUtil.isNullOrEmpty(productToAdd.getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (StringUtil.isNullOrEmpty(productToAdd.getName())) {
            return ResponseEntity.badRequest().build();
        }
        if (isNull(productToAdd.getPrice()) || productToAdd.getPrice() < 0) {
            return ResponseEntity.badRequest().build();
        }
        PRODUCTS.add(productToAdd);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Product> getProducts() {
        return PRODUCTS;
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> editProduct(@PathVariable String productId, @RequestBody final Product updateBody) {
        final Optional<Product> product = PRODUCTS.stream()
                .filter(c -> c.getId().equals(productId))
                .findFirst();
        if (product.isPresent()) {
            final Product p = product.get();
            p.setPrice(updateBody.getPrice());
            p.setName(updateBody.getName());
            p.setDescription(updateBody.getDescription());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable final String productId) {
        final Optional<Product> product = PRODUCTS.stream().filter(c -> c.getId().equals(productId))
                .findFirst();

        if (product.isPresent()) {
            PRODUCTS.remove(product.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
