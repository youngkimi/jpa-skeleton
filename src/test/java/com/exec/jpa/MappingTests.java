package com.exec.jpa;

import com.exec.jpa.entity.Member;
import com.exec.jpa.entity.Team;
import com.exec.jpa.service.MemberService;
import com.exec.jpa.service.TeamService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MappingTests {

    @Autowired
    MemberService memberService;

    @Autowired
    TeamService teamService;

    private List<Team> teams = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        for (int i = 1; i <= 3; i++) {

            System.out.printf("%d 번째 팀 생성 시작\n", i);
            Team team = teamService.createTeam(new Team(i, "team " + i, new ArrayList<>()));

            for (int j = 1; j <= i; j++) {
                System.out.printf("- %d 번째 팀원 생성 시작\n", j);
                Member member = new Member(j, "Member " + (i*j), null);
                member = memberService.createMember(member);
                teamService.addTeamMember(team, member);
            }

            teams.add(team);
        }
    }

    /* 실패 코드 : 팀이 비영속 상태에서 멤버에 팀을 mapping 했다. -> 팀을 먼저 영속화 시킬 것.
    @BeforeEach
    public void setUp() {

        for (int i = 1; i <= 3; i++) {

            System.out.printf("%d 번째 팀 생성 시작\n", i);
            Team team = new Team(i, "team " + i, new ArrayList<>());

            for (int j = 1; j <= i; j++) {
                System.out.printf("- %d 번째 팀원 생성 시작\n", j);
                Member member = new Member(j, "Member " + (i*j), null);
                member = memberService.createMember(member);
                teamService.addTeamMember(team, member);
            }

            teams.add(teamService.createTeam(team));
        }
    }

    실패 결과:
        필요:null
        실제:Team(id=1, name=team 1, members=[Member(id=1, name=Member 1, team=null)])
     */

    @AfterEach
    public void cleanUp() {
        memberService.cleanUp();
        teamService.cleanUp();
    }

    /*
        public class Team {
            ...

            public void joinTeam(Member member) {
            this.members.add(member);
            member.setTeam(this);
        }

        ->
     */

    @DisplayName("테스트 팀과 멤버의 생성 확인")
    @Test
    public void setUpAndCleanUpTest() {
        assertEquals(3, teams.size(), "테스트 팀은 세 가지");

        for (int i = 1; i <= 3; i++) {
            Team team = teams.get(i-1);
            List<Member> members = team.getMembers();
            assertEquals(i, members.size(), i + "번째 테스트 팀의 멤버 수는 " + i + " 명이다.");
            for (Member member : members) {
                assertEquals(member.getTeam(), team, "멤버의 팀은 " + i + "번째가 맞다.");
            }
        }
    }

}
