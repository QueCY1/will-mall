package com.will.shop.admin.controller;

import com.will.shop.bean.model.Delivery;
import com.will.shop.common.response.ServerResponseEntity;
import com.will.shop.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物流控制
 * @author will
 */
@RestController
@RequestMapping("/admin/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

	/**
	 * 分页获取
	 */
    @GetMapping("/list")
	public ServerResponseEntity<List<Delivery>> page(){
		
		List<Delivery> list = deliveryService.list();
		return ServerResponseEntity.success(list);
	}

}
