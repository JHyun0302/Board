package spring.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring.board.entity.Board;
import spring.board.entity.User;
import spring.board.repository.UserRepository;
import spring.board.service.exception.ResourceNotFoundException;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
//@Rollback(false)
class BoardServiceTest {
    @Autowired
    BoardService boardService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    Board board1;
    Board board2;

    @BeforeEach
    void Init() {
        User userA = new User("userA", "userA!", "userA@gmail.com");
        User userB = new User("userB", "userB!", "userB@gmail.com");
        userRepository.save(userA);
        userRepository.save(userB);

        board1 = new Board("title1", "content1", userA);
        board2 = new Board("title2", "content2", userB);
        boardService.join(board1);
        boardService.join(board2);

    }

    @Test
    @DisplayName("게시글 생성 성공")
    void save() {
        Board saveBoard = boardService.join(board1);

        checkBoard(saveBoard, board1);
    }

    @Test
    @DisplayName("게시글 단일 조회")
    void find() {
        Board findBoard = boardService.findOne(board1.getId());

        checkBoard(findBoard, board1);
    }

    @Test
    @DisplayName("게시글 조회 실패")
    void findFail() {
        Long boardId = 1L;
//        boardService.findOne(boardId);
        assertThrows(ResourceNotFoundException.class, () -> {
            boardService.findOne(boardId);
        });
    }

    @Test
    @DisplayName("게시글 전체 조회")
    void findAll() {
        List<Board> findBoards = boardService.findAll();

        Assertions.assertThat(findBoards).contains(board1, board2);
    }

    private void checkBoard(Board board1, Board board2) {
        assertThat(board1.getTitle()).isEqualTo(board2.getTitle());
        assertThat(board1.getContent()).isEqualTo(board2.getContent());

        assertThat(board1.getCreatedDate()).isEqualTo(board2.getCreatedDate());
        assertThat(board1.getModifiedDate()).isEqualTo(board2.getModifiedDate());
    }
}
