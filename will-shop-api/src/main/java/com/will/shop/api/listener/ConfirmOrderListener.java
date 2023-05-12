package com.will.shop.api.listener;

import com.will.shop.bean.app.dto.ShopCartItemDto;
import com.will.shop.bean.app.dto.ShopCartOrderDto;
import com.will.shop.bean.app.param.OrderParam;
import com.will.shop.bean.event.ConfirmOrderEvent;
import com.will.shop.bean.model.Product;
import com.will.shop.bean.model.Sku;
import com.will.shop.bean.model.UserAddr;
import com.will.shop.bean.order.ConfirmOrderOrder;
import com.will.shop.common.exception.WillShopBindException;
import com.will.shop.common.util.Arithmetic;
import com.will.shop.security.api.util.SecurityUtils;
import com.will.shop.service.ProductService;
import com.will.shop.service.SkuService;
import com.will.shop.service.TransportManagerService;
import com.will.shop.service.UserAddrService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 确认订单信息时的默认操作
 * @author LGH
 */
@Component("defaultConfirmOrderListener")
@AllArgsConstructor
public class ConfirmOrderListener {

    private final UserAddrService userAddrService;

    private final TransportManagerService transportManagerService;

    private final ProductService productService;

    private final SkuService skuService;

    /**
     * 计算订单金额
     */
    @EventListener(ConfirmOrderEvent.class)
    @Order(ConfirmOrderOrder.DEFAULT)
    public void defaultConfirmOrderEvent(ConfirmOrderEvent event) {


        ShopCartOrderDto shopCartOrderDto = event.getShopCartOrderDto();

        OrderParam orderParam = event.getOrderParam();

        String userId = SecurityUtils.getUser().getUserId();

        // 订单的地址信息
        UserAddr userAddr = userAddrService.getUserAddrByUserId(orderParam.getAddrId(), userId);

        double total = 0.0;

        int totalCount = 0;

        double transfee = 0.0;

        for (ShopCartItemDto shopCartItem : event.getShopCartItems()) {
            // 获取商品信息
            Product product = productService.getProductByProdId(shopCartItem.getProdId());
            // 获取sku信息
            Sku sku = skuService.getSkuBySkuId(shopCartItem.getSkuId());
            if (product == null || sku == null) {
                throw new WillShopBindException("购物车包含无法识别的商品");
            }
            if (product.getStatus() != 1 || sku.getStatus() != 1) {
                throw new WillShopBindException("商品[" + sku.getProdName() + "]已下架");
            }

            totalCount = shopCartItem.getProdCount() + totalCount;
            total = Arithmetic.add(shopCartItem.getProductTotalAmount(), total);
            // 用户地址如果为空，则表示该用户从未设置过任何地址相关信息
            if (userAddr != null) {
                // 每个产品的运费相加
                transfee = Arithmetic.add(transfee, transportManagerService.calculateTransfee(shopCartItem, userAddr));
            }

            shopCartItem.setActualTotal(shopCartItem.getProductTotalAmount());
            shopCartOrderDto.setActualTotal(Arithmetic.add(total, transfee));
            shopCartOrderDto.setTotal(total);
            shopCartOrderDto.setTotalCount(totalCount);
            shopCartOrderDto.setTransfee(transfee);
        }
    }
}
