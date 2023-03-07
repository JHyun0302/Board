package spring.board.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import spring.board.repository.BoardRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class BoardTest {
    @Autowired
    BoardRepository boardRepository;
    @PersistenceContext
    EntityManager em;


    User user;


    @BeforeEach
    void userInit() {
        user = new User("test", "test!", "test@gail.com");
    }

    @Test
    @DisplayName("게시판 생성 성공")
    void success() {
        Board board = new Board("title", "content", user);
        boardRepository.save(board);

        Board findBoard = boardRepository.findById(board.getId()).get();
        assertThat(findBoard).isEqualTo(board);
        assertThat(findBoard.getTitle()).isEqualTo("title");
        assertThat(findBoard.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("게시판 제목 없으면 실패")
    void noTitle() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        //given
        Board board = new Board(null, "content", user);


        Set<ConstraintViolation<Board>> violations = validator.validate(board);
        for (ConstraintViolation<Board> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation = " + violation.getMessage());
        }
    }

    @Test
    @DisplayName("게시판 컨텐츠 없으면 실패")
    void noContent() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        //given
        Board board = new Board("title", null, user);


        Set<ConstraintViolation<Board>> violations = validator.validate(board);
        for (ConstraintViolation<Board> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation = " + violation.getMessage());
        }
    }

}