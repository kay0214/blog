package com.sandman.blog.service.user;

import com.sandman.blog.dao.mysql.user.NoticeDao;
import com.sandman.blog.entity.common.BaseDto;
import com.sandman.blog.entity.common.ResponseStatus;
import com.sandman.blog.entity.user.Notice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private NoticeDao noticeDao;
    public BaseDto getAllNotice(){
        List<Notice> noticeList = noticeDao.getAllNotice();
        return new BaseDto(ResponseStatus.SUCCESS,noticeList);
    }
}
