package com.isoft.mall.controller;

import com.github.pagehelper.PageInfo;
import com.isoft.mall.common.ApiRestResponse;
import com.isoft.mall.common.Constant;
import com.isoft.mall.exception.MallException;
import com.isoft.mall.exception.MallExceptionEnum;
import com.isoft.mall.model.pojo.Product;
import com.isoft.mall.model.request.AddProductReq;
import com.isoft.mall.model.request.UpdateProductReq;
import com.isoft.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * @author shen
 * 后台商品管理Controller
 */

@RestController
@Validated
public class ProductAdminController {
    @Autowired
    ProductService productService;
    /**
     * 新加商品
     * @param addProductReq
     */
    @PostMapping("/product/add")
    public ApiRestResponse addProduct(@RequestBody @Valid AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    /**
     * 图片上传接口开发
     * 使用UUID命名来 添加图片
     */
    @PostMapping("/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
//      生成文件名称UUID
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
//        创建文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {
                throw new MallException(MallExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
//            将file的内容写到destFile
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURI() + "")) +
                    "/images/" + newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(MallExceptionEnum.UPLOAD_FAILED);
        }
    }

    /**
     * 获取ip和host
     * @param uri 传一个uri
     */
    private URI getHost(URI uri) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
                    null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

    /**
     * 更新接口
     */
    @ApiOperation("后台更新商品")
    @PostMapping("/product/update")
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    /**
     * 后台删除商品
     */
    @ApiOperation("后台删除商品")
    @DeleteMapping("/product/delete")
    public ApiRestResponse deleteProduct(@RequestParam Integer id) {
        productService.delete(id);
        return ApiRestResponse.success();
    }

    /**
     * 后台商品列表接口
     * @param pageNum 从第几页开始
     * @param pageSize 一页有几个
     */
    @ApiOperation("后台商品列表接口")
    @GetMapping("/product/list")
    public ApiRestResponse list(@RequestParam Integer pageNum, @RequestParam Integer pageSize,@RequestParam(defaultValue = "")String name) {
        PageInfo<Product> pageInfo = productService.listForAdmin(pageNum, pageSize,name);
        return ApiRestResponse.success(pageInfo);
    }

}
