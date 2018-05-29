package com.sandman.blog.service.system;

import com.sandman.blog.dao.mysql.system.RoleDao;
import com.sandman.blog.entity.system.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunpeikai on 2018/5/23.
 */
@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    public List<Role> findByUserId(Long userId){
        return roleDao.findByUserId(userId);
    }
}
