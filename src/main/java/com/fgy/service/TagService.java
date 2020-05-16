package com.fgy.service;

import com.fgy.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TagService {
    //新增
    Tag saveTag(Tag tag);
    //查询
    Tag getTag(Long id);
    Tag getTagByName(String name);
    //分页
    Page<Tag> listTag(Pageable pageable);
    /*返回所有博客标签*/
    List<Tag> listTag();
    /*返回多个标签*/
    List<Tag> listTag(String ids);
    /*返回前N个标签*/
    List<Tag> listTagTop(Integer size);
    //更新
    Tag updateTag(Long id, Tag tag);
    //删除
    void deleteTag(Long id);
}
