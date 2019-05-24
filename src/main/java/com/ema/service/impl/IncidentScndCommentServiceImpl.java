package com.ema.service.impl;

import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.dao.ISCThumbUpMapper;
import com.ema.dao.IncidentCommentMapper;
import com.ema.dao.IncidentScndCommentMapper;
import com.ema.dao.UserMapper;
import com.ema.pojo.IncidentScndComment;
import com.ema.pojo.User;
import com.ema.service.IIncidentScndCommentService;
import com.ema.util.DateTimeUtil;
import com.ema.vo.IncidentScndCommentVo;
import com.ema.vo.UserVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private IncidentCommentMapper incidentCommentMapper;

    @Autowired
    private ISCThumbUpMapper iscThumbUpMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 保存二级评论
     * 状态码0表示保存失败
     * 状态码1表示保存成功
     *
     * @param incidentScndComment 二级评论pojo
     * @param sessionUser 用户pojo
     * @return 如果保存成功返回二级评论id
     */
    public ServerResponse saveComment(IncidentScndComment incidentScndComment, User sessionUser) {
        if (incidentScndComment == null) {
            return ServerResponse.createByErrorMessage("This incident scnd comment is null");
        }
        incidentScndComment.setId(null);
        incidentScndComment.setThumbUps(null);
        incidentScndComment.setCommentTime(null);
        incidentScndComment.setCreateTime(null);
        incidentScndComment.setUserId(null);
        incidentScndComment.setUserId(sessionUser.getId());
        incidentScndCommentMapper.insertSelective(incidentScndComment);

        //一级评论的评论数+1
        incidentCommentMapper.incrComments(incidentScndComment.getIncidentCommentId());

        Map<String,Integer> map = new HashMap<>();
        map.put("id", incidentScndComment.getId());
        return ServerResponse.createBySuccess("save comment success", map);
    }

    /**
     * 获得一级评论的二级评论列表
     * 如果状态码为0表示成功
     * 如果状态码为1表示失败
     *
     * @param incidentCommentId 一级评论id
     * @param pageNum 页码
     * @param pageSize 一页条数
     * @param user 获取列表的用户
     * @return 返回二级评论列表
     */
    public ServerResponse getCommentList(Integer incidentCommentId, int pageNum, int pageSize, User user) {
        //一级评论id不能为null
        if (incidentCommentId == null) {
            return ServerResponse.createByErrorMessage("incident comment id can't be null");
        }
        //生成分页信息
        PageInfo<IncidentScndCommentVo> pageResult =
                new PageInfo<>(getIncidentScndCommentVoList(user, pageNum, pageSize, incidentCommentId));
        return ServerResponse.createBySuccess(pageResult);
    }

    /**
     * 删除一个评论
     *
     * @param id 二级评论的id
     * @param userId 用户的id
     * @param commentId 一级评论的id
     * @return 返回带状态码的响应
     */
    public ServerResponse deleteComment(Integer id, Integer userId, Integer commentId) {
        int rowCount = incidentScndCommentMapper.deleteByIdAndUserId(id, userId, commentId);
        if (rowCount < 1) {
            return ServerResponse.createByErrorMessage("delete false");
        }

        //一级评论的评论数-1
        incidentCommentMapper.decrComments(commentId);

        return ServerResponse.createBySuccess("delete success");
    }

    /**
     * 点赞
     * 如果返回状态码1表明点赞失败
     * 如果返回状态码280表明点赞成功
     * 如果返回状态码281表明取消赞成功
     *
     * @param id 二级评论id
     * @param userId 用户id
     * @return 带状态码的响应
     */
    public ServerResponse thumbUpComment(Integer id, Integer userId) {
        //尝试让点赞数+1，如果此用户没有点赞过此二级评论的话
        int rowCount = incidentScndCommentMapper.incrThumbUps(id, userId);
        //如果点赞成功则把点赞的映射对加入数据库
        System.out.println(rowCount);
        if (rowCount >= 1) {
            iscThumbUpMapper.insert(userId, id);
            return ServerResponse.create(
                    ResponseCode.THUMB_UP_SUCCESS.getCode(), ResponseCode.THUMB_UP_SUCCESS.getDesc());
        }

        //如果点赞失败则表明此次是取消点赞
        if (rowCount < 1) {
            //把二级评论的点赞数-1
            incidentScndCommentMapper.decrThumbUps(id, userId);
            //并删除点赞映射对
            iscThumbUpMapper.deleteByUserIdAndISCId(id, userId);
            return ServerResponse.create(
                    ResponseCode.CANCEL_THUMB_UP_SUCCESS.getCode(), ResponseCode.CANCEL_THUMB_UP_SUCCESS.getDesc());
        }

        //点赞失败
        return ServerResponse.createByErrorMessage("thumb up false");
    }


    /**
     * 获取事件二级评论列表，带分页
     *
     * @param user 获取此列表的用户pojo
     * @param pageNum 页码
     * @param pageSize 页条数
     * @param incidentCommentId 一级评论id
     * @return IncidentScndCommentVoList
     */
    public List<IncidentScndCommentVo> getIncidentScndCommentVoList(User user, int pageNum, int pageSize, Integer incidentCommentId) {
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        //查询二级评论列表
        List<IncidentScndComment> incidentScndCommentList =
                incidentScndCommentMapper.selectByIncidentCommentId(incidentCommentId);

        //获取用户列表
        List<Integer> userIdList = new ArrayList<>();
        //父二级评论id列表
        List<Integer> parScndCommentIdList = new ArrayList<>();
        for (IncidentScndComment i : incidentScndCommentList) {
            userIdList.add(i.getUserId());
            parScndCommentIdList.add(i.getParScndCommentId());
        }
        List<User> userList = new ArrayList<>();
        for (Integer i : userIdList) {
            userList.add(userMapper.selectByPrimaryKey(i));
        }

        //获取父用户列表
        List<User> parScndCommentUserList = new ArrayList<>();
        for (Integer i : parScndCommentIdList) {
            parScndCommentUserList.add(userMapper.selectByParScndCommentId(i));
        }

        //二级评论点赞状态列表
        List<Boolean> isThumbUpList = null;
        if (user != null) {
            isThumbUpList = new ArrayList<>();
            for (IncidentScndComment i : incidentScndCommentList) {
                int rowCount = iscThumbUpMapper.selectCountByUserIdAndISCId(user.getId(), i.getId());
                if (rowCount >=  1) {
                    isThumbUpList.add(true);
                } else {
                    isThumbUpList.add(false);
                }
            }
        }

        //装配IncidentScndCommentVoList
        List<IncidentScndCommentVo> incidentScndCommentVoList =
                assembleIncidentScndCommentVoList(incidentScndCommentList, userList,
                        parScndCommentUserList, isThumbUpList);
        return incidentScndCommentVoList;
    }

    /**
     * 装配IncidentScndCommentVoList
     *
     * @param incidentScndCommentList
     * @param userList
     * @param parScndCommentUserList
     * @return
     */
    private List<IncidentScndCommentVo> assembleIncidentScndCommentVoList(
            List<IncidentScndComment> incidentScndCommentList,
            List<User> userList, List<User> parScndCommentUserList,
            List<Boolean> isThumbUpList) {
        List<IncidentScndCommentVo> incidentScndCommentVoList = new ArrayList<>();
        for (int i = 0; i < incidentScndCommentList.size(); i++) {
            incidentScndCommentVoList.add(assembleIncidentScndCommentVo(
                    incidentScndCommentList.get(i),
                    userList.get(i),
                    parScndCommentUserList.get(i), isThumbUpList != null ? isThumbUpList.get(i) : null));
        }
        return incidentScndCommentVoList;
    }

    /**
     * 装配IncidentScndCommentVo
     *
     * @param incidentScndComment
     * @param user
     * @param parScndCommentUser
     * @return
     */
    private IncidentScndCommentVo assembleIncidentScndCommentVo(IncidentScndComment incidentScndComment,
                                                                User user, User parScndCommentUser, Boolean isThumbUp) {
        IncidentScndCommentVo incidentScndCommentVo = new IncidentScndCommentVo();
        incidentScndCommentVo.setId(incidentScndComment.getId());
        incidentScndCommentVo.setUserVo(assembleUserVo(user));
        incidentScndCommentVo.setParScndCommentUserVo(assembleUserVo(parScndCommentUser));
        incidentScndCommentVo.setIncidentCommentId(incidentScndComment.getIncidentCommentId());
        incidentScndCommentVo.setComment(incidentScndComment.getComment());
        incidentScndCommentVo.setThumbUps(incidentScndComment.getThumbUps());
        incidentScndCommentVo.setCommentTime(DateTimeUtil.dateToStr(incidentScndComment.getCommentTime()));
        incidentScndCommentVo.setThumbUp(isThumbUp != null ? isThumbUp : false);
        return incidentScndCommentVo;
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
