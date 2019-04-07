package com.ema.service.impl;

import com.ema.common.ServerResponse;
import com.ema.dao.IncidentScndCommentMapper;
import com.ema.pojo.IncidentScndComment;
import com.ema.pojo.User;
import com.ema.service.IIncidentScndCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-07 21:59
 */
@Service("iIncidentScndCommentService")
public class IncidentScndCommentServiceImpl implements IIncidentScndCommentService{

    @Autowired
    private IncidentScndCommentMapper incidentScndCommentMapper;

    public ServerResponse saveComment(IncidentScndComment incidentScndComment, User sessionUser) {
        
    }

}
