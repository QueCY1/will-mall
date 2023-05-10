package com.will.shop.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.shop.bean.app.dto.ProdCommDataDto;
import com.will.shop.bean.app.dto.ProdCommDto;
import com.will.shop.bean.model.ProdComm;
import com.will.shop.common.util.Arithmetic;
import com.will.shop.common.util.PageParam;
import com.will.shop.dao.ProdCommMapper;
import com.will.shop.service.ProdCommService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 商品评论
 *
 * @author will
 */
@Service
@RequiredArgsConstructor
public class ProdCommServiceImpl extends ServiceImpl<ProdCommMapper, ProdComm> implements ProdCommService {

    private final ProdCommMapper prodCommMapper;

    @Override
    public ProdCommDataDto getProdCommDataByProdId(Long prodId) {
        ProdCommDataDto prodCommDataDto = prodCommMapper.getProdCommDataByProdId(prodId);
        //计算出好评率
        if (prodCommDataDto.getPraiseNumber() == 0 || prodCommDataDto.getNumber() == 0) {
            prodCommDataDto.setPositiveRating(0.0);
        } else {
            prodCommDataDto.setPositiveRating(Arithmetic.mul(Arithmetic.div(prodCommDataDto.getPraiseNumber(), prodCommDataDto.getNumber()), 100));
        }
        return prodCommDataDto;
    }

    @Override
    public IPage<ProdCommDto> getProdCommDtoPageByUserId(PageParam<ProdCommDto> page, String userId) {
        return prodCommMapper.getProdCommDtoPageByUserId(page, userId);
    }

    @Override
    public IPage<ProdCommDto> getProdCommDtoPageByProdId(PageParam<ProdCommDto> page, Long prodId, Integer evaluate) {

        IPage<ProdCommDto> prodCommDtoPages = prodCommMapper.getProdCommDtoPageByProdId(page, prodId, evaluate);
        prodCommDtoPages.getRecords().forEach(prodCommDto -> {
            // 匿名评价
            if (prodCommDto.getIsAnonymous() == 1) {
                prodCommDto.setNickName(null);
            }
        });
        return prodCommDtoPages;
    }

    @Override
    public IPage<ProdComm> getProdCommPage(PageParam<ProdComm> page, ProdComm prodComm) {
        return prodCommMapper.getProdCommPage(page, prodComm);
    }
}
