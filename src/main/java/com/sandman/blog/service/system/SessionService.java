package com.sandman.blog.service.system;

import com.sandman.blog.dao.mysql.system.SessionDao;
import com.sandman.blog.entity.system.SessionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private SessionDao sessionDao;
    public void createSession(SessionEntity sessionEntity){
        log.info("创建session======================" + sessionEntity.toString());
        sessionDao.createSession(sessionEntity);
    }
    public SessionEntity findSessionById(String id){
        log.info("获取session======================" + id);
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(id);
        return sessionDao.findSessionById(sessionEntity);
    }
    public void updateSession(SessionEntity sessionEntity){
        log.info("更新session======================" + sessionEntity.toString());
        sessionDao.updateSession(sessionEntity);
    }
    public void deleteSession(SessionEntity sessionEntity){
        log.info("删除session======================" + sessionEntity.toString());
        sessionDao.deleteSession(sessionEntity);
    }
    public List<SessionEntity> findAll(){
        log.info("获取全部session======================");
        return sessionDao.findAll();
    }
}
