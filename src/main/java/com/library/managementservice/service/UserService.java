package com.library.managementservice.service;

import com.library.managementservice.api.dto.CreateUserRequest;
import com.library.managementservice.api.dto.CreateUserResponse;
import com.library.managementservice.entity.Member;
import com.library.managementservice.entity.User;
import com.library.managementservice.repository.MemberRepository;
import com.library.managementservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private static final Logger log =
            LoggerFactory.getLogger(UserService.class);


    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest req) {

        log.info("Create user requested, username={}, role={}",
                req.username(), req.role());

        if (userRepository.findByUsername(req.username()).isPresent()) {
            log.warn("Create user failed: username already exists, username={}",
                    req.username());
            throw new IllegalStateException("Username already exists");
        }

        User user = new User();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(req.role());

        Member member = null;
        if ("MEMBER".equals(req.role())) {

            if (req.memberId() == null) {
                log.warn("Create user failed: memberId missing for MEMBER role, username={}",
                        req.username());
                throw new IllegalArgumentException("memberId is required for MEMBER role");
            }

            member = memberRepository.findById(req.memberId())
                    .orElseThrow(() -> {
                        log.warn("Create user failed: member not found, memberId={}, username={}",
                                req.memberId(), req.username());
                        return new IllegalArgumentException("Member not found");
                    });

            user.setMember(member);
        }

        userRepository.save(user);

        log.info("User created successfully, userId={}, username={}, role={}",
                user.getId(), user.getUsername(), user.getRole());

        return new CreateUserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                member != null ? member.getId() : null,
                null
        );
    }

    public User getCurrentUser(Authentication authentication) {

        String username = authentication.getName();
        log.debug("Get current user from authentication, username={}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Authenticated user not found in database, username={}", username);
                    return new IllegalStateException("Authenticated user not found");
                });
    }


    public Member getCurrentMember(Authentication authentication) {

        User user = getCurrentUser(authentication);

        if (!"MEMBER".equals(user.getRole())) {
            log.warn("Current user is not a MEMBER, userId={}, role={}",
                    user.getId(), user.getRole());
            throw new IllegalStateException("Current user is not a member");
        }

        if (user.getMember() == null) {
            log.error("Member mapping not found for user, userId={}", user.getId());
            throw new IllegalStateException("Member mapping not found for user");
        }

        log.debug("Current member resolved, memberId={}, userId={}",
                user.getMember().getId(), user.getId());

        return user.getMember();
    }



    @Transactional
    public CreateUserResponse createUserForMember(Member member) {

        log.info("Auto-create user for member, memberId={}, email={}",
                member.getId(), member.getEmail());

        String generatedPassword = generateRandomPassword();

        CreateUserRequest req = new CreateUserRequest(
                member.getEmail(),
                generatedPassword,
                "MEMBER",
                member.getId()
        );

        CreateUserResponse response = createUser(req);

        log.info("User auto-created for member, memberId={}, userId={}",
                member.getId(), response.userId());

        return new CreateUserResponse(
                response.userId(),
                response.username(),
                response.role(),
                response.memberId(),
                generatedPassword // RETURN ONLY, NEVER LOG
        );
    }


    private String generateRandomPassword() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10);
    }


}
