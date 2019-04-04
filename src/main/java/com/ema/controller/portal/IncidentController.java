package com.ema.controller.portal;

import com.ema.common.Const;
import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.pojo.Incident;
import com.ema.pojo.User;
import com.ema.service.IIncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
     * 上报事件
     *
     * @param session 会话
     * @param incident 事件pojo
     * @return
     */
    @RequestMapping(value = "report_incident.do")
    public ServerResponse reportIncident(HttpSession session, Incident incident) {
        User sessionUser = (User) session.getAttribute(Const.LOGINING_USER);
        if (sessionUser == null) {
            return ServerResponse.create(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iIncidentService.saveIncident(incident, sessionUser);
    }


}
