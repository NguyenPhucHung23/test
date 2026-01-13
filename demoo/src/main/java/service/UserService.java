package Practice.service;

import Practice.model.dto.request.ChangePasswordRequest;
import Practice.model.dto.request.UpdateUserRequest;
import Practice.model.dto.response.UserResponse;
import Practice.model.entity.UserEntity;
import Practice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse getUserProfile(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        return mapToUserResponse(user);
    }

    public UserResponse updateUserProfile(String email, UpdateUserRequest request) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        user.setFullname(request.getFullname());
        user.setPhone(request.getPhone());

        UserEntity updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
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
