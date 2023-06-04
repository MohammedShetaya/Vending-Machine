package vending.machine.VendingMachine.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vending.machine.VendingMachine.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}
