package kr.ac.jejunu.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @PostMapping("/enroll")
    public User create(@RequestBody Map<String, String> newUser) {
        return userRepository.save(
                User.builder().
                        username(newUser.get("username")).
                        password(passwordEncoder.encode(newUser.get("password")))
                .build()
        );
    }

    // 로그인
    @PostMapping("/login")
    public Token login(@RequestBody Map<String, String> loginData) {
        User user = userRepository.findByUsername(loginData.get("username")).
                orElseThrow( () -> new IllegalArgumentException("Unvalid ID"));
        if (!passwordEncoder.matches(loginData.get("password"), user.getPassword())) {
            throw new IllegalArgumentException("Wrong PW");
        }

        return Token.builder().token(jwtTokenProvider.createToken(user.getUsername(),user.getRoles())).build();


    }


}
