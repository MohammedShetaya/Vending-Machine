package vending.machine.VendingMachine.services;

import java.util.ArrayList;
import java.util.List;

public class ChangeCalculatorService {

    static int[] coins = {5, 10, 20, 50, 100};

    public static List<Integer> calculateChange(double amount) {
        List<Integer> change = new ArrayList<>();
        int i = coins.length - 1;
        while (amount >= 5) {
            if (coins[i] <= amount) {
                amount -= coins[i];
                change.add(coins[i]);
            } else {
                i--;
            }
        }
        return change;
    }
}
