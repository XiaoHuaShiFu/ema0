package com.ema.controller.portal;

import com.ema.common.Const;
import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.pojo.IncidentComment;
import com.ema.pojo.User;
import com.ema.service.IIncidentCommentService;
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
     * 状态码0表示获取成功
     * 状态码1表示获取失败
     *
     * @param session 会话
     * @param id 评论的id
     * @return 返回评论的详细信息
     */
    @RequestMapping(value = "get_comment.do")
    public ServerResponse getComment(HttpSession session, Integer id) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        return iIncidentCommentService.getComment(id, sessionUser);
    }

    /**
     * 删除一个一级评论
     * 状态码0表示删除成功
     * 状态码1表示删除失败
     * 状态码10表示未登录
     *
     * @param session 会话
     * @param id 用户id
     * @param incidentId 事件id
     * @return 带状态码的响应
     */
    @RequestMapping(value = "delete.do")
    public ServerResponse deleteComment(HttpSession session, Integer id, Integer incidentId) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentCommentService.deleteComment(sessionUser, id, incidentId);
    }


    /**
     * 状态码0表示获取成功
     *
     * @param incidentId 事件id
     * @param pageNum 页码，默认1
     * @param pageSize 页大小，默认10
     * @return 带状态码的响应信息
     */
    @RequestMapping(value = "list.do")
    public ServerResponse list(Integer incidentId,
                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iIncidentCommentService.getCommentList(incidentId, pageNum, pageSize);
    }

    /**
     * 收藏一个事件
     * 如果返回状态码1表明收藏失败
     * 如果返回状态码10表明用户未登录
     * 如果返回状态码284表明收藏成功
     * 如果返回状态码285表明取消收藏成功
     *
     * 此接口如果已经收藏则取消收藏，未收藏则增加收藏
     *
     * @param session 会话
     * @param id 事件评论id
     * @return 带状态码的响应
     */
    @RequestMapping(value = "collect.do")
    public ServerResponse collectComment(HttpSession session, Integer id) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentCommentService.collectComment(sessionUser.getId(),id);
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
     * @param id 一级评论id
     * @param session 会话
     * @return 带状态码的响应
     */
    @RequestMapping(value = "thumb_up.do")
    public ServerResponse thumbUpComment(HttpSession session, Integer id) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentCommentService.thumbUpComment(sessionUser.getId(),id);
    }

    /**
     * 获取用户最近评论列表
     * 如果返回状态码0表明获取失败
     * 如果返回状态码10表明用户未登录
     *
     * @param session 会话
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户最近评论列表
     */
    @RequestMapping(value = "user_list.do")
    public ServerResponse getUserCommentList(HttpSession session,
                                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentCommentService.getUserCommentList(sessionUser.getId(), pageNum, pageSize);
    }

    /**
     * 获取用户收藏列表
     * 如果返回状态码0表明获取失败
     * 如果返回状态码10表明用户未登录
     *
     * @param session 会话
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户收藏列表
     */
    @RequestMapping(value = "collection_list.do")
    public ServerResponse getCollectionList(HttpSession session,
                                             @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentCommentService.getCollectionList(sessionUser.getId(), pageNum, pageSize);
    }

}
