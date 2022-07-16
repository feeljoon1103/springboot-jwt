package com.bookflex.backend.controller;

import com.bookflex.backend.dto.UserDto;
import com.bookflex.backend.dto.response.BaseResponse;
import com.bookflex.backend.dto.response.SingleDataResponse;
import com.bookflex.backend.exception.UserNotFoundException;
import com.bookflex.backend.service.ResponseService;
import com.bookflex.backend.service.UserService;
import com.bookflex.backend.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ResponseService responseService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/user/test")
    public Map userResponseTest() {
        Map<String, String> result = new HashMap<>();
        result.put("result","user ok");
        return result;
    }

    @PostMapping("/admin/test")
    public Map adminResponseTest() {
        Map<String, String> result = new HashMap<>();
        result.put("result","admin ok");
        return result;
    }

    @GetMapping("/user/users")
    public ResponseEntity findUserByUsername(final Authentication authentication) {
        ResponseEntity responseEntity = null;
        try {
            Long userId = ((UserDto) authentication.getPrincipal()).getUserId();
            UserDto findUser = userService.findByUserId(userId);

            SingleDataResponse<UserDto> response = responseService.getSingleDataResponse(true, "조회 성공", findUser);

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserNotFoundException exception) {
            logger.debug(exception.getMessage());
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return responseEntity;
    }

}
