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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ConvenientMethodTests {

    @Autowired
    MemberService memberService;

    @Autowired
    TeamService teamService;

    private List<Team> teams = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        for (int i = 1; i <= 2; i++) {
            System.out.printf("%d 번째 팀 생성 시작\n", i);
            Team team = teamService.createTeam(new Team(i, "team " + i, new ArrayList<>()));

            for (int j = 1; j <= i; j++) {
                System.out.printf("- %d 번째 팀원 생성 시작\n", j);
                Member member = new Member(j, "Member " + (i*j), null);
                member = memberService.createMember(member);
                team.joinTeam(member);
            }

            teams.add(team);
        }
    }

    @AfterEach
    public void cleanUp() {
        memberService.cleanUp();
        teamService.cleanUp();
    }

    @Test
    @DisplayName("한 번에 하나에 팀에만 가입할 수 있다.")
    public  void memberCanJoinIntoOnlyOneTeamTest() {

        Member member = memberService.createMember(new Member(null, "Tester", null));

        Team team1 = teams.get(0);
        Team team2 = teams.get(1);

        team1.joinTeam(member);

        assertTrue(team1.getMembers().contains(member), "Member joined team1");

        team2.joinTeam(member);

        assertFalse(team1.getMembers().contains(member), "Member exiled team1");
        assertTrue(team2.getMembers().contains(member), "Member joined team2");
    }

}
