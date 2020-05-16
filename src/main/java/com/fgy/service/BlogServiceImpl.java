package com.fgy.service;

import com.fgy.NotFoundException;
import com.fgy.dao.BlogRepository;
import com.fgy.po.Blog;
import com.fgy.po.Tag;
import com.fgy.po.Type;
import com.fgy.util.MarkdownUtils;
import com.fgy.util.MyBeanUtils;
import com.fgy.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    /*获得单个博客*/
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    /*查询并修改博客内容(Markdown转HTML),从而让显示更美观*/
    @Transactional
    @Override
    public Blog getAndCovert(Long id) {
        Blog blog1 = blogRepository.findById(id).get();
        if (blog1 == null){
            throw new NotFoundException("该博客不存在！");
        }
        Blog b=new Blog();
        BeanUtils.copyProperties( blog1,b);
        String content=b.getContent();
        b.setContent(MarkdownUtils.markdownToHTMLExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }
    /*依据时间归档博客*/
    @Override
    public Map<String, List<Blog>> archivesBlog() {
        List<String> years=blogRepository.findGroupByYear();
        Map<String,List<Blog>> map=new HashMap<>();
        for(String year : years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }
    /*博客条目计算*/
    @Override
    public Long countBlog() {
        return blogRepository.countByPublishedTrue();
    }

    /*JPA关联查询 传入标签ID查询所属博客及其对应的标签组*/
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                /*Join join=root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);*/
                List<Predicate> predicates=new ArrayList<>();
                Join<Blog,Tag> join = root.join("tags",JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(join.get("id"),tagId));
                predicates.add(criteriaBuilder.equal(root.<Boolean>get("published"),true));
                return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            }
        },pageable);
    }


    /*组合查询*/
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            /*
             * Root<Blog> root //查询的对象 获取表字段、属性
             * CriteriaQuery<?> criteriaQuery //存储查询的条件
             * CriteriaBuilder criteriaBuilder //设置具体某个条件的表达式
             * */
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //用以放置组合查询的条件
                List<Predicate> predicateList = new ArrayList<>();
                //第一个条件(博客标题)
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicateList.add(criteriaBuilder.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                //第二个条件(博客分类)
                if (blog.getTypeId() != null) {
                    predicateList.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                //第三个条件(是否推荐)
                if (blog.isRecommend()) {
                    predicateList.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
                return null;
            }
        }, pageable);
    }

    /*依靠分类查询博客**********************/
    @Override
    public Page<Blog> listBlogByType(Pageable pageable, BlogQuery blog) {


        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //用以放置组合查询的条件
                List<Predicate> predicateList = new ArrayList<>();
                //第一个条件(博客发布状态)
                if (blog.isPublished()) {
                    predicateList.add(criteriaBuilder.equal(root.<Boolean>get("published"), true));
                }
                //第二个条件(博客分类)
                if (blog.getTypeId() != null) {
                    predicateList.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
                return null;
            }
        }, pageable);
    }
    //////////////////////////////////////////////
    /*查询所有博客*/
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }



    /*前端搜索博客*/
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    /*查询推荐的博客*/
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort=Sort.by(Sort.Direction.DESC,"views");
        Pageable pageable= PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    /*查询发布的博客*/
    @Override
    public Page<Blog> listBlogByPublic(Pageable pageable) {
        return blogRepository.listBlogByPublished(pageable);
    }

    /*保存博客*/
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId() == null){
            if(blog.getFlag().length()<1){
                blog.setFlag("原创");
            }
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else{
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }
    /*修改博客*/
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 = blogRepository.findById(id).get();
        if (blog1 == null) {
            throw new NotFoundException("不存在该博客！");
        }
        /*通过反射将blog1赋值给blog*/
        /*忽略为null的属性值*/
        BeanUtils.copyProperties(blog , blog1, MyBeanUtils.getNullPropertyNames(blog));
        blog.setUpdateTime(new Date());
        return blogRepository.save(blog1);
    }
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
