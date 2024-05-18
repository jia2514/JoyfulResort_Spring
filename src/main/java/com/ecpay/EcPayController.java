package com.ecpay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joyfulresort.reserveorder.model.ResService;
import com.joyfulresort.reserveorder.model.ResVO;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@RestController
public class EcPayController {
//	一般信用卡測試卡號 :
//	 4311-9522-2222-2222 安全碼 : 222
	@Autowired
	ResService resService;
	ResVO resVO = new ResVO();

	private static AllInOne all;

	public EcPayController() {
		all = new AllInOne("");
	}

	@RequestMapping("/pay")
	public String pay(Model model) {
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String timeString = time.format(formatter);

		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setChoosePayment("Credit");
		obj.setMerchantTradeNo(uuId);
		obj.setMerchantTradeDate("2017/01/01 08:05:23");
		obj.setTotalAmount("101"); // 價錢
		obj.setTradeDesc("我是描述");
		obj.setItemName("我是商品名稱");
		obj.setReturnURL("<https://www.google.com.tw/>");
		obj.setClientBackURL("http://localhost:8080/joyfulresort");
		obj.setNeedExtraPaidInfo("N");
		String form = all.aioCheckOut(obj, null);
		model.addAttribute("form", form);

		return form;

	}

}
