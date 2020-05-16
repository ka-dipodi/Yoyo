package com.fgy.dao;

import com.fgy.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 使用Spring Boot的JPA
 * 提供基本的增删改查
 * JpaRepository<dao操作的实体类对象,主键类型>
 */
public interface TagRepository extends JpaRepository<Tag,Long>, JpaSpecificationExecutor<Tag> {
    /*自定义方法 以非主键(name)查询*/
    Tag findByName(String name);
    /*返回前N个标签*/
    @Query("select distinct t from Tag t join t.blogs bs where bs.published = true")
    List<Tag> findTop( Pageable pageable);






}
