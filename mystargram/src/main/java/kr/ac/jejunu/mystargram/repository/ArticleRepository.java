package kr.ac.jejunu.mystargram.repository;

import kr.ac.jejunu.mystargram.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

}
