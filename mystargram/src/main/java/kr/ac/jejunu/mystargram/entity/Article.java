package kr.ac.jejunu.mystargram.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article extends Object{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private User writer;
    private String content;
    private String imgUrl;

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", writer=" + writer +
                ", content='" + content + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
