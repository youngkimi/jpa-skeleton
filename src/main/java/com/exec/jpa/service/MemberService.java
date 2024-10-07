package com.exec.jpa.service;

import com.exec.jpa.entity.Member;
import com.exec.jpa.repository.MemberRepository;
import com.exec.jpa.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberById(Integer id) {
        return memberRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Member createMember(Member member) {
        memberRepository.save(member);
        return member;
    }

    @Transactional
    public Member mergeMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public void cleanUp() {
        memberRepository.deleteAll();
    }

}
