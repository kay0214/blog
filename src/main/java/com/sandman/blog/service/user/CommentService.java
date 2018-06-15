package com.sandman.blog.service.user;

import com.github.pagehelper.PageHelper;
import com.sandman.blog.dao.mysql.user.BlogDao;
import com.sandman.blog.dao.mysql.user.CommentDao;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.CommentLeaf;
import com.sandman.blog.entity.common.CommentRoute;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.user.Blog;
import com.sandman.blog.entity.user.Comment;
import com.sandman.blog.utils.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class CommentService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private BloggerService bloggerService;

    public BaseDto getMyComment(Integer pageNumber, Integer size,Long bloggerId) {
        pageNumber = (pageNumber==null || pageNumber<1)?1:pageNumber;
        size = (size==null || size<0)?10:size;
        String orderBy = "createTime desc";//默认按照createTime降序排序

        Integer totalRow = commentDao.getMyComment(bloggerId).size();//查询出数据条数
        log.info("totalRow:::::{}",totalRow);
        PageHelper.startPage(pageNumber,size).setOrderBy(orderBy);

        List<Comment> commentList = commentDao.getMyComment(bloggerId);//查询出列表（已经分页）

        PageBean<Comment> pageBean = new PageBean<>(pageNumber,size,totalRow);//这里是为了计算页数，页码

        pageBean.setItems(commentList);
        List<Comment> result = pageBean.getItems();

        Map data = new HashMap();//最终返回的map

        data.put("totalRow",totalRow);
        data.put("totalPage",pageBean.getTotalPage());
        data.put("currentPage",pageBean.getCurrentPage());//默认0就是第一页
        data.put("commentList",result);
        return new BaseDto(ResponseStatus.SUCCESS,data);
    }

    public BaseDto getCommentMe(Integer pageNumber, Integer size,Long bloggerId) {
        log.info("pageNumber======={},size========{},bloggerId========{}",pageNumber,size,bloggerId);
        pageNumber = (pageNumber==null || pageNumber<1)?1:pageNumber;
        size = (size==null || size<0)?10:size;
        String orderBy = "createTime desc";//默认按照createTime降序排序

        Integer totalRow = commentDao.getCommentMe(bloggerId).size();//查询出数据条数
        log.info("totalRow:::::{}",totalRow);
        PageHelper.startPage(pageNumber,size).setOrderBy(orderBy);

        List<Comment> commentList = commentDao.getCommentMe(bloggerId);//查询出列表（已经分页）

        PageBean<Comment> pageBean = new PageBean<>(pageNumber,size,totalRow);//这里是为了计算页数，页码

        pageBean.setItems(commentList);
        List<Comment> result = pageBean.getItems();

        Map data = new HashMap();//最终返回的map

        data.put("totalRow",totalRow);
        data.put("totalPage",pageBean.getTotalPage());
        data.put("currentPage",pageBean.getCurrentPage());//默认0就是第一页
        data.put("commentList",result);
        return new BaseDto(ResponseStatus.SUCCESS,data);
    }
    /**
     * 删除某一篇博客下面的所有评论
     * */
    public Integer deleteCommentByBlogId(Long blogId){
        return commentDao.deleteCommentByBlogId(blogId);//返回受影响的数量
    }
    public BaseDto deleteComment(Long id, Long bloggerId) {
        log.info("deleteComment=========id:::{},bloggerId:::{}", id, bloggerId);
        Comment comment = commentDao.findById(id);
        if (bloggerId.equals(comment.getBlog().getBloggerId()) || bloggerId.equals(comment.getBloggerId())) {//如果是评论我的，或者是我评论的。那么就可以删除
            if(comment.getReplayed() == 1){//如果有回复，就先把回复删除
                log.info("如果有回复，就先把回复删除");
                commentDao.deleteReplay(comment.getId());
            }
            commentDao.deleteComment(id);
            return new BaseDto(ResponseStatus.SUCCESS);
        }
        return new BaseDto(ResponseStatus.NOT_HAVE_PERMISSION_TO_DELETE);
    }

    //TODO:
    public List<Comment> getCommentByBlogId(Long blogId) {
        List<Comment> resultList = new ArrayList<>();
        List<Comment> commentList = commentDao.getCommentByBlogId(blogId);

        List<CommentRoute> routeList = new ArrayList<>();//父亲节点
        List<CommentLeaf> leafList = new ArrayList<>();//叶子节点
        for (Comment comment : commentList) {
            if (comment.getParentId() == 0) {
                routeList.add(new CommentRoute(comment));
            } else {
                leafList.add(new CommentLeaf(comment));
            }
        }
        while(leafList.size()!=0){
            for (CommentRoute route : routeList) {
                for (CommentLeaf leaf : leafList) {
                    if (route.getParentIdList().contains(leaf.getFatherId())) {
                        route.addSonList(leaf.getComment());
                        leafList.remove(leaf);
                        break;
                    }
                }
            }
        }

        for (CommentRoute route : routeList) {
            System.out.println(route.toString());
            resultList.add(route.getCompletedFather());
        }

        return resultList;
    }

    public BaseDto createComment(Comment comment) {//评论一篇博客
        comment.setStatus(0);//待审核状态
        comment.setCreateBy(comment.getBloggerId());
        comment.setCreateTime(ZonedDateTime.now());
        comment.setUpdateBy(comment.getBloggerId());
        comment.setUpdateTime(ZonedDateTime.now());
        comment.setDelFlag(0);
        log.info("comment====={}",comment);
        commentDao.createComment(comment);
        if(comment.getParentId() != 0){ // 回复
            log.info("这是回复的评论");
            Comment parent = commentDao.findById(comment.getParentId());
            System.out.println("parent:::::::::::::" + parent.toString());
            parent.setReplayed(1);
            commentDao.updateComment(parent);
        }
        Blog blog = blogDao.getBlogById(comment.getBlogId());
        blog.setReplayCount(blog.getReplayCount() + 1);
        blogDao.updateBlog(blog);
        bloggerService.addCommentCount(blog.getBloggerId());//博主的评论数 + 1
        return new BaseDto(ResponseStatus.SUCCESS);
    }
}
