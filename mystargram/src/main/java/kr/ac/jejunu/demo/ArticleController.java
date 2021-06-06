package kr.ac.jejunu.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleRepository articleRepository;

    @GetMapping("/{id}")
    public Article findById(@PathVariable Integer id) {
        return articleRepository.findById(id).get();
    }


}
