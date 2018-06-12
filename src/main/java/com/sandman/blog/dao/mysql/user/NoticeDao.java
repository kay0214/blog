package com.sandman.blog.dao.mysql.user;

import com.sandman.blog.entity.user.Notice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeDao {
    public List<Notice> getAllNotice();
}
