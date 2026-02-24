package com.metacoding.springv2.user;

import com.metacoding.springv2._core.handler.ex.Exception404;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse.DetailDTO findById(Integer id) {
        // rule1 반영: 가입된 유저 정보 조회 기능
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception404("User not found"));

        return new UserResponse.DetailDTO(user);
    }
}
