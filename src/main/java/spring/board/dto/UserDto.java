package spring.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String username;
    private String email;

    private String content;

    @QueryProjection
    public UserDto(String username, String email, String content) {
        this.username = username;
        this.email = email;
        this.content = content;
    }
}
