package com.isoft.mall.service;

import com.isoft.mall.model.mapper.CategoryMapper;
import com.isoft.mall.model.pojo.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CategoryServiceTest {
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    CategoryService categoryService;
    @Test
    void add() {

        Category category = categoryMapper.selectByName("新鲜水果");
        System.out.println(category);
    }
}
