package com.bookflex.backend.service;

import com.bookflex.backend.exception.UserNotFoundException;
import com.bookflex.backend.utils.JwtTokenProvider;
import com.bookflex.backend.dto.LoginDto;
import com.bookflex.backend.dto.UserDto;
import com.bookflex.backend.exception.DuplicatedUsernameException;
import com.bookflex.backend.exception.LoginFailedException;
import com.bookflex.backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto join(String username, String password) {

        UserDto userDto = new UserDto();

        String pass = passwordEncoder.encode(password);

        userDto.setPassword(pass);
        userDto.setUsername(username);
        userMapper.save(userDto);

        System.out.println(userMapper.findUserByUsername(userDto.getUsername()).get());

        return userMapper.findUserByUsername(userDto.getUsername()).get();
    }
    public String login(String username, String password) {
        UserDto userDto = userMapper.findUserByUsername(username)
                .orElseThrow(() -> new LoginFailedException("잘못된 아이디입니다"));

        if (!passwordEncoder.matches(password, userDto.getPassword())) {
            throw new LoginFailedException("잘못된 비밀번호입니다");
        }

        return jwtTokenProvider.createToken(userDto.getUserId(), Collections.singletonList(userDto.getRole()));
    }

    public UserDto findByUserId(Long userId) {
        return userMapper.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("없는 유저입니다."));
    }
}
