package kr.ac.jejunu.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ArticleController articleController;

    @Test
    void contextLoads() {
    }

    @Test
    void getArticleById() {
        Integer id = 1;

        Article article = articleController.findById(id);
        System.out.println("get article 1 by Controller : " + article);

        Integer writerId = 1;
        String content = "this is first Article";

        assertThat(article.getId(), is(id));
        assertThat(article.getWriter().getId(), is(writerId));
        assertThat(article.getContent(), is(content));
    }


}
