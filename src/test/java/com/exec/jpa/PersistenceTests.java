package com.exec.jpa;

import com.exec.jpa.entity.Member;
import com.exec.jpa.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersistenceTests {

    @Autowired
    MemberService memberService;

    @DisplayName("준영속 상태에서의 수정 확인")
    @Test
    public void testDetachedEntities() {
        // 생성 : 현재 비영속 상태.
        Member createMember = new Member(1, "John Doe", null);

        // 저장 : 내부에서 영속 후 준영속 상태로 전환.
        createMember = memberService.createMember(createMember);

        // 수정 : 준영속 상태에서 수정.
        createMember.setName("Jane Doe");

        Member foundMember = memberService.findMemberById(createMember.getId());

        // 준영속 상태의 수정은 반영이
        assertEquals("Jane Doe", createMember.getName(), "준영속 상태에서 수정 엔티티. 인스턴스는 수정된다.");
        assertEquals("John Doe", foundMember.getName(), "준영속 상태에서 수정 엔티티. 데이터베이스는 반영되지 않았다.");

        Member mergeMember = memberService.mergeMember(createMember);

        assertEquals("Jane Doe", mergeMember.getName(), "준영속 상태에서 수정 후 트랜잭션. 반영된다.");

        assertNotSame(createMember, mergeMember, "처음 생성한 엔티티와 준영속 상태에서 수정 후 저장한 엔티티는 다르다.");
    }
}
