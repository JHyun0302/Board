package spring.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {
    private String username;
    private String email;

    @QueryProjection
    public MemberDto(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
