package com.zslin.basic.tools;

import com.zslin.basic.model.Role;
import com.zslin.basic.model.RoleMenu;
import com.zslin.basic.service.IMenuService;
import com.zslin.basic.service.IRoleMenuService;
import com.zslin.basic.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zsl on 2018/7/23.
 */
@Component
public class AuthRoleMenuTools {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Autowired
    private IMenuService menuService;

    public void authAdmin() {
        Role r = roleService.findBySn("ROLE_SUPER_ADMIN");
        if(r!=null) {
            Integer rid = r.getId();
            List<Integer> alreadyMenuIds = roleMenuService.queryMenuIds(rid);
            List<Integer> allMenuIds = menuService.findAllIds();
            for(Integer id : allMenuIds) {
                if(!alreadyMenuIds.contains(id)) {
                    RoleMenu rm = new RoleMenu();
                    rm.setMid(id);
                    rm.setRid(rid);
                    roleMenuService.save(rm);
                }
            }
        }
    }
}
