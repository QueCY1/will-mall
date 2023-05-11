package com.will.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.bean.model.IndexImg;

import java.util.List;

/**
 * @author will
 */
public interface IndexImgService extends IService<IndexImg> {

    /**
     * 根据id列表删除图片
     *
     * @param ids
     */
    void deleteIndexImgByIds(Long[] ids);

    /**
     * 获取全部图片列表
     *
     * @return
     */
    List<IndexImg> listIndexImg();

    /**
     * 删除图片缓存
     */
    void removeIndexImgCache();
}
