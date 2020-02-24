package com.zslin.admin.controller;

import com.zslin.web.model.Category;
import com.zslin.web.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/public")
public class AdminPublicController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping(value = "listCateByStore")
    public List<Category> listCateByStore(Integer storeId) {
        System.out.println("--------------->AdminPublicController---storeId:::"+storeId);
        List<Category> list = categoryService.findByStoreId(storeId);
        return list;
    }
}
