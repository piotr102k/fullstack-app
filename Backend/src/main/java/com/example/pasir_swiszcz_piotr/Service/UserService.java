package com.example.pasir_swiszcz_piotr.Service;

import com.example.pasir_swiszcz_piotr.DTO.LoginDTO;
import com.example.pasir_swiszcz_piotr.DTO.UserDTO;
import com.example.pasir_swiszcz_piotr.Security.JwtUtil;
import com.example.pasir_swiszcz_piotr.model.User;
import com.example.pasir_swiszcz_piotr.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(UserDTO dto){
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setUsername(dto.getUsername());
        return userRepository.save(user);
    }

    public String login(LoginDTO dto){
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        if(!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("nieprawidłowe dane do logowania");
        }
        return jwtUtil.generateToken(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found " +email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),new ArrayList<>());
    }
}
