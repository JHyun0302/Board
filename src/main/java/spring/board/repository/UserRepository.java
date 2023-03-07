package spring.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.board.dto.UserDto;
import spring.board.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select new spring.board.dto.UserDto(u.username, u.email, b.content) " +
            "from User u join u.boards b")
    List<UserDto> findUserDto();
}
