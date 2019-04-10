package com.ema.service.impl;

import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.dao.*;
import com.ema.pojo.ICThumbUp;
import com.ema.pojo.IncidentComment;
import com.ema.pojo.IncidentCommentCollection;
import com.ema.pojo.User;
import com.ema.service.IIncidentCommentService;
import com.ema.service.IIncidentScndCommentService;
import com.ema.util.DateTimeUtil;
import com.ema.vo.IncidentCommentVo;
import com.ema.vo.UserCollectionIncidentCommentVo;
import com.ema.vo.UserIncidentCommentVo;
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
    private IncidentCommentCollectionMapper incidentCommentCollectionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ICThumbUpMapper icThumbUpMapper;

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
        //用户评论数+1
        userMapper.incrComments(sessionUser.getId());
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
        //用户评论数-1
        userMapper.decrComments(sessionUser.getId());
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
     * 收藏
     * 如果返回状态码1表明点赞失败
     * 如果返回状态码284表明点赞成功
     * 如果返回状态码285表明取消赞成功
     *
     * @param commentId 一级评论id
     * @param userId 用户id
     * @return 带状态码的响应
     */
    public ServerResponse collectComment(Integer userId, Integer commentId) {
        //尝试让收藏数+1，如果此用户没有收藏过此一级评论的话
        int rowCount = incidentCommentMapper.incrCollections(commentId, userId);
        //如果收藏成功则把收藏的映射对加入数据库
        if (rowCount >= 1) {
            IncidentCommentCollection icc = new IncidentCommentCollection();
            icc.setUserId(userId);
            icc.setIncidentCommentId(commentId);
            incidentCommentCollectionMapper.insertSelective(icc);
            //用户收藏数+1
            userMapper.incrCollections(userId);
            return ServerResponse.create(
                    ResponseCode.COLLECTION_SUCCESS.getCode(), ResponseCode.COLLECTION_SUCCESS.getDesc());
        }

        //如果收藏失败则表明此次是取消收藏
        if (rowCount < 1) {
            //把一级评论的收藏数-1
            incidentCommentMapper.decrCollections(commentId, userId);
            //并删除收藏映射对
            incidentCommentCollectionMapper.deleteByUserIdAndCommentId(commentId, userId);
            //用户收藏数-1
            userMapper.decrCollections(userId);
            return ServerResponse.create(
                    ResponseCode.CANCEL_COLLECTION_SUCCESS.getCode(), ResponseCode.CANCEL_COLLECTION_SUCCESS.getDesc());
        }

        //收藏失败
        return ServerResponse.createByErrorMessage("collection false");
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
    public ServerResponse thumbUpComment(Integer userId, Integer id) {
        //尝试让点赞数+1，如果此用户没有点赞过此一级评论的话
        int rowCount = incidentCommentMapper.incrThumbUps(id, userId);
        //如果点赞成功则把点赞的映射对加入数据库
        if (rowCount >= 1) {
            ICThumbUp icThumbUp = new ICThumbUp();
            icThumbUp.setuId(userId);
            icThumbUp.setIcId(id);
            icThumbUpMapper.insert(icThumbUp);
            return ServerResponse.create(
                    ResponseCode.THUMB_UP_SUCCESS.getCode(), ResponseCode.THUMB_UP_SUCCESS.getDesc());
        }

        //如果点赞失败则表明此次是取消点赞
        if (rowCount < 1) {
            //把一级评论的点赞数-1
            incidentCommentMapper.decrThumbUps(id, userId);
            //并删除点赞映射对
            icThumbUpMapper.deleteByUserIdAndICId(id, userId);
            return ServerResponse.create(
                    ResponseCode.CANCEL_THUMB_UP_SUCCESS.getCode(), ResponseCode.CANCEL_THUMB_UP_SUCCESS.getDesc());
        }

        //点赞失败
        return ServerResponse.createByErrorMessage("thumb up false");
    }

    /**
     * 获取用户最近评论列表
     *
     * @param userId 用户id
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户最近评论列表
     */
    public ServerResponse getUserCommentList(Integer userId, int pageNum, int pageSize) {
        PageInfo<UserIncidentCommentVo> result = new PageInfo<>(getUserIncidentCommentVoList(userId, pageNum, pageSize));
        return ServerResponse.createBySuccess(result);
    }


    /**
     * 获取用户收藏列表
     *
     * @param userId 用户id
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户收藏列表
     */
    public ServerResponse getCollectionList(Integer userId, int pageNum, int pageSize) {
        PageInfo<UserCollectionIncidentCommentVo> result = new PageInfo<>(getUserCollectionIncidentCommentVoList(userId, pageNum, pageSize));
        return ServerResponse.createBySuccess(result);
    }


    /**
     * 获取用户最近评论列表
     *
     * @param userId 用户id
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户最近评论列表
     */
    private List<UserIncidentCommentVo> getUserIncidentCommentVoList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<IncidentComment> incidentCommentList = incidentCommentMapper.selectByUserId(userId);
        List<UserIncidentCommentVo> userIncidentCommentVoList = new ArrayList<>();
        if (incidentCommentList != null) {
            for (IncidentComment i : incidentCommentList) {
                String incidentTitle = incidentMapper.selectTitleById(i.getIncidentId());
                UserIncidentCommentVo userIncidentCommentVo = assembleUserIncidentCommentVo(i, incidentTitle);
                userIncidentCommentVoList.add(userIncidentCommentVo);
            }
        }
        return userIncidentCommentVoList;
    }



    /**
     * 获取用户收藏评论列表
     *
     * @param userId 用户id
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户收藏评论列表
     */
    private List<UserCollectionIncidentCommentVo> getUserCollectionIncidentCommentVoList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<IncidentComment> incidentCommentList = incidentCommentMapper.selectByUserId0(userId);
        List<UserCollectionIncidentCommentVo> userCollectionIncidentCommentVoList = new ArrayList<>();
        if (incidentCommentList != null) {
            for (IncidentComment i : incidentCommentList) {
                String incidentTitle = incidentMapper.selectTitleById(i.getIncidentId());
                User user = userMapper.selectByPrimaryKey(i.getUserId());
                UserCollectionIncidentCommentVo userCollectionIncidentCommentVo =
                        assembleUserCollectionIncidentCommentVo(i, incidentTitle, user);
                userCollectionIncidentCommentVoList.add(userCollectionIncidentCommentVo);
            }
        }
        return userCollectionIncidentCommentVoList;
    }

    /**
     * 装配UserCollectionIncidentCommentVo
     *
     * @param incidentComment
     * @param incidentTitle
     * @param user
     * @return
     */
    private UserCollectionIncidentCommentVo assembleUserCollectionIncidentCommentVo(
            IncidentComment incidentComment, String incidentTitle, User user) {
        UserCollectionIncidentCommentVo userCollectionIncidentCommentVo = new UserCollectionIncidentCommentVo();
        userCollectionIncidentCommentVo.setUserVo(assembleUserVo(user));
        userCollectionIncidentCommentVo.setCommentId(incidentComment.getId());
        userCollectionIncidentCommentVo.setIncidentTitle(incidentTitle);
        userCollectionIncidentCommentVo.setComment(incidentComment.getComment());
        userCollectionIncidentCommentVo.setComments(incidentComment.getComments());
        userCollectionIncidentCommentVo.setCollections(incidentComment.getCollections());
        userCollectionIncidentCommentVo.setThumbUps(incidentComment.getThumbUps());
        userCollectionIncidentCommentVo.setCommentTime(DateTimeUtil.dateToStr(incidentComment.getCommentTime()));
        return userCollectionIncidentCommentVo;
    }

    /**
     * 装配UserIncidentCommentVo
     *
     * @param incidentComment
     * @param incidentTitle
     * @return
     */
    private UserIncidentCommentVo assembleUserIncidentCommentVo(IncidentComment incidentComment, String incidentTitle) {
        UserIncidentCommentVo userIncidentCommentVo = new UserIncidentCommentVo();
        userIncidentCommentVo.setCommentId(incidentComment.getId());
        userIncidentCommentVo.setIncidentTitle(incidentTitle);
        userIncidentCommentVo.setComment(incidentComment.getComment());
        userIncidentCommentVo.setComments(incidentComment.getComments());
        userIncidentCommentVo.setCollections(incidentComment.getCollections());
        userIncidentCommentVo.setThumbUps(incidentComment.getThumbUps());
        userIncidentCommentVo.setCommentTime(DateTimeUtil.dateToStr(incidentComment.getCommentTime()));
        return userIncidentCommentVo;
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
