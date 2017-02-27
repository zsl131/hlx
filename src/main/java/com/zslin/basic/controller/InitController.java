package com.zslin.basic.controller;

import com.zslin.basic.annotations.Token;
import com.zslin.basic.model.*;
import com.zslin.basic.service.*;
import com.zslin.basic.tools.AuthTools;
import com.zslin.basic.tools.SecurityUtil;
import com.zslin.basic.tools.TokenTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 系统初始化Controller
 * @author zslin.com 20160520
 *
 */
@Controller
@RequestMapping
public class InitController {

    @Autowired
    private IAppConfigService appConfigService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private AuthTools authTools;

    /** 初始化GET */
    @RequestMapping(value="init", method=RequestMethod.GET)
    @Token(flag= Token.READY)
    public String init(Model model, HttpServletRequest request) {
        AppConfig appConfig = appConfigService.loadOne();
        if(appConfig==null || appConfig.getInitFlag()==null || "0".equals(appConfig.getInitFlag())) {
            //表示可以初始化
            User user = new User();
            user.setStatus(1);
            model.addAttribute("user", user);
            model.addAttribute("initFlag", true);
        } else {
            //表示不可以初始化
            model.addAttribute("initFlag", false);
        }
        return "admin/basic/init";
    }

    /** 初始化POST */
    @Token(flag= Token.CHECK)
    @RequestMapping(value="init", method=RequestMethod.POST)
    public String init(Model model, User user, HttpServletRequest request) {
        if(TokenTools.isNoRepeat(request)) {
            AppConfig appConfig = appConfigService.loadOne();
            if(appConfig==null || appConfig.getInitFlag()==null || "0".equals(appConfig.getInitFlag())) {
                initBaseUser(user);
            } else {
                //表示不可以初始化，不可以初始化，则直接返回
            }
        }
        return "redirect:/init";
    }

    /** 系统首页 */
    @RequestMapping(value={"index"}, method=RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        AppConfig appConfig = appConfigService.loadOne();
        if(appConfig==null || appConfig.getInitFlag()==null || "0".equals(appConfig.getInitFlag())) {
            //表示可以初始化
            return "redirect:/init";
        } else {
            //表示不可以初始化，不可以初始化，则直接返回
            String indexPage = appConfig.getIndexPage();
            if(indexPage==null) {indexPage = "/admin";}
            return "redirect:"+indexPage;
        }
    }

    /**
     * 初始化基础用户数据
     * - 1、初始化菜单
     * - 2、初始化角色
     * - 3、为角色分配所有菜单
     * - 4、添加用户
     * - 5、为用户分配角色
     * @param user
     */
    public void initBaseUser(User user) {
        try {
            authTools.buildSystemMenu("com/zslin/basic/controller/*Controller.class");
            Role role = new Role();
            role.setName("超级管理员角色"); role.setSn("ROLE_SUPER_ADMIN");
            roleService.save(role);
//            List<Menu> menuList = menuService.listAllUrlMenu();
            List<Menu> menuList = menuService.findAll();
            for(Menu m : menuList) {
                RoleMenu rm = roleMenuService.queryByRidAndMid(role.getId(), m.getId());
                if(rm==null) {
                    rm = new RoleMenu();
                    rm.setMid(m.getId()); rm.setRid(role.getId());
                    roleMenuService.save(rm);
                } else {
                    roleMenuService.delete(rm);
                }
            }

            user.setPassword(SecurityUtil.md5(user.getUsername(), user.getPassword()));
            user.setStatus(1); user.setIsAdmin(1); user.setCreateDate(new Date());
            userService.save(user);
            addOrDeleteUserRole(user.getId(), role.getId());

            AppConfig ac = appConfigService.loadOne();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(ac==null) {
                ac = new AppConfig();
                ac.setCreateDate(sdf.format(new Date()));
                ac.setAppVersion("V 1.0");
                ac.setAppName("系统名称");
                ac.setIndexPage("/");
                ac.setAdminEmail("zsl131@qq.com");
                ac.setInitFlag("1"); appConfigService.save(ac);
            } else {
                ac.setCreateDate(sdf.format(new Date()));
                ac.setInitFlag("1"); appConfigService.save(ac);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加或删除用户角色对应关系，如果存在则删除，如果不存在则添加
     * @param userId 用户Id
     * @param roleId 角色Id
     */
    public void addOrDeleteUserRole(Integer userId, Integer roleId) {
        UserRole ur = userRoleService.findByUidAndRid(userId, roleId);
        if(ur==null) {
            ur = new UserRole();
            ur.setRid(roleId); ur.setUid(userId);
            userRoleService.save(ur);
        } else {
            userRoleService.delete(ur);
        }
    }
}