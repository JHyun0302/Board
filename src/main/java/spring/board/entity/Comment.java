package spring.board.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id", "content"})
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @NotNull
    @Column(length = 500)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @NotNull
    private Board board;

    //==연관 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getComments().add(this);
    }

    public void setBoard(Board board) {
        this.board = board;
        board.getComments().add(this);
    }

    public Comment(String content, User user, Board board) {
        this.content = content;
        this.user = user;
        this.board = board;
    }
}
