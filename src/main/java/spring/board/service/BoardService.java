package spring.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.board.entity.Board;
import spring.board.repository.BoardRepository;
import spring.board.service.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Board join(Board board) {
        return boardRepository.save(board);
    }

    public Board findOne(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow((() -> new ResourceNotFoundException("Board not Found")));
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }
}
