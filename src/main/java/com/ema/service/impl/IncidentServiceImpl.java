package com.ema.service.impl;

import com.ema.common.ServerResponse;
import com.ema.pojo.Incident;
import com.ema.pojo.User;
import com.ema.service.IIncidentService;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-03 23:49
 */
public class IncidentServiceImpl  implements IIncidentService{

    public ServerResponse saveIncident(Incident incident, User user) {
        incident.setId(null);
        incident.setUserId(user.getId());
        incident.setViews(null);
        incident.setAttentions(null);
        incident.setComments(null);
    }

}
