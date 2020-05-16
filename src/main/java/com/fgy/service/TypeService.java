package com.fgy.service;

import com.fgy.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TypeService {
    //新增
    Type saveType(Type type);
    //查询
    Type getType(Long id);
    Type getTypeByName(String name);
    //分页
    Page<Type> listType(Pageable pageable);
    /*返回所有博客分类*/
    List<Type> listType();
    /*返回博客数多的分类*/
    List<Type> listTypeTop(Integer size);
    //更新
    Type updateType(Long id,Type type);
    //删除
    void deleteType(Long id);

}
