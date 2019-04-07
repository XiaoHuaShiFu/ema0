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

    @RequestMapping(value = "get_incident.do")
    public ServerResponse getIncident(HttpSession session, int id) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentService.getIncident(id);
    }


}
