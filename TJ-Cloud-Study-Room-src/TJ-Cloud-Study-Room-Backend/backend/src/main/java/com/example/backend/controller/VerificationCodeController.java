package com.example.backend.controller;

import com.example.backend.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apistd.uni.Uni;
import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniSMS;
import com.apistd.uni.sms.UniMessage;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping()
public class VerificationCodeController {

    public static String ACCESS_KEY_ID = "RQ23bm31a3ksa8hs8Qdjw9pEn2u7bJGJoi2JY4K5gc8No9GHj";

    @PostMapping("/vcode")  // 上传
    public Result<?> sendVCode(@RequestParam String num) {
        // 初始化
        Uni.init(ACCESS_KEY_ID); // 若使用简易验签模式仅传入第一个参数即可

        Long vCode = Math.round(Math.random() * (999999 - 100000) + 100000);

        // 设置自定义参数 (变量短信)
        Map<String, String> templateData = new HashMap<String, String>();
        templateData.put("code", String.valueOf(vCode));

        // 构建信息
        UniMessage message = UniSMS.buildMessage()
                .setTo(num)
                .setSignature("张润泽测试")
                .setTemplateId("pub_verif_identity")
                .setTemplateData(templateData);

        // 发送短信
        try {
            UniResponse res = message.send();
            System.out.println(res);
        } catch (UniException e) {
            System.out.println("Error: " + e);
            System.out.println("RequestId: " + e.requestId);
        }

        return Result.success(templateData.get("code"));
    }
}
