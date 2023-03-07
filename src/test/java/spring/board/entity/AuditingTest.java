package spring.board.entity;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import spring.board.dto.MemberDto;
import spring.board.dto.QMemberDto;
import spring.board.dto.UserDto;
import spring.board.repository.BoardRepository;
import spring.board.repository.CommentRepository;
import spring.board.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static spring.board.entity.QUser.user;

@SpringBootTest
@Transactional
@Rollback(false)
class AuditingTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CommentRepository commentRepository;

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        User user1 = new User("user1", "password1", "user1@gmail.com");
        User user2 = new User("user2", "password2", "user2@gmail.com");
//        User user3 = new User("user3", "password3", "user3@gmail.com");
//        User user4 = new User("user4", "password4", "user4@gmail.com");
        em.persist(user1);
        em.persist(user2);
//        em.persist(user3);
//        em.persist(user4);

        Board boardA = new Board("titleA", "contentA", user1);
        Board boardB = new Board("titleB", "contentB", user1);

        Board boardC = new Board("titleC", "contentC", user2);
        Board boardD = new Board("titleD", "contentD", user2);
        em.persist(boardA);
        em.persist(boardB);
        em.persist(boardC);
        em.persist(boardD);
    }

    @Test
    void userAuditing() throws Exception {
        //given
        User user = new User("userA", "userA!", "userA@gmail.com");
        User saveUser = userRepository.save(user);//@PrePersist

//        Thread.sleep(100);
//        user.setUsername("userB"); //@PreUpdate

        em.flush();
        em.clear();

        //when
        User findUser = userRepository.findById(user.getId()).get();

        System.out.println("findUser.getUsername() = " + findUser.getUsername());
        //then
        System.out.println("findUser.getCreatedDate() = " + findUser.getCreatedDate());
        System.out.println("findUser.getModifiedDate() = " + findUser.getModifiedDate());

        assertThat(saveUser.getCreatedDate()).isNotNull();
        assertThat(saveUser.getModifiedDate()).isNotNull();
    }

    @Test
    void boardAuditing() {
        //given
        User user = new User("userA", "userA!", "userA@gmail.com");
        User saveUser = userRepository.save(user);//@PrePersist

        Board board = new Board("title", "content", user);
        Board saveBoard = boardRepository.save(board);

        em.flush();
        em.clear();

        //when
        User findUser = userRepository.findById(saveUser.getId()).get();
        Board findBoard = boardRepository.findById(saveBoard.getId()).get();

        //then
        System.out.println("findBoard.getCreatedBy() = " + findBoard.getCreatedBy());
        System.out.println("findBoard.getUpdatedBy() = " + findBoard.getUpdatedBy());
        System.out.println("findBoard.getCreatedDate() = " + findBoard.getCreatedDate());
        System.out.println("findBoard.getModifiedDate() = " + findBoard.getModifiedDate());
    }

    @Test
    void commentAuditing() {
        //given
        User user = new User("userA", "userA!", "userA@gmail.com");
        User saveUser = userRepository.save(user);//@PrePersist

        Board board = new Board("title", "content", user);
        Board saveBoard = boardRepository.save(board);

        Comment content = new Comment("content", user, board);
        Comment saveComment = commentRepository.save(content);

        em.flush();
        em.clear();

        //when
        User findUser = userRepository.findById(saveUser.getId()).get();
        Board findBoard = boardRepository.findById(saveBoard.getId()).get();
        Comment comment = commentRepository.findById(saveComment.getId()).get();

        //then
        System.out.println("comment.getCreatedBy() = " + comment.getCreatedBy());
        System.out.println("comment.getUpdatedBy() = " + comment.getUpdatedBy());
        System.out.println("comment.getCreatedDate() = " + comment.getCreatedDate());
        System.out.println("comment.getModifiedDate() = " + comment.getModifiedDate());
    }

    @Test
    @DisplayName("Dto + JPQL 이용한 set")
    void updateAuditing() {
        //given
        User userA = new User("userA", "userA!", "userA@gmail.com");
        userRepository.save(userA);

        Board board = new Board("title", "content", null);
        board.setUser(userA);
        boardRepository.save(board);

        //when
        List<UserDto> userDto = userRepository.findUserDto();

        //then
        for (UserDto dto : userDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findDtoBySetter() {
        List<Tuple> result = queryFactory
                .select(Projections.bean(MemberDto.class),
                        user.username,
                        user.email)
                .from(user)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void findDtoField() {
        List<Tuple> result = queryFactory
                .select(Projections.fields(MemberDto.class),
                        user.username,
                        user.email)
                .from(user)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void findDtoByQueryProjection() {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(user.username, user.email))
                .from(user)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }
}