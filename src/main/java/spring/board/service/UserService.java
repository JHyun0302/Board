package spring.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.board.entity.User;
import spring.board.repository.UserRepository;
import spring.board.service.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User join(User user) {
        return userRepository.save(user);
    }

    public User findOne(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
