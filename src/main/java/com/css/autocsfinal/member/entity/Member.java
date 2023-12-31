package com.css.autocsfinal.member.entity;

import com.css.autocsfinal.main.entity.Todo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Entity
@Table(name = "TBL_MEMBER")
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR", // 엔티티에서 지정한 시퀀스이름
        sequenceName = "SEQ_MEMBER_NO", // 실제 데이터베이스에 있는 시퀀스 명
        initialValue = 1, allocationSize = 1
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Member {

    @Id
    @Column(name = "MEMBER_NO")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR"
    )
    private int no;

    @Column(name = "ID")
    private String id;

    @Column(name = "PWD")
    private String pwd;

    @Column(name = "STATE")
    private String state;

    @Column(name = "ROLE")
    private String role;

    @Override
    public String toString() {
        return new StringJoiner(", ", "{", "}")
                .add("\"no\" : \"" + no + "\"")
                .add("\"id\" : \"" + id + "\"")
                .add("\"pwd\" : \"" + pwd + "\"")
                .add("\"state\" :\"" + state.trim() + "\"")
                .add("\"role\" :\"" + role + "\"")
                .toString();
    }
}