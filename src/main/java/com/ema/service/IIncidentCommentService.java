package com.ema.service;

import com.ema.common.ServerResponse;
import com.ema.pojo.IncidentComment;
import com.ema.pojo.User;
import com.ema.vo.IncidentCommentVo;

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

    ServerResponse deleteComment(User sessionUser, int id, Integer incidentId);

    ServerResponse getCommentList(Integer incidentId, int pageNum, int pageSize);

    List<IncidentCommentVo> getIncidentCommentVoList(Integer incidentId, int pageNum, int pageSize);

    ServerResponse collectComment(Integer userId, Integer commentId);

    ServerResponse thumbUpComment(Integer userId, Integer id);
}
