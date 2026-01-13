package Practice.service;

import Practice.model.dto.request.LoginRequest;
import Practice.model.dto.request.RegisterRequest;
import Practice.model.dto.response.AuthResponse;
import Practice.model.dto.response.UserResponse;
import Practice.model.entity.UserEntity;
import Practice.repository.UserRepository;
import Practice.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public UserResponse register(RegisterRequest request) {
        String email = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email không được để trống!");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        String phone = request.getPhone();
        if (phone != null) {
            phone = phone.trim();
            if (phone.isBlank()) phone = null;
            if (phone != null && userRepository.existsByPhone(phone)) {
                throw new RuntimeException("Số điện thoại đã được sử dụng!");
            }
        }

        UserEntity user = new UserEntity();
        user.setFullname(request.getFullname());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(phone);

        Set<String> roles = new HashSet<>();
        roles.add("USER");
        user.setRoles(roles);

        UserEntity savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email hoặc mật khẩu không đúng");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, mapToUserResponse(user));
    }


    private UserResponse mapToUserResponse(UserEntity user) {
        return new UserResponse(
                String.valueOf(user.getId()),
                user.getFullname(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles(),
                user.getCreatedAt()
        );
    }
}
