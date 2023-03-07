package spring.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.board.entity.Comment;
import spring.board.repository.CommentRepository;
import spring.board.service.exception.ResourceNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public Comment join(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findOne(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment not found"));
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
}
