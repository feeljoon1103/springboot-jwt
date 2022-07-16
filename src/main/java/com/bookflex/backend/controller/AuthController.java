package com.bookflex.backend.controller;

import com.bookflex.backend.dto.UserDto;
import com.bookflex.backend.dto.response.BaseResponse;
import com.bookflex.backend.dto.response.SingleDataResponse;
import com.bookflex.backend.exception.DuplicatedUsernameException;
import com.bookflex.backend.exception.LoginFailedException;
import com.bookflex.backend.exception.UserNotFoundException;
import com.bookflex.backend.service.ResponseService;
import com.bookflex.backend.service.UserService;
import com.bookflex.backend.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final ResponseService responseService;
    private JwtTokenProvider jwtTokenProvider;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    /*public ResponseEntity join(HttpServletRequest request) {*/
    public ResponseEntity join(@RequestBody @Validated UserDto userDto) {
        ResponseEntity responseEntity = null;

        /*String username = request.getParameter("username");
        String password = request.getParameter("password");*/

        String username = userDto.getUsername();
        String password = userDto.getPassword();
        try {
            UserDto savedUser = userService.join(username, password);
            SingleDataResponse<UserDto> response = responseService.getSingleDataResponse(true, "회원가입 성공", savedUser);

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DuplicatedUsernameException exception) {
            logger.debug(exception.getMessage());
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return responseEntity;
    }

    @PostMapping("/login")
    /*public ResponseEntity login(HttpServletRequest request) {*/
    public ResponseEntity<?> login(@RequestBody @Validated UserDto userDto) {
        ResponseEntity<?> responseEntity = null;
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        try {
            String token = userService.login(username, password);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + token);
            //httpHeaders.add("Authorization", token);

            SingleDataResponse<String> response = responseService.getSingleDataResponse(true, "로그인 성공", token);
            responseEntity = ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(response);

        } catch (LoginFailedException exception) {
            logger.debug(exception.getMessage());
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return responseEntity;
    }

    @GetMapping("/users")
    public ResponseEntity findUserByUsername(final Authentication authentication) {
        ResponseEntity responseEntity = null;
        try {
            Long userId = ((UserDto) authentication.getPrincipal()).getUserId();
            UserDto findUser = userService.findByUserId(userId);

            SingleDataResponse<UserDto> response = responseService.getSingleDataResponse(true, "조회 성공", findUser);

            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);

            System.out.println(findUser.getRole());
        } catch (UserNotFoundException exception) {
            logger.debug(exception.getMessage());
            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return responseEntity;
    }

    @GetMapping("/user")
    public ModelAndView user(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("user");

        String header = jwtTokenProvider.resolveToken(request);
        System.out.println("user header : " + header);

        return mav;
    }
}
