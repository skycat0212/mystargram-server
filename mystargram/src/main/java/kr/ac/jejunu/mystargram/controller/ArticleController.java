package kr.ac.jejunu.mystargram.controller;

import kr.ac.jejunu.mystargram.entity.User;
import kr.ac.jejunu.mystargram.repository.ArticleRepository;
import kr.ac.jejunu.mystargram.entity.Article;
import kr.ac.jejunu.mystargram.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/article")
@NoArgsConstructor
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    // 게시물 id로 게시물 불러오기
    @GetMapping("/{id}")
    public Article findById(@PathVariable Integer id) {
        return articleRepository.findById(id).get();
    }

    // 모든 게시물 불러오기
    @GetMapping("/list")
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    // 최신순으로 모든 게시물 불러오기
    @GetMapping("/list/recent")
    public List<Article> getRecentArticles() {
        return  articleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    // 최신순의 게시물을 페이지 단위로 불러오기
    @GetMapping("/list/recent/{page}")
    public List<Article> getRecentArticlesByPage(@PathVariable(value = "page") Integer pageCnt) {
        Page<Article> articles =
                articleRepository.findAll(
                        PageRequest.of(pageCnt,10, Sort.Direction.DESC, "id")
                );
        return articles.getContent();
    }

    // 게시물 저장하기
    @PostMapping("/write")
    public Article addArticle(Principal principal, @RequestBody Article inputArticle) {
        User user = userRepository.findByUsername(principal.getName()).get();
        Article article = inputArticle;
        article.setWriter(user);
        return articleRepository.save(article);
    }


}
