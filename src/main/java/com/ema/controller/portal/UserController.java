package com.ema.controller.portal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-03-29 19:15
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    @RequestMapping("test.do")
    public String test() {
        System.out.println("ddddddddd");
        return "xhsf for test";
    }

}
