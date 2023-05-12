package com.will.shop.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.will.shop.bean.app.dto.ProductItemDto;
import com.will.shop.bean.enums.TransportChargeType;
import com.will.shop.bean.model.*;
import com.will.shop.common.util.Arithmetic;
import com.will.shop.common.util.Json;
import com.will.shop.service.ProductService;
import com.will.shop.service.SkuService;
import com.will.shop.service.TransportManagerService;
import com.will.shop.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author will
 */
@Service
@RequiredArgsConstructor
public class TransportManagerServiceImpl implements TransportManagerService {

    private final ProductService productService;

    private final SkuService skuService;

    private final TransportService transportService;

    @Override
    public Double calculateTransfee(ProductItemDto productItem, UserAddr userAddr) {
        Product product = productService.getProductByProdId(productItem.getProdId());
        // 用户所在城市id
        Long cityId = userAddr.getCityId();

        Product.DeliveryModeVO deliveryModeVO = Json.parseObject(product.getDeliveryMode(), Product.DeliveryModeVO.class);

        // 没有店铺配送的方式
        if (!deliveryModeVO.getHasShopDelivery()) {
            return 0.0;
        }
        if (product.getDeliveryTemplateId() == null) {
            return 0.0;
        }

        //找出该产品的运费项
        Transport transport = transportService.getTransportAndAllItems(product.getDeliveryTemplateId());
        //商家把运费模板删除
        if (transport == null) {
            return 0.0;
        }

        Sku sku = skuService.getSkuBySkuId(productItem.getSkuId());

        // 用于计算运费的件数
        Double piece = getPiece(productItem, transport, sku);


        //如果有包邮的条件
        if (transport.getHasFreeCondition() == 1) {
            // 获取所有的包邮条件
            List<TransfeeFree> transfeeFrees = transport.getTransfeeFrees();
            for (TransfeeFree transfeeFree : transfeeFrees) {
                List<Area> freeCityList = transfeeFree.getFreeCityList();
                for (Area freeCity : freeCityList) {
                    if (!Objects.equals(freeCity.getAreaId(), cityId)) {
                        continue;
                    }
                    //包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）
                    boolean isFree = (transfeeFree.getFreeType() == 0 && piece >= transfeeFree.getPiece()) ||
                            (transfeeFree.getFreeType() == 1 && productItem.getProductTotalAmount() >= transfeeFree.getAmount()) ||
                            (transfeeFree.getFreeType() == 2 && piece >= transfeeFree.getPiece() && productItem.getProductTotalAmount() >= transfeeFree.getAmount());
                    if (isFree) {
                        return 0.0;
                    }
                }
            }
        }

        //订单的运费项
        Transfee transfee = null;
        List<Transfee> transfees = transport.getTransfees();
        for (Transfee dbTransfee : transfees) {
            // 将该商品的运费设置为默认运费
            if (transfee == null && CollectionUtil.isEmpty(dbTransfee.getCityList())) {
                transfee = dbTransfee;
            }
            // 如果在运费模板中的城市找到该商品的运费，则将该商品由默认运费设置为该城市的运费
            for (Area area : dbTransfee.getCityList()) {
                if (area.getAreaId().equals(cityId)) {
                    transfee = dbTransfee;
                    break;
                }
            }
            // 如果在运费模板中的城市找到该商品的运费，则退出整个循环
            if (transfee != null && CollectionUtil.isNotEmpty(transfee.getCityList())) {
                break;
            }
        }

        // 如果无法获取到任何运费相关信息，则返回0运费
        if (transfee == null) {
            return 0.0;
        }

        // 产品的运费
        Double fee = transfee.getFirstFee();
        // 如果件数大于首件数量，则开始计算超出的运费
        if (piece > transfee.getFirstPiece()) {
            // 续件数量
            Double prodContinuousPiece = Arithmetic.sub(piece, transfee.getFirstPiece());
            // 续件数量的倍数，向上取整
            Integer mulNumber = (int) Math.ceil(Arithmetic.div(prodContinuousPiece, transfee.getContinuousPiece()));
            // 续件数量运费
            Double continuousFee = Arithmetic.mul(mulNumber, transfee.getContinuousFee());
            fee = Arithmetic.add(fee, continuousFee);
        }
        return fee;
    }

    @NotNull
    private Double getPiece(ProductItemDto productItem, Transport transport, Sku sku) {
        Double piece = 0.0;

        if (Objects.equals(TransportChargeType.COUNT.value(), transport.getChargeType())) {
            // 按件数计算运费
            piece = Double.valueOf(productItem.getProdCount());
        } else if (Objects.equals(TransportChargeType.WEIGHT.value(), transport.getChargeType())) {
            // 按重量计算运费
            double weight = sku.getWeight() == null ? 0 : sku.getWeight();
            piece = Arithmetic.mul(weight, productItem.getProdCount());
        } else if (Objects.equals(TransportChargeType.VOLUME.value(), transport.getChargeType())) {
            // 按体积计算运费
            double volume = sku.getVolume() == null ? 0 : sku.getVolume();
            piece = Arithmetic.mul(volume, productItem.getProdCount());
        }
        return piece;
    }


}
