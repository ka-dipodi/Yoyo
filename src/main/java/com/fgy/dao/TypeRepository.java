package com.fgy.dao;

import com.fgy.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 使用Spring Boot的JPA
 * 提供基本的增删改查
 * JpaRepository<dao操作的实体类对象,主键类型>
 */
public interface TypeRepository extends JpaRepository<Type,Long>{
    /*自定义方法 以非主键(name)查询*/
    Type findByName(String name);
    /*自定义方法 按照分页查询分类*/
    @Query("select distinct t from Type t left join Blog b on t.id=b.type.id where b.published=true")
    List<Type> findTop(Pageable pageable);
}
