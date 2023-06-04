package vending.machine.VendingMachine.controllers;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vending.machine.VendingMachine.config.security.JwtTokenUtil;
import vending.machine.VendingMachine.models.Product;
import vending.machine.VendingMachine.models.User;
import vending.machine.VendingMachine.services.ProductService;
import vending.machine.VendingMachine.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {

        Product savedProduct ;
        try {
            savedProduct = productService.saveProduct(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product,
            @RequestHeader("Authorization") String authorizationHeader
    ) {

        Optional<Product> existingProduct = productService.getProductById(id);
        if (existingProduct.isPresent()) {
            User authenticatedUser = userService.getAuthenticatedUser(authorizationHeader);

            if (authenticatedUser.getId().equals(existingProduct.get().getSeller().getId())) {
                product.setId(id);
                product.setSeller(existingProduct.get().getSeller());

                Product updatedProduct;
                try {
                    updatedProduct = productService.saveProduct(product);
                } catch (NotFoundException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                return ResponseEntity.ok(updatedProduct);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        Optional<Product> existingProduct = productService.getProductById(id);
        if (existingProduct.isPresent()) {
            User authenticatedUser = userService.getAuthenticatedUser(authorizationHeader);

            if (authenticatedUser.getId().equals(existingProduct.get().getSeller().getId())) {
                productService.deleteProduct(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
