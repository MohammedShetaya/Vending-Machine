package vending.machine.VendingMachine.services;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vending.machine.VendingMachine.models.Product;
import vending.machine.VendingMachine.models.User;
import vending.machine.VendingMachine.repos.ProductRepository;
import vending.machine.VendingMachine.repos.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) throws NotFoundException {
        // get the user from database
        User seller = userRepository.findById(product.getSeller().getId()).orElseThrow(() -> new NotFoundException("Seller not found"));

        product.setSeller(seller);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}
