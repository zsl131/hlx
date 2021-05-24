package com.zslin.basic.controller;

import com.zslin.basic.annotations.AdminAuth;
import com.zslin.basic.db.dao.IDBBackupDao;
import com.zslin.basic.db.model.DBBackup;
import com.zslin.basic.db.tools.ExportDBTools;
import com.zslin.basic.qiniu.tools.QiniuTools;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.utils.ParamFilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="admin/dbBackup")
@AdminAuth(name = "数据库管理", psn="系统管理", orderNum = 2, pentity=0, porderNum=2)
@Slf4j
public class AdminDBBackupController {

    @Autowired
    private IDBBackupDao dbBackupDao;

    @Autowired
    private QiniuTools qiniuTools;

    @Autowired
    private ExportDBTools exportDBTools;

    /** 列表 */
    @AdminAuth(name = "数据库管理", orderNum = 1, icon="fa fa-list", type="1")
    @RequestMapping(value="list", method= RequestMethod.GET)
    public String list(Model model, Integer page, HttpServletRequest request) {
        Page<DBBackup> datas = dbBackupDao.findAll(new ParamFilterUtil<DBBackup>().buildSearch(model, request), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("id_d")));
        model.addAttribute("datas", datas);
        return "admin/basic/dbBackup/list";
    }

    /** 手动备份 */
    @AdminAuth(name="备份数据库", orderNum=4)
    @PostMapping(value = "backup")
    public @ResponseBody String backup(HttpServletRequest request) {
        exportDBTools.exportDb();
        return "1";
    }

    @AdminAuth(name="删除数据库", orderNum=4)
    @RequestMapping(value="delete/{id}", method=RequestMethod.POST)
    public @ResponseBody
    String delete(@PathVariable Integer id) {
        try {
            DBBackup backup = dbBackupDao.findOne(id);
            qiniuTools.deleteFile(backup.getName());
            dbBackupDao.delete(backup);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
}
