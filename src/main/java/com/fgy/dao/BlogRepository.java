package com.fgy.dao;

import com.fgy.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 使用Spring Boot的JPA
 * 提供基本的增删改查
 * JpaRepository<dao操作的实体类对象,主键类型>
 * JpaSpecificationExecutor 用以动态组合查询
 */

public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {
    /*查询返回推荐的博客*/
    @Query(value="Select b from Blog b where b.recommend = true and b.published = true")
    List<Blog> findTop(Pageable pageable);

    /*查询发布的博客*/
    @Query(value="Select b from Blog b where b.published = true")
    Page<Blog> listBlogByPublished(Pageable pageable);

    /*归档博客数*/
    Long countByPublishedTrue();

    /*前端搜索博客*/
    /*String query -> ?1
    *Pageable pageable -> ?2
    */
    @Query(value="Select b from Blog b where b.published = true and b.title like ?1 or b.content like ?1 or b.description like ?1")
    Page<Blog> findByQuery(String query,Pageable pageable);

    /*浏览次数的更新*/
    @Transactional
    @Modifying
    @Query("update Blog b set b.views=b.views+1 where b.id= ?1")
    int updateViews(Long id);

    /*查询获得年份*/
    //@Query("select function('data_format',b.updateTime,'%Y') as Year from Blog b group by function('data_format',b.updateTime,'%Y') order by Year desc")
    @Query(value = "SELECT date_format(b.update_time,'%Y') as year from t_blog b where b.published = true GROUP by year ORDER BY year DESC",nativeQuery = true)
    List<String> findGroupByYear();

    //@Query("select * from t_blog b where date_format(b.updateTime,'%Y') = ?1")
    @Query(value = "select * from t_blog b where date_format(b.update_time,'%Y') = ?1 AND b.published = true",nativeQuery = true)
    List<Blog> findByYear(String year);
}
