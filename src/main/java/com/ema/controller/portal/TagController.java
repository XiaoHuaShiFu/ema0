package com.ema.controller.portal;

import com.ema.common.Const;
import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.pojo.IncidentScndComment;
import com.ema.pojo.User;
import com.ema.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-09 19:17
 */
@RestController
@RequestMapping("/tag/")
public class TagController {

    @Autowired
    private ITagService iTagService;

    /**
     * 上次一个事件标签
     * 状态码0表示上传成功
     * 状态码1表示上传失败
     * 状态码10表示用户未登录
     *
     * @param session 会话
     * @param tagName 标签名字
     * @return 带状态码的响应信息
     */
    @RequestMapping(value = "upload_tag.do")
    public ServerResponse uploadTag(HttpSession session, String tagName) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iTagService.saveTag(tagName);
    }

    /**
     * 获取标签列表
     * 状态码0表示获取成功
     *
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 标签列表
     */
    @RequestMapping(value = "list.do")
    public ServerResponse getTagList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iTagService.getTagList(pageNum, pageSize);
    }

}
