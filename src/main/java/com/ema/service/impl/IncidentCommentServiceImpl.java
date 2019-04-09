package com.ema.service.impl;

import com.ema.common.ServerResponse;
import com.ema.dao.IncidentCommentMapper;
import com.ema.dao.IncidentMapper;
import com.ema.dao.UserMapper;
import com.ema.pojo.IncidentComment;
import com.ema.pojo.User;
import com.ema.service.IIncidentCommentService;
import com.ema.service.IIncidentScndCommentService;
import com.ema.util.DateTimeUtil;
import com.ema.vo.IncidentCommentVo;
import com.ema.vo.UserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-07 20:41
 */
@Service("iIncidentCommentService")
public class IncidentCommentServiceImpl implements IIncidentCommentService{

    @Autowired
    private IncidentMapper incidentMapper;

    @Autowired
    private IncidentCommentMapper incidentCommentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IIncidentScndCommentService iIncidentScndCommentService;

    /**
     * 保存一个事件的评论
     * 状态码0表示保存成功
     * 状态码1表示保存失败
     *
     * @param incidentComment 评论pojo
     * @param sessionUser 用户pojo
     * @return 如果成功会返回评论的id
     */
    public ServerResponse saveComment(IncidentComment incidentComment, User sessionUser) {
        if (incidentComment == null) {
            return ServerResponse.createByError("argument can't be null");
        }
        incidentComment.setId(null);
        incidentComment.setComments(null);
        incidentComment.setCollections(null);
        incidentComment.setThumbUps(null);
        incidentComment.setMainImageUrl(null);
        incidentComment.setCreateTime(null);
        incidentComment.setUpdateTime(null);
        incidentComment.setCommentTime(null);
        incidentComment.setUserId(sessionUser.getId());
        //插入评论
        incidentCommentMapper.insertSelective(incidentComment);
        //事件的评论数+1
        incidentMapper.incrComments(incidentComment.getIncidentId());
        return ServerResponse.createBySuccess("upload comment success", incidentComment.getId());
    }


    /**
     * 获取事件评论详情
     *
     * @param id 评论id
     * @param sessionUser 获取本一级评论的用户pojo
     * @return 如果成功返回评论的详细信息
     */
    public ServerResponse getComment(int id, User sessionUser) {
        IncidentComment incidentComment = incidentCommentMapper.selectByPrimaryKey(id);
        if (incidentComment == null) {
            return ServerResponse.createByError("This incident comment not exist");
        }
        User user = userMapper.selectByPrimaryKey(incidentComment.getUserId());
        IncidentCommentVo incidentCommentVo = assembleIncidentCommentVo(incidentComment, user);
        incidentCommentVo.setIncidentScndCommentVoList(
                iIncidentScndCommentService.getIncidentScndCommentVoList(sessionUser, 1, 2, id));
        return ServerResponse.createBySuccess(incidentCommentVo);
    }

    /**
     * 删除一个一级评论
     *
     * @param sessionUser 发出删除请求的用户pojo
     * @param id 一级评论id
     * @param incidentId
     * @return 带状态码的响应
     */
    public ServerResponse deleteComment(User sessionUser, int id, Integer incidentId) {
        int rowCount = incidentCommentMapper.deleteByIdAndUserId(sessionUser.getId(), id);
        //删除评论失败
        if (rowCount < 1) {
            return ServerResponse.createByErrorMessage("delete comment false");
        }
        //事件的评论数-1
        incidentMapper.decrComments(incidentId);
        return ServerResponse.createBySuccess("delete comment success");
    }

    /**
     * 获取一个事件的一级评论简略列表
     *
     * @param incidentId 事件id
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 简略评论列表
     */
    public ServerResponse getCommentList(Integer incidentId, int pageNum, int pageSize) {
        PageInfo<IncidentCommentVo> result = new PageInfo<>(getIncidentCommentVoList(incidentId, pageNum, pageSize));
        return ServerResponse.createBySuccess(result);
    }

    /**
     * 获取事件一级评论的简略信息列表
     *
     * @param incidentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<IncidentCommentVo> getIncidentCommentVoList(Integer incidentId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<IncidentComment> incidentCommentList = incidentCommentMapper.selectByIncidentId(incidentId);
        List<IncidentCommentVo> incidentCommentVoList = new ArrayList<>();
        if (incidentCommentList != null) {
            for (IncidentComment i : incidentCommentList) {
                User user = userMapper.selectByPrimaryKey(i.getUserId());
                IncidentCommentVo incidentCommentVo = assembleIncidentCommentVo(i, user);
                incidentCommentVoList.add(incidentCommentVo);
            }
        }
        return incidentCommentVoList;
    }

    /**
     * 装配IncidentCommentVo
     *
     * @param incidentComment
     * @param user
     * @return
     */
    private IncidentCommentVo assembleIncidentCommentVo(IncidentComment incidentComment, User user) {
        IncidentCommentVo incidentCommentVo = new IncidentCommentVo();
        incidentCommentVo.setId(incidentComment.getId());
        incidentCommentVo.setUserVo(assembleUserVo(user));
        incidentCommentVo.setIncidentId(incidentComment.getIncidentId());
        incidentCommentVo.setAnon(incidentComment.getAnon());
        incidentCommentVo.setComment(incidentComment.getComment());
        incidentCommentVo.setComments(incidentComment.getComments());
        incidentCommentVo.setCollections(incidentComment.getCollections());
        incidentCommentVo.setThumbUps(incidentComment.getThumbUps());
        incidentCommentVo.setCommentTime(DateTimeUtil.dateToStr(incidentComment.getCommentTime()));
        incidentCommentVo.setMainImageUrl(incidentComment.getMainImageUrl());
        return incidentCommentVo;
    }

    /**
     * 装配UserVo
     *
     * @param user
     * @return
     */
    private UserVo assembleUserVo(User user) {
        if (user == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setNickName(user.getNickName());
        userVo.setAvatarUrl(user.getAvatarUrl());
        return userVo;
    }

}
