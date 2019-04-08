package com.ema.service;

import com.ema.common.ServerResponse;
import com.ema.pojo.IncidentScndComment;
import com.ema.pojo.User;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-07 21:59
 */
public interface IIncidentScndCommentService {
    ServerResponse saveComment(IncidentScndComment incidentScndComment, User sessionUser);

    ServerResponse getCommentList(Integer commentId, int pageNum, int pageSize, User user);

    ServerResponse deleteComment(Integer id, Integer userId);

    ServerResponse thumbUpComment(Integer id, Integer userId);
}
