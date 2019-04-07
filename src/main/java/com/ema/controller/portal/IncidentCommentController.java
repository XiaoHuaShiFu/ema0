package com.ema.controller.portal;

import com.ema.common.Const;
import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.pojo.IncidentComment;
import com.ema.pojo.User;
import com.ema.service.IIncidentCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-07 17:46
 */
@RestController
@RequestMapping("/incident_comment/")
public class IncidentCommentController {

    @Autowired
    private IIncidentCommentService iIncidentCommentService;

    /**
     * 上传一个事件的评论
     * 状态码0表示上传成功
     * 状态码1表示上传失败
     *
     * @param incidentComment 评论pojo
     * @param session 会话
     * @return 如果成功会返回评论的id
     */
    @RequestMapping(value = "upload_comment.do")
    public ServerResponse uploadComment(HttpSession session, IncidentComment incidentComment) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentCommentService.saveComment(incidentComment, sessionUser);
    }

    /**
     * 获得一个一级评论的详细信息
     *
     * @param session 会话
     * @param id 评论的id
     * @return 返回评论的详细信息
     */
    @RequestMapping(value = "get_comment.do")
    public ServerResponse getComment(HttpSession session, int id) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentCommentService.getComment(id);
    }

}
