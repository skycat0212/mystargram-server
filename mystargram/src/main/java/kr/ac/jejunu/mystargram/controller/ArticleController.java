package kr.ac.jejunu.mystargram.controller;

import kr.ac.jejunu.mystargram.entity.ArticlePack;
import kr.ac.jejunu.mystargram.entity.User;
import kr.ac.jejunu.mystargram.repository.ArticleRepository;
import kr.ac.jejunu.mystargram.entity.Article;
import kr.ac.jejunu.mystargram.repository.UserRepository;
import kr.ac.jejunu.mystargram.utils.ImageUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    // 특정 유저의 최신순의 게시물을 페이지 단위로 불러오기
    @GetMapping("/list/user/{username}/page/{page}")
    public List<Article> getUserArticlesByPage(@PathVariable(value = "username") String username, @PathVariable(value = "page") Integer pageCnt) {
        Optional<User> writer = userRepository.findByUsername(username);
        Page<Article> articles =
                articleRepository.findByWriter(
                        writer.get(),
                        PageRequest.of(pageCnt,10, Sort.Direction.DESC, "id")
                );
        return articles.getContent();
    }

    // 게시물 저장하기
    @PostMapping("/write")
    public Article addArticle(HttpServletRequest request, Principal principal, @RequestBody ArticlePack inputArticle) throws IOException {
        User user = userRepository.findByUsername(principal.getName()).get();
        Article article = inputArticle.getArticle();
        article.setWriter(user);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date time = new Date();
        String fileName = user.getId().toString() + "-" + dateFormat.format(time);
        String imgPath = ImageUtils.generateAbsoluteImgPath("article", fileName, "jpg");
        byte[] imgData = ImageUtils.base64ToByteArray(inputArticle.getArticleImgData().toString());
        File saveFile = new File(imgPath);

        ImageUtils.writeImageAtFile(imgData, saveFile);

        // set image path for access out
        String serverUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        article.setImgUrl(serverUrl + "/images/article/" + fileName + ".jpg");

        return articleRepository.save(article);
    }

    // 게시글 수정하기
    @PutMapping("/correct")
    public Boolean correctArticle(Principal principal, @RequestBody Article article) {
        User user = userRepository.findByUsername(principal.getName()).get();
        if (!user.getUsername().equals(article.getWriter().getUsername())) {
            return false;
        }
        return articleRepository.save(article) != null;
    }

    // 게시글 삭제하기
    @DeleteMapping("/delete")
    public Boolean deleteArticle(Principal principal, @RequestBody Article article) {
        User user = userRepository.findByUsername(principal.getName()).get();
        if (!user.getUsername().equals(article.getWriter().getUsername())) {
            return false;
        }
        Article deletedArticle = article;
        deletedArticle.setWriter(user);
        articleRepository.delete(deletedArticle);
        System.out.println(articleRepository.findById(deletedArticle.getId()));

        return articleRepository.findById(deletedArticle.getId()).isEmpty();
    }
}
