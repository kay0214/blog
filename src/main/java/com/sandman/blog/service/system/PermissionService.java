package com.sandman.blog.service.system;

import com.sandman.blog.dao.mysql.system.PermissionDao;
import com.sandman.blog.entity.system.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sunpeikai on 2018/5/24.
 */
@Service
public class PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    public List<Permission> findByRoleId(Long roleId){
        return permissionDao.findByRoleId(roleId);
    }
}
