package com.ema.service;

import com.ema.common.ServerResponse;
import com.ema.pojo.Incident;
import com.ema.pojo.User;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-03 23:48
 */
public interface IIncidentService {
    ServerResponse saveIncident(Incident incident, User user);
}
