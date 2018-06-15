package com.sandman.blog.service.user;

import com.sandman.blog.dao.mysql.user.BloggerDao;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.common.SftpParam;
import com.sandman.blog.entity.user.Blogger;
import com.sandman.blog.utils.FileUtils;
import com.sandman.blog.utils.RandomUtils;
import com.sandman.blog.utils.ShiroSecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.ZonedDateTime;

@Service
public class BloggerService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private BloggerDao bloggerDao;
    public void createBlogger(Long id,String userName,String nickName){
        Blogger blogger = new Blogger();
        blogger.setId(id);
        blogger.setUserName(userName);
        blogger.setNickName(nickName);
        blogger.setImageUrl(SftpParam.getBloggerDefaultImg());
        blogger.setOriginalBlogCount(0);
        blogger.setTransferBlogCount(0);
        blogger.setVisitCount(0);
        blogger.setCommentCount(0);
        blogger.setCreateBy(1L);
        blogger.setCreateTime(ZonedDateTime.now());
        blogger.setUpdateBy(1L);
        blogger.setUpdateTime(ZonedDateTime.now());
        blogger.setDelFlag(0);
        bloggerDao.createBlogger(blogger);
    }
    public BaseDto getBloggerById(Long id){
        if(id == null){
            return new BaseDto(ResponseStatus.HAVE_NO_DATA);
        }
        Blogger blogger = bloggerDao.findById(id);
        addVisitCount(id);//访问数 + 1

        return new BaseDto(ResponseStatus.SUCCESS,blogger);
    }
    public BaseDto modifyAvatar(MultipartFile file,Long bloggerId){
        Blogger blogger = bloggerDao.findById(bloggerId);
        if(blogger != null){
            String fileName = RandomUtils.getRandomFileName() + "." + FileUtils.getSuffixNameByFileName(file.getOriginalFilename());
            String path = "/spkIMG/sandman/blog/content/" + bloggerId + "/";//这里的7 到时候换成userId
            String filePath = SftpParam.getPathPrefix() + path;//服务器图片路径
            String linePath = SftpParam.getLinePathPrefix() + path + fileName;//网络图片路径
            log.info("filePath=[{}],linePath=[{}]",filePath,linePath);
            File tempFile = FileUtils.getFileByMultipartFile(file);
            boolean uploadSuccess = FileUtils.upload(filePath,fileName,tempFile);//FileUtils.upload(filePath,fileName,tempFile);//上传服务器
            if(uploadSuccess){
                tempFile.delete();
                blogger.setImageUrl(linePath);
                bloggerDao.updateBlogger(blogger);
                return new BaseDto(ResponseStatus.SUCCESS,blogger);
            }
            return new BaseDto(ResponseStatus.UPLOAD_SERVER_FAILED);//上传远程服务器失败
        }else{
            return new BaseDto(ResponseStatus.HAVE_NO_DATA);
        }
    }
    //id为bloggerId的博主的原创博客数量+1
    public void addOriginalBlogCount(Long bloggerId){
        Blogger blogger = bloggerDao.findById(bloggerId);
        blogger.setOriginalBlogCount(blogger.getOriginalBlogCount() + 1);
        bloggerDao.updateBlogger(blogger);
    }
    //id为bloggerId的博主的原创博客数量-1
    public void reduceOriginalBlogCount(Long bloggerId){
        Blogger blogger = bloggerDao.findById(bloggerId);
        blogger.setOriginalBlogCount(blogger.getOriginalBlogCount() - 1);
        bloggerDao.updateBlogger(blogger);
    }
    //id为bloggerId的博主的转载博客数量+1
    public void addTransferBlogCount(Long bloggerId){
        Blogger blogger = bloggerDao.findById(bloggerId);
        blogger.setTransferBlogCount(blogger.getTransferBlogCount() + 1);
        bloggerDao.updateBlogger(blogger);
    }
    //id为bloggerId的博主的转载博客数量-1
    public void reduceTransferBlogCount(Long bloggerId){
        Blogger blogger = bloggerDao.findById(bloggerId);
        blogger.setTransferBlogCount(blogger.getTransferBlogCount() - 1);
        bloggerDao.updateBlogger(blogger);
    }
    //id为bloggerId的博主的访问数量+1
    public void addVisitCount(Long bloggerId){
        Long myBloggerId = ShiroSecurityUtils.getCurrentUserId();
        if(myBloggerId!=null){  //登录用户并且登录用户不是博主自己，访问量 + 1
            Blogger blogger = bloggerDao.findById(bloggerId);
            if(!myBloggerId.equals(blogger.getId())){
                blogger.setVisitCount(blogger.getVisitCount() + 1);
                bloggerDao.updateBlogger(blogger);
            }
        }
    }
    //id为bloggerId的博主的评论数量+1
    public void addCommentCount(Long bloggerId){
        Blogger blogger = bloggerDao.findById(bloggerId);
        blogger.setCommentCount(blogger.getCommentCount() + 1);
        bloggerDao.updateBlogger(blogger);
    }
    //id为bloggerId的博主的评论数量- count个
    public void reduceCommentCount(Long bloggerId,Integer count){
        count = (count==null)?0:count;
        Blogger blogger = bloggerDao.findById(bloggerId);
        blogger.setCommentCount(blogger.getCommentCount() - count);
        bloggerDao.updateBlogger(blogger);
    }
}
