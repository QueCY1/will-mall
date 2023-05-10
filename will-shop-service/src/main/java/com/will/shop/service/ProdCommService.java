package com.will.shop.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.will.shop.bean.app.dto.ProdCommDataDto;
import com.will.shop.bean.app.dto.ProdCommDto;
import com.will.shop.bean.model.ProdComm;
import com.will.shop.common.util.PageParam;


/**
 * 商品评论
 *
 * @author will
 */
public interface ProdCommService extends IService<ProdComm> {
    /**
     * 根据商品id获取商品评论信息
     *
     * @param prodId
     * @return
     */
    ProdCommDataDto getProdCommDataByProdId(Long prodId);

    /**
     * 根据用户id分页获取商品评论
     *
     * @param page
     * @param userId
     * @return
     */
    IPage<ProdCommDto> getProdCommDtoPageByUserId(PageParam<ProdCommDto> page, String userId);

    /**
     * 根据商品id和评价等级获取商品评价
     *
     * @param page
     * @param prodId
     * @param evaluate
     * @return
     */
    IPage<ProdCommDto> getProdCommDtoPageByProdId(PageParam<ProdCommDto> page, Long prodId, Integer evaluate);

    /**
     * 根据参数分页获取商品评价
     *
     * @param page
     * @param prodComm
     * @return
     */
    IPage<ProdComm> getProdCommPage(PageParam<ProdComm> page, ProdComm prodComm);

}
