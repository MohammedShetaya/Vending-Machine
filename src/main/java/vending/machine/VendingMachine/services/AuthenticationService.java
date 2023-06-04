package vending.machine.VendingMachine.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vending.machine.VendingMachine.config.security.JwtTokenUtil;
import vending.machine.VendingMachine.models.requests.LoginRequest;
import vending.machine.VendingMachine.models.responses.TokenResponse;
import vending.machine.VendingMachine.repos.UserRepository;

import vending.machine.VendingMachine.models.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public TokenResponse login(LoginRequest request) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        authenticationManager.authenticate(authentication);

        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var token = jwtTokenUtil.generateToken(user);
        return  TokenResponse.builder()
                .token(token)
                .build();
    }
}
