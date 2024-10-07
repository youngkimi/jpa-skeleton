package com.exec.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity @Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();

//    public void joinTeam(Member member) {
//        this.members.add(member);
//        member.setTeam(this);
//    }

    public void joinTeam(Member member) {
        this.members.add(member);
        if (member.getTeam() != null) {
            member.getTeam().getMembers().remove(member);
        }
        member.setTeam(this);
    }
}
