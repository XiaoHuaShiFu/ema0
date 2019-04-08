package com.ema.service;

import com.ema.common.ServerResponse;
import com.ema.pojo.IncidentComment;
import com.ema.pojo.User;
import com.ema.vo.IncidentScndCommentVo;

import java.util.List;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-07 20:41
 */
public interface IIncidentCommentService {
    ServerResponse saveComment(IncidentComment incidentComment, User sessionUser);

    ServerResponse getComment(int id, User user);


}
