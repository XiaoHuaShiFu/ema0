package com.ema.controller.portal;

import com.ema.common.Const;
import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.pojo.IncidentScndComment;
import com.ema.pojo.User;
import com.ema.service.IIncidentScndCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-07 21:55
 */
@RestController
@RequestMapping("/incident_scnd_comment/")
public class IncidentScndCommentController {

    @Autowired
    private IIncidentScndCommentService iIncidentScndCommentService;

    @RequestMapping(value = "upload_comment.do")
    public ServerResponse uploadComment(HttpSession session, IncidentScndComment incidentScndComment) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentScndCommentService.saveComment(incidentScndComment, sessionUser);
    }

}
