package com.sandman.blog.dao.mysql.system;

import com.sandman.blog.entity.system.SessionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionDao {
    public void createSession(SessionEntity sessionEntity);
    public void updateSession(SessionEntity sessionEntity);
    public void deleteSession(SessionEntity sessionEntity);
    public List<SessionEntity> findAll();
    public SessionEntity findSessionById(SessionEntity sessionEntity);
}
