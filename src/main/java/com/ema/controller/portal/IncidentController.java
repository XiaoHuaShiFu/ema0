package com.ema.controller.portal;

import com.ema.common.Const;
import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.pojo.Incident;
import com.ema.pojo.User;
import com.ema.service.IIncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-03 23:06
 */
@RestController
@RequestMapping("/incident/")
public class IncidentController {

    @Autowired
    private IIncidentService iIncidentService;

    /**
     * 上传事件
     * 状态码0表示保存事件成功
     * 状态码1表示保存事件失败
     * 状态码10表示用户未登录
     *
     * @param incident 事件pojo
     * @param tags 标签id数组
     * @param session 会话
     * @return 带状态码的响应,如果成功会返回事件的id
     */
    @RequestMapping(value = "upload_incident.do")
    public ServerResponse uploadIncident(HttpSession session, int[] tags, Incident incident) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentService.saveIncident(incident, sessionUser, tags);
    }

    /**
     * 获取一个事件的详细信息
     * 状态码0表示获取成功
     * 状态码1表示获取失败
     *
     * @param session 会话
     * @param id 事件id
     * @return 返回事件详情列表
     */
    @RequestMapping(value = "get_incident.do")
    public ServerResponse getIncident(HttpSession session, Integer id) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        return iIncidentService.getIncident(id, sessionUser == null ?  0 : sessionUser.getId());
    }

    /**
     * 删除一个事件
     * 状态码0表示删除成功
     * 状态码1表示删除失败
     * 状态码10表示用户未登录
     *
     * @param session 会话
     * @param id 事件id
     * @return 带状态码的响应信息
     */
    @RequestMapping(value = "delete.do")
    public ServerResponse delete(HttpSession session, Incident id) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentService.deleteIncident(sessionUser, id);
    }

    /**
     * 获取事件简略信息列表
     * 状态码0表示获取成功
     *
     * @param session 会话
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 事件简略信息列表
     */
    @RequestMapping(value = "list.do")
    public ServerResponse getIncidentList(HttpSession session,
                                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        return iIncidentService.getIncidentList(pageNum, pageSize, sessionUser == null ?  0 : sessionUser.getId());
    }

    /**
     * 更新一个事件
     * 状态码0表示更新成功
     * 状态码1表示更新失败
     * 状态码10表示用户未登录
     *
     * @param session 会话
     * @param incident 事件pojo
     * @return 带状态码的响应信息
     */
    @RequestMapping(value = "update.do")
    public ServerResponse updateIncident(HttpSession session, Incident incident) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentService.updateIncident(sessionUser, incident);
    }

    /**
     * 上传事件主图
     * 状态码0表示上传成功
     * 状态码1表示上传失败
     * 状态码10表示用户未登录
     *
     * @param session 会话
     * @param mainImage 主题
     * @param request 请求
     * @param incidentId 事件id
     * @return 带状态码的响应
     */
    @RequestMapping(value = "upload_main_image.do")
    public ServerResponse updateImage(HttpSession session, MultipartFile mainImage, HttpServletRequest request, Integer incidentId) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload"); //会创建在webapp文件夹下
        return iIncidentService.uploadMainImage(path, mainImage, sessionUser.getId(), incidentId);
    }

    /**
     * 上传事件视频
     * 状态码0表示上传成功
     * 状态码1表示上传失败
     * 状态码10表示用户未登录
     *
     * @param session 会话
     * @param mainVideo 主题
     * @param request 请求
     * @param incidentId 事件id
     * @return 带状态码的响应
     */
    @RequestMapping(value = "upload_main_video.do")
    public ServerResponse updateVideo(HttpSession session, MultipartFile mainVideo, HttpServletRequest request, Integer incidentId) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload"); //会创建在webapp文件夹下
        return iIncidentService.uploadMainVideo(path, mainVideo, sessionUser.getId(), incidentId);
    }

    /**
     * 关注
     * 如果返回状态码1表明关注失败
     * 如果返回状态码10表明用户未登录
     * 如果返回状态码282表明关注成功
     * 如果返回状态码283表明取消关注成功
     *
     * 此接口如果已经关注则取消关注，未关注则增加关注
     *
     * @param id 事件id
     * @param session 会话
     * @return 带状态码的响应
     */
    @RequestMapping(value = "attention.do")
    public ServerResponse attentionIncident(HttpSession session, Integer id) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentService.attentionIncident(sessionUser.getId(), id);
    }

    /**
     * 浏览，添加浏览记录
     * 如果返回状态码0表明添加成功
     * 如果返回状态码10表明用户未登录
     *
     * @param session 会话
     * @param id 事件id
     * @return 带状态码的响应
     */
    @RequestMapping(value = "view.do")
    public ServerResponse viewIncident(HttpSession session, Integer id) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentService.viewIncident(sessionUser.getId(), id);
    }

    /**
     * 获取用户最近的浏览列表
     * 如果返回状态码0表明获取成功
     * 如果返回状态码10表明用户未登录
     *
     * @param session 会话
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户最近浏览列表
     */
    @RequestMapping(value = "view_list.do")
    public ServerResponse getViewList(HttpSession session,
                                      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentService.getViewList(sessionUser.getId(), pageNum, pageSize);
    }

    /**
     * 获取用户关注的事件列表
     * 如果返回状态码0表明获取成功
     * 如果返回状态码10表明用户未登录
     *
     * @param session 会话
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户关注的事件列表
     */
    @RequestMapping(value = "attention_list.do")
    public ServerResponse getAttentionList(HttpSession session,
                                      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentService.getAttentionList(sessionUser.getId(), pageNum, pageSize);
    }

    /**
     * 获取用户发布的事件列表
     * 如果返回状态码0表明获取成功
     * 如果返回状态码10表明用户未登录
     *
     * @param session 会话
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户发布的事件列表
     */
    @RequestMapping(value = "report_list.do")
    public ServerResponse getReportList(HttpSession session,
                                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentService.getReportList(sessionUser.getId(), pageNum, pageSize);
    }

    /**
     * 通过标题搜索事件
     * 如果返回状态码0表明搜索成功
     *
     * @param title 事件标题
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 符合条件的搜索结果
     */
    @RequestMapping(value = "search.do")
    public ServerResponse getReportList(HttpSession session, String title,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        return iIncidentService.getIncidentListByTitle(
                title, pageNum, pageSize, sessionUser == null ?  0 : sessionUser.getId());
    }

}
