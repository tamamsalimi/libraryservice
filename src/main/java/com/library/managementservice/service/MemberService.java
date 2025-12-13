package com.library.managementservice.service;

import com.library.managementservice.api.dto.CreateMemberRequest;
import com.library.managementservice.api.dto.MemberResponse;
import com.library.managementservice.entity.Member;
import com.library.managementservice.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public MemberResponse create(CreateMemberRequest request) {
        Member member = new Member(
                request.name(),
                request.email()
        );
        if (repository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already exists");
        }
        repository.save(member);
        return toResponse(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse getById(Long id) {
        Member member = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return toResponse(member);
    }

    private MemberResponse toResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }
}
