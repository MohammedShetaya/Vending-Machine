package vending.machine.VendingMachine.controllers;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import vending.machine.VendingMachine.config.security.JwtTokenUtil;
import vending.machine.VendingMachine.models.requests.BuyRequest;
import vending.machine.VendingMachine.models.requests.DepositRequest;
import vending.machine.VendingMachine.models.User;
import vending.machine.VendingMachine.models.responses.BuyResponse;
import vending.machine.VendingMachine.models.responses.ErrorResponse;
import vending.machine.VendingMachine.models.responses.ResetResponse;
import vending.machine.VendingMachine.services.UserService;
import vending.machine.VendingMachine.models.responses.ResponseMessage;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/buy")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<BuyResponse> buyProduct(
            @RequestBody BuyRequest buyRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        User authenticatedUser = userService.getAuthenticatedUser(authorizationHeader);

        // check if the user exists and is owner of the resource
        if (authenticatedUser == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        BuyResponse buyResponse;

        try {
            buyResponse = userService.buyProduct(
                    authenticatedUser.getId(),
                    buyRequest.getProductId(),
                    buyRequest.getAmount()
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(buyResponse);
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<ResponseMessage> increaseDeposit(
            @RequestBody DepositRequest depositRequest,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        User authenticatedUser = userService.getAuthenticatedUser(authorizationHeader);

        // check if the user exists and is owner of the resource
        if (authenticatedUser == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            userService.deposit(authenticatedUser.getId(), depositRequest.getCoin());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(new ResponseMessage("Deposit increased successfully"));
    }

    @PostMapping("/reset")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<ResetResponse> resetDeposit(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        User authenticatedUser = userService.getAuthenticatedUser(authorizationHeader);
        // check if the user exists and is owner of the resource
        if (authenticatedUser == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ResetResponse resetResponse;
        try {
            resetResponse = userService.reset(authenticatedUser.getId());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(resetResponse);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {

        User createdUser;
        try {
            createdUser = userService.createUser(user);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        User authenticatedUser = userService.getAuthenticatedUser(authorizationHeader);
        // check if the user exists and is owner of the resource
        if (authenticatedUser == null ||
                !authenticatedUser.getId().equals(id)
        ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User foundUser = userService.getUserById(id);
        return ResponseEntity.ok(foundUser);
    }

    @GetMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        User authenticatedUser = userService.getAuthenticatedUser(authorizationHeader);

        // check if the user exists and is owner of the resource
        if (authenticatedUser == null ||
                !authenticatedUser.getId().equals(id)
        ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        user.setId(id);

        User updatedUser = null;
        try {
            updatedUser = userService.updateUser(user);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        User authenticatedUser = userService.getAuthenticatedUser(authorizationHeader);

        // check if the user exists and is owner of the resource
        if (authenticatedUser == null ||
                !authenticatedUser.getId().equals(id)
        ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }


}
