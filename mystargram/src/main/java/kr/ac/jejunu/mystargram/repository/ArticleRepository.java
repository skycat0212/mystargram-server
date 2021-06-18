package kr.ac.jejunu.mystargram.repository;

import kr.ac.jejunu.mystargram.entity.Article;
import kr.ac.jejunu.mystargram.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Page<Article> findAll(Pageable pageable);
    Page<Article> findByWriter(User writer, Pageable pageable);

    List<Article> findAllByWriter(User writer);

    void deleteAllByWriter(User writer);
}