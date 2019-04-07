package com.ema.service.impl;

import com.ema.common.ServerResponse;
import com.ema.dao.IncidentScndCommentMapper;
import com.ema.dao.UserMapper;
import com.ema.pojo.Incident;
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
     * @return 返回二级评论列表
     */
    public ServerResponse getCommentList(Integer incidentCommentId, int pageNum, int pageSize) {
        //一级评论id不能为null
        if (incidentCommentId == null) {
            return ServerResponse.createByErrorMessage("incident comment id can't be null");
        }
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        //查询二级评论列表
        List<IncidentScndComment> incidentScndCommentList =
                incidentScndCommentMapper.selectByIncidentCommentId(incidentCommentId);
        System.out.println(incidentScndCommentList);

        //获取用户列表
        List<Integer> userIdList = new ArrayList<>();
        List<Integer> parScndCommentIdList = new ArrayList<>();
        for (IncidentScndComment i : incidentScndCommentList) {
            userIdList.add(i.getUserId());
            parScndCommentIdList.add(i.getParScndCommentId());
        }
        System.out.println(userIdList);
        System.out.println(parScndCommentIdList);
        List<User> userList = userMapper.selectByIdList(userIdList);
        System.out.println(userList.size());

        //获取父用户列表
        List<User> parScndCommentUserList = userMapper.selectByParScndCommentIdList(parScndCommentIdList);
        System.out.println(parScndCommentUserList.size());

        //装配IncidentScndCommentVoList
        List<IncidentScndCommentVo> incidentScndCommentVoList =
                assembleIncidentScndCommentVoList(incidentScndCommentList, userList, parScndCommentUserList);
        System.out.println(incidentScndCommentVoList);
        //生成分页信息
        PageInfo pageResult = new PageInfo(incidentScndCommentVoList);
        return ServerResponse.createBySuccess(pageResult);
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
            List<IncidentScndComment> incidentScndCommentList, List<User> userList, List<User> parScndCommentUserList) {
        List<IncidentScndCommentVo> incidentScndCommentVoList = new ArrayList<>();
        for (int i = 0; i < incidentScndCommentList.size(); i++) {
            incidentScndCommentVoList.add(assembleIncidentScndCommentVo(
                    incidentScndCommentList.get(i),
                    userList.get(i),
                    parScndCommentUserList.get(i)));
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
                                                                User user, User parScndCommentUser) {
        IncidentScndCommentVo incidentScndCommentVo = new IncidentScndCommentVo();
        incidentScndCommentVo.setId(incidentScndComment.getId());
        incidentScndCommentVo.setUserVo(assembleUserVo(user));
        incidentScndCommentVo.setParScndCommentUserVo(assembleUserVo0(parScndCommentUser));
        incidentScndCommentVo.setIncidentCommentId(incidentScndComment.getIncidentCommentId());
        incidentScndCommentVo.setComment(incidentScndComment.getComment());
        incidentScndCommentVo.setThumbUps(incidentScndComment.getThumbUps());
        incidentScndCommentVo.setCommentTime(DateTimeUtil.dateToStr(incidentScndComment.getCommentTime()));
        return incidentScndCommentVo;
    }

    /**
     * 装配UserVo
     *
     * @param user
     * @return
     */
    private UserVo assembleUserVo(User user) {
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setNickName(user.getNickName());
        userVo.setAvatarUrl(user.getAvatarUrl());
        return userVo;
    }

    /**
     * 装配UserVo
     *
     * @param user
     * @return
     */
    private UserVo assembleUserVo0(User user) {
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setAvatarUrl(user.getAvatarUrl());
        return userVo;
    }
}
