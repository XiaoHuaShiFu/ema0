package com.ema.controller.portal;

import com.ema.common.Const;
import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.pojo.IncidentScndComment;
import com.ema.pojo.User;
import com.ema.service.IIncidentScndCommentService;
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
 * @create 2019-04-07 21:55
 */
@RestController
@RequestMapping("/incident_scnd_comment/")
public class IncidentScndCommentController {

    @Autowired
    private IIncidentScndCommentService iIncidentScndCommentService;

    /**
     * 上传二级评论
     * 状态码0表示上传失败
     * 状态码1表示上传成功
     *
     * @param incidentScndComment 二级评论pojo
     * @param session 会话
     * @return 如果上传成功返回二级评论id
     */
    @RequestMapping(value = "upload_comment.do")
    public ServerResponse uploadComment(HttpSession session, IncidentScndComment incidentScndComment) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentScndCommentService.saveComment(incidentScndComment, sessionUser);
    }

    /**
     * 获得一级评论的二级评论列表
     * 如果状态码为0表示成功
     * 如果状态码为1表示失败
     *
     * @param incidentCommentId 一级评论id
     * @param pageNum 页码
     * @param pageSize 一页条数
     * @return 返回二级评论列表
     */
    @RequestMapping(value = "list.do")
    public ServerResponse getCommentList(HttpSession session, Integer incidentCommentId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        return iIncidentScndCommentService.getCommentList(incidentCommentId, pageNum, pageSize, sessionUser);
    }

    /**
     * 删除一个评论
     *
     * @param id 二级评论的id
     * @param session 会话
     * @param commentId 一级评论id
     * @return 返回带状态码的响应
     */
    @RequestMapping(value = "delete.do")
    public ServerResponse deleteComment(HttpSession session, Integer id, Integer commentId) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentScndCommentService.deleteComment(id, sessionUser.getId(), commentId);
    }

    /**
     * 点赞
     * 如果返回状态码1表明点赞失败
     * 如果返回状态码10表明用户未登录
     * 如果返回状态码280表明点赞成功
     * 如果返回状态码281表明取消赞成功
     *
     * 此接口如果已经点赞则取消点赞，未点赞则增加点赞
     *
     * @param id 二级评论id
     * @param session 会话
     * @return 带状态码的响应
     */
    @RequestMapping(value = "thumb_up.do")
    public ServerResponse thumbUpComment(HttpSession session, Integer id) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentScndCommentService.thumbUpComment(sessionUser.getId(),id);
    }


}
