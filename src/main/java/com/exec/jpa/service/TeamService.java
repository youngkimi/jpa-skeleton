package com.exec.jpa.service;

import com.exec.jpa.entity.Member;
import com.exec.jpa.entity.Team;
import com.exec.jpa.repository.MemberRepository;
import com.exec.jpa.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class TeamService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    public List<Member> findMembersByTeamId(Integer id) {
        return teamRepository.findById(id)
                .orElseThrow()
                .getMembers();
    }

    @Transactional
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    @Transactional
    public void addTeamMember(Team team, Member member) {
        team.getMembers().add(member);
        member.setTeam(team);
    }

    @Transactional
    public void cleanUp() {
        teamRepository.deleteAll();
    }

}
