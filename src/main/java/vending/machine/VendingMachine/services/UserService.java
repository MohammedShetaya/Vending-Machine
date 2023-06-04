package vending.machine.VendingMachine.services;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vending.machine.VendingMachine.config.security.JwtTokenUtil;
import vending.machine.VendingMachine.config.security.PasswordEncoderConfig;
import vending.machine.VendingMachine.models.Product;
import vending.machine.VendingMachine.models.User;
import vending.machine.VendingMachine.models.responses.BuyResponse;
import vending.machine.VendingMachine.models.responses.ResetResponse;
import vending.machine.VendingMachine.repos.ProductRepository;
import vending.machine.VendingMachine.repos.UserRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoderConfig config;
    public void deposit(Long userId, int coin) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        // check the coin category
        if (coin != 5 && coin != 10 && coin != 20 && coin != 50 && coin != 100) {
            throw new IllegalArgumentException("Invalid coin");
        }

        double currentDeposit = user.getDeposit();
        double newDeposit = currentDeposit + coin;
        user.setDeposit(newDeposit);
        userRepository.save(user);
    }

    public ResetResponse reset(Long userId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        // convert deposit into coins
        List<Integer> coins = ChangeCalculatorService.calculateChange(user.getDeposit());
        user.setDeposit(0);
        userRepository.save(user);

        return ResetResponse.builder()
                .coins(coins)
                .build();
    }

    public BuyResponse buyProduct(Long userId, Long productId, int amount) throws IllegalAccessException, NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));

        // check if the product stock is enough
        if (product.getAmountAvailable() < amount) {
            throw new IllegalAccessException("Not enough stock");
        }

        // check if the user has enough deposit
        if (user.getDeposit() < product.getCost() * amount) {
            throw new IllegalAccessException("Not enough deposit");
        }
        // decrease the product stock
        int currentStock = product.getAmountAvailable();
        product.setAmountAvailable(currentStock - amount);
        productRepository.save(product);

        // decrease the user deposit
        double currentDeposit = user.getDeposit();
        user.setDeposit(0);
        userRepository.save(user);

        List<Integer> change = ChangeCalculatorService.calculateChange(currentDeposit - product.getCost() * amount);

        return BuyResponse.builder()
                .productName(product.getProductName())
                .total(product.getCost() * amount)
                .change(change)
                .build();
    }


    public User createUser(User user) {
        String encodedPassword = config.passwordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User getAuthenticatedUser(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        Long id = jwtTokenUtil.getIdFromToken(token);
        return getUserById(id);

    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) throws NotFoundException {
        if (user.getPassword() != null) {
            User oldUser = userRepository.findById(user.getId()).orElseThrow(() -> new NotFoundException("User not found"));
            user.setPassword(oldUser.getPassword());
        }
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

}
