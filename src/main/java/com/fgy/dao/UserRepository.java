package com.fgy.dao;

import com.fgy.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 使用Spring Boot的JPA
 * 提供基本的增删改查
 * JpaRepository<dao操作的实体类对象,主键类型>
 */
public interface UserRepository extends JpaRepository<User,Long> {
    /*自定义方法 验证用户*/
    User findByUsernameAndPassword(String username,String password);
}
