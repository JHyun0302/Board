package spring.board.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.board.entity.User;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    EntityManager em;

    User userA;
    User userB;

    @BeforeEach
    void beforeEach() {
        userA = new User("userA", "userA!", "userA@gmail.com");
        userB = new User("userB", "userB!", "userB@gmail.com");
    }

    @Test
    @DisplayName("유저 생성 성공")
    void save() {
        User saveUser = userService.join(userA);

        checkUser(saveUser, userA);
    }

    @Test
    @DisplayName("유저 단일 조회")
    void find() {
        //given
        User saveUser = userService.join(userA);
        em.flush();
        em.clear();

        //when
        Long userId = saveUser.getId();
        User findUser = userService.findOne(userId);

        //then
        checkUser(findUser, saveUser);
    }

    @Test
    @DisplayName("유저 전체 조회")
    public void findAll() throws Exception {
        //given
        userService.join(userA);
        userService.join(userB);

        //when
        List<User> users = userService.findAll();

        //then
        assertThat(users).contains(userA, userB);
    }

    private void checkUser(User user1, User user2) {
        assertThat(user1.getUsername()).isEqualTo(user2.getUsername());
        assertThat(user1.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());

        assertThat(user1.getCreatedDate()).isEqualTo(user2.getCreatedDate());
        assertThat(user1.getModifiedDate()).isEqualTo(user2.getModifiedDate());
    }
}
