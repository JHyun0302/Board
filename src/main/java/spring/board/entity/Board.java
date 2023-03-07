package spring.board.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "title", "content"})
public class Board extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @NotNull(message = "제목은 null이면 안됩니다.")
    private String title;
    @Column(columnDefinition = "TEXT") // 원하는 컬럼 타입으로 데이터 추출
    @NotNull(message = "컨텐츠는 null이면 안됩니다.")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @NotNull(message = "유저가 없으면 안됩니다.")
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    //==연관 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getBoards().add(this);
    }

    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
