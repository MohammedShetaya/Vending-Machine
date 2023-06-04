package vending.machine.VendingMachine.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyResponse {
    private String productName;
    private Double total;
    private List<Integer> change ;
}
