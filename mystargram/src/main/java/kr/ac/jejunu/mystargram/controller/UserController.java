package kr.ac.jejunu.mystargram.controller;

import kr.ac.jejunu.mystargram.entity.Article;
import kr.ac.jejunu.mystargram.repository.ArticleRepository;
import kr.ac.jejunu.mystargram.repository.UserRepository;
import kr.ac.jejunu.mystargram.entity.Token;
import kr.ac.jejunu.mystargram.entity.User;
import kr.ac.jejunu.mystargram.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ArticleRepository articleRepository;

    // 회원가입
    @PostMapping("/enroll")
    public User create(@RequestBody Map<String, String> newUser) {
        return userRepository.save(
                User.builder().
                        username(newUser.get("username")).
                        password(passwordEncoder.encode(newUser.get("password"))).
                        roles(Collections.singletonList("ROLE_USER"))
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

    //아이디 중복검사
   @GetMapping("/{username}")
    public boolean checkUsableName(@PathVariable(value = "username") String username) {
        System.out.println(username);
        System.out.println(userRepository.findByUsername(username).isEmpty());
        if (userRepository.findByUsername(username).isEmpty()) {
            return true;
        } else {
            return false;
        }

    }
    //회원 탈퇴
    @DeleteMapping("/delete/{username}")
    public boolean deleteUser(Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        List<Article> articleList = articleRepository.findAllByWriter(user.get());
        for (Article article:articleList) {
            articleRepository.delete(article);
        }
        userRepository.delete(user.get());
        return userRepository.findByUsername(user.get().getUsername()).isEmpty();
    }


}
