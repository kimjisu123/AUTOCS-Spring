package com.css.autocsfinal.schedule.entity;

import com.css.autocsfinal.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="TBL_SCHEDULE")
@SequenceGenerator(
        name = "SCHEDULE_NO",
        sequenceName = "SEQ_SCHEDULE_NO",
        initialValue = 1, allocationSize = 1
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @Column(name = "SCHEDULE_CODE")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SCHEDULE_NO"
    )
    private int scheduleCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PLACE")
    private String place;

    @Column(name ="CONTENT")
    private String content;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REF_MEMBER_NO")
    private Member member;

    @OneToMany(mappedBy = "schedule")
    private List<Attendee> attendeeList;

    @OneToMany(mappedBy = "schedule")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "schedule")
    private List<OutsideAttendess> outsideAttendessList;

}
