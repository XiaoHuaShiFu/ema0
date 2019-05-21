package com.ema.service.impl;

import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.dao.*;
import com.ema.pojo.*;
import com.ema.service.IFileService;
import com.ema.service.IIncidentCommentService;
import com.ema.service.IIncidentService;
import com.ema.util.DateTimeUtil;
import com.ema.util.PropertiesUtil;
import com.ema.util.StringUtil0;
import com.ema.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import java.util.*;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-03 23:49
 */
@Service("iIncidentService")
public class IncidentServiceImpl implements IIncidentService{

    @Autowired
    private TagIncidentMapMapper tagIncidentMapMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private IncidentMapper incidentMapper;

    @Autowired
    private IncidentAttentionMapper incidentAttentionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IncidentViewMapper incidentViewMapper;

    @Autowired
    private IIncidentCommentService iIncidentCommentService;

    @Autowired
    private IFileService iFileService;

    /**
     * 状态码0表示保存事件成功
     * 状态码1表示保存事件失败
     *
     * @param incident 事件pojo
     * @param user 用户
     * @param tags 标签id数组
     * @return 带状态码的响应,如果成功会返回事件的id
     */
    public ServerResponse saveIncident(Incident incident, User user, int[] tags) {
        //事件为空，直接返回错误信息
        if (incident == null) {
            return ServerResponse.createByError("incident can't be blank");
        }

        //处理事件信息，防止用户偷添加
        incident.setId(null);
        incident.setViews(null);
        incident.setAttentions(null);
        incident.setComments(null);
        incident.setMainImageUrl(null);
        incident.setMainVideoUrl(null);
        incident.setCreateTime(null);
        incident.setUpdateTime(null);
        incident.setUserId(user.getId()); //把用户id添加到事件的发布者id上

        //插入事件
        incidentMapper.insertSelective(incident);

        //把事件的标签插入标签和事件的映射表里
        tagIncidentMapMapper.insertByIncidentIdAndTagId(incident.getId(), tags);
        //把标签的引用数都+1
        tagMapper.incrNumByTagIds(tags);

        //把事件的id返回出去
        Map<String, Integer> map = new HashMap<>();
        map.put("id", incident.getId());
        return ServerResponse.createBySuccess("save incident success", map);
    }


    /**
     * 更新事件
     *
     * @param sessionUser 发出请求的用户pojo
     * @param incident 事件pojo
     * @return 如果成功返回事件更新后的信息
     */
    public ServerResponse updateIncident(User sessionUser, Incident incident) {
        //事件为空，直接返回错误信息
        if (incident == null) {
            return ServerResponse.createByErrorMessage("incident can't be blank");
        }

        //检查该事件是否是此人上传的
        int rowCount = incidentMapper.selectCountByUserIdAndId(sessionUser.getId(), incident.getId());
        if (rowCount < 1) {
            return ServerResponse.createByErrorMessage("update incident false");
        }

        //处理事件信息，防止用户偷添加
        incident.setViews(null);
        incident.setAttentions(null);
        incident.setComments(null);
        incident.setMainImageUrl(null);
        incident.setMainVideoUrl(null);
        incident.setCreateTime(null);
        incident.setUpdateTime(null);

        //更新事件
        incidentMapper.updateByPrimaryKeySelective(incident);

        //把更新后的事件返回出去
        return ServerResponse.createBySuccess(getIncident(incident.getId()));
    }

    /**
     * 获取一个事件的详细信息,包括评论一级评论的简略信息
     *
     * @param id 事件id
     * @return
     */
    public ServerResponse  getIncident(int id) {
        Incident incident = incidentMapper.selectByPrimaryKey(id);
        //事件不存在
        if (incident == null) {
            return ServerResponse.createByErrorMessage("This incident not exist");
        }
        User user = userMapper.selectByPrimaryKey(incident.getUserId());
        List<Tag> tagList = tagMapper.selectTagListByIncidentId(incident.getId());
        List<IncidentCommentVo> incidentCommentVoList = iIncidentCommentService.getIncidentCommentVoList(id, 1, 10);
        //装配事件详情Vo
        IncidentVo incidentVo = assembleIncidentVo(incident, user, tagList, incidentCommentVoList);
        return ServerResponse.createBySuccess(incidentVo);
    }

    /**
     * 删除一个事件
     *
     * @param sessionUser 发出请求的用户pojo
     * @param id 事件id
     * @return 带状态码的响应信息
     */
    public ServerResponse deleteIncident(User sessionUser, Incident id) {
        int rowCount = incidentMapper.deleteByIdAndUserId(sessionUser.getId(), id);
        //删除失败，可能因为用户越权等
        if (rowCount < 1) {
            return ServerResponse.createByErrorMessage("delete incident false");
        }
        return ServerResponse.createBySuccess("delete incident success");
    }

    /**
     * 获取事件列表
     * 状态码0表示获取成功
     *
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 事件简略信息列表
     */
    public ServerResponse getIncidentList(int pageNum, int pageSize) {
        PageInfo<IncidentVo> result = new PageInfo<>(getIncidentVoList(pageNum, pageSize));
        return ServerResponse.createBySuccess(result);
    }

    /**
     * 上传事件图片
     *
     * @param path 路径
     * @param mainImage 图片
     * @param userId 用户id
     * @param incidentId 事件id
     * @return 带状态码的响应
     */
    public ServerResponse uploadMainImage(String path, MultipartFile mainImage, Integer userId, Integer incidentId) {
        //检查该事件是否是此人上传的
        int rowCount = incidentMapper.selectCountByUserIdAndId(userId, incidentId);
        if (rowCount < 1) {
            return ServerResponse.createByErrorMessage("upload main image false");
        }

        //开始上传事件
        String directory = "img/incident/"; //文件夹路径
        String imageName = iFileService.upload(mainImage, path, directory, UUID.randomUUID().toString()); //新头像名称
        //上传文件失败
        if (StringUtils.isBlank(imageName)) {
            return ServerResponse.createByErrorMessage("upload main image false");
        }
        //拼接图片url
        PropertiesUtil.setProps("ema.properties");
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + directory  + imageName;
        Incident incident = new Incident();
        incident.setId(incidentId);
        incident.setMainImageUrl(url);
        incidentMapper.updateByPrimaryKeySelective(incident);
        return ServerResponse.createBySuccess("upload main image success");
    }

    /**
     * 上传事件视频
     *
     * @param path 路径
     * @param mainVideo 视频
     * @param userId 用户id
     * @param incidentId 事件id
     * @return 带状态码的响应
     */
    public ServerResponse uploadMainVideo(String path, MultipartFile mainVideo, Integer userId, Integer incidentId) {
        //检查该事件是否是此人上传的
        int rowCount = incidentMapper.selectCountByUserIdAndId(userId, incidentId);
        if (rowCount < 1) {
            return ServerResponse.createByErrorMessage("upload main video false");
        }

        //开始上传事件
        String directory = "video/incident/"; //文件夹路径
        String videoName = iFileService.upload(mainVideo, path, directory, UUID.randomUUID().toString()); //新头像名称
        //上传文件失败
        if (StringUtils.isBlank(videoName)) {
            return ServerResponse.createByErrorMessage("upload main video false");
        }
        //拼接视频url
        PropertiesUtil.setProps("ema.properties");
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + directory  + videoName;
        Incident incident = new Incident();
        incident.setId(incidentId);
        incident.setMainVideoUrl(url);
        incidentMapper.updateByPrimaryKeySelective(incident);
        return ServerResponse.createBySuccess("upload main video success");
    }

    /**
     * 关注，让关注数+1或-1，且增加或删除关注映射对
     * 如果返回状态码1表明关注失败
     * 如果返回状态码10表明用户未登录
     * 如果返回状态码282表明关注成功
     * 如果返回状态码283表明取消关注成功
     *
     * @param id 事件id
     * @param userId 发起请求的用户的id
     * @return 带状态码的响应
     */
    public ServerResponse attentionIncident(Integer userId, Integer id) {
        //尝试让关注数+1，如果此用户没有关注过此事件的话
        int rowCount = incidentMapper.incrAttentions(id, userId);
        //如果关注成功则把关注的映射对加入数据库
        if (rowCount >= 1) {
            incidentAttentionMapper.insert(userId, id);
            //用户关注数+1
            userMapper.incrAttentions(userId);
            return ServerResponse.create(
                    ResponseCode.ATTENTION_SUCCESS.getCode(), ResponseCode.ATTENTION_SUCCESS.getDesc());
        }

        //如果关注失败则表明此次是取消关注
        if (rowCount < 1) {
            //把事件的关注数-1
            incidentMapper.decrAttentions(id, userId);
            //并删除关注映射对
            incidentAttentionMapper.deleteByUserIdAndIncidentId(id, userId);
            //用户关注数-1
            userMapper.decrAttentions(userId);
            return ServerResponse.create(
                    ResponseCode.CANCEL_ATTENTION_SUCCESS.getCode(), ResponseCode.CANCEL_ATTENTION_SUCCESS.getDesc());
        }

        //关注失败
        return ServerResponse.createByErrorMessage("attention false");
    }

    /**
     * 让文章的浏览时
     *
     * @param userId 用户id
     * @param incidentId 事件id
     * @return 带状态码的响应
     */
    public ServerResponse viewIncident(Integer userId, Integer incidentId) {
        //事件浏览数+1
        int rowCount = incidentMapper.incrViewsByIncidentId(incidentId);

        //事件不存在
        if (rowCount < 1) {
            return ServerResponse.createBySuccess("this incident not exist");
        }

        //查找用户是否已经浏览过，如果浏览过就不插入映射对
        rowCount = incidentViewMapper.selectCountByUserIdAndIncidentId(userId, incidentId);
        if (rowCount >= 1) {
            return ServerResponse.createBySuccess("view success");
        }
        //插入事件和用户id的映射对
        IncidentView incidentView = new IncidentView();
        incidentView.setUserId(userId);
        incidentView.setIncidentId(incidentId);
        incidentViewMapper.insert(incidentView);
        //把用户浏览数+1
        userMapper.incrViews(userId);
        return ServerResponse.createBySuccess("view success");
    }

    /**
     * 获取用户的最近浏览事件列表
     *
     * @param userId 发出请求的用户id
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 事件浏览列表
     */
    public ServerResponse getViewList(Integer userId, int pageNum, int pageSize) {
        PageInfo<IncidentViewVo> result = new PageInfo<>(getIncidentViewVoList(userId, pageNum, pageSize));
        return ServerResponse.createBySuccess(result);
    }

    /**
     * 获取用户关注的事件列表
     *
     * @param userId 发出请求的用户id
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户关注的事件列表
     */
    public ServerResponse getAttentionList(Integer userId, int pageNum, int pageSize) {
        PageInfo<UserAttentionIncidentVo> result = new PageInfo<>(
                getUserAttentionIncidentVoList(userId, pageNum, pageSize));
        return ServerResponse.createBySuccess(result);
    }

    /**
     * 获取用户发布的事件列表
     *
     * @param userId 发出请求的用户id
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 用户用户发布的事件列表
     */
    public ServerResponse getReportList(Integer userId, int pageNum, int pageSize) {
        PageInfo<UserReportIncidentVo> result = new PageInfo<>(
                getUserReportIncidentVoList(userId, pageNum, pageSize));
        return ServerResponse.createBySuccess(result);
    }

    /**
     * 获取事件列表，通过标题，模糊匹配
     * @param title 文章标题
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 返回搜索的结果列表
     */
    public ServerResponse getIncidentListByTitle(String title, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Incident> incidentList = incidentMapper.selectListByTitle("%" + title + "%");
        List<IncidentVo> incidentVoList = assembleIncidentVoList(incidentList);
        PageInfo<IncidentVo> result = new PageInfo<>(incidentVoList);
        return ServerResponse.createBySuccess(result);
    }

    /**
     * 获取UserReportIncidentVoList
     *
     * @param userId 发出请求的用户id
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return UserReportIncidentVoList
     */
    private List<UserReportIncidentVo> getUserReportIncidentVoList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Incident> incidentList = incidentMapper.selectByUserId0(userId);
        if (incidentList == null) {
            return null;
        }
        List<UserReportIncidentVo> userReportIncidentVoList = new ArrayList<>();
        for (Incident i : incidentList) {
            UserReportIncidentVo userReportIncidentVo = assembleUserReportIncidentVo(i);
            userReportIncidentVoList.add(userReportIncidentVo);
        }
        return userReportIncidentVoList;
    }

    /**
     * 装配UserReportIncidentVo
     *
     * @param incident
     * @return
     */
    private UserReportIncidentVo assembleUserReportIncidentVo(Incident incident) {
        UserReportIncidentVo userReportIncidentVo = new UserReportIncidentVo();
        userReportIncidentVo.setId(incident.getId());
        userReportIncidentVo.setViews(incident.getViews());
        userReportIncidentVo.setAttentions(incident.getAttentions());
        userReportIncidentVo.setComments(incident.getComments());
        userReportIncidentVo.setTitle(incident.getTitle());
        return userReportIncidentVo;
    }

    /**
     * 获取UserAttentionIncidentVoList
     *
     * @param userId 发出请求的用户id
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return UserAttentionIncidentVoList
     */
    private List<UserAttentionIncidentVo> getUserAttentionIncidentVoList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Incident> incidentList = incidentMapper.selectByUserId(userId);
        if (incidentList == null) {
            return null;
        }
        List<UserAttentionIncidentVo> userAttentionIncidentVoList = new ArrayList<>();
        for (Incident i : incidentList) {
            UserAttentionIncidentVo userAttentionIncidentVo = assembleUserAttentionIncidentVo(i);
            userAttentionIncidentVoList.add(userAttentionIncidentVo);
        }
        return userAttentionIncidentVoList;
    }

    /**
     * 装配UserAttentionIncidentVo
     *
     * @param incident
     * @return
     */
    private UserAttentionIncidentVo assembleUserAttentionIncidentVo(Incident incident) {
        UserAttentionIncidentVo userAttentionIncidentVo = new UserAttentionIncidentVo();
        userAttentionIncidentVo.setId(incident.getId());
        userAttentionIncidentVo.setViews(incident.getViews());
        userAttentionIncidentVo.setAttentions(incident.getAttentions());
        userAttentionIncidentVo.setComments(incident.getComments());
        userAttentionIncidentVo.setTitle(incident.getTitle());
        return userAttentionIncidentVo;
    }

    /**
     * 获取IncidentViewVoList
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    private List<IncidentViewVo> getIncidentViewVoList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<IncidentView> incidentViewList = incidentViewMapper.selectByUserId(userId);
        if (incidentViewList == null) {
            return null;
        }
        List<IncidentViewVo> incidentViewVoList = new ArrayList<>();
        for (IncidentView i : incidentViewList) {
            Incident incident = incidentMapper.selectByPrimaryKey(i.getIncidentId());
            incidentViewVoList.add(assembleIncidentViewVo(i, incident));
        }
        return incidentViewVoList;
    }

    /**
     * 装配IncidentViewVo
     *
     * @param incidentView
     * @param incident
     * @return
     */
    private IncidentViewVo assembleIncidentViewVo(IncidentView incidentView, Incident incident) {
        IncidentViewVo incidentViewVo = new IncidentViewVo();
        incidentViewVo.setIncidentId(incidentView.getIncidentId());
        incidentViewVo.setIncidentTitle(incident.getTitle());
        incidentViewVo.setViews(incident.getViews());
        incidentViewVo.setAttentions(incident.getAttentions());
        incidentViewVo.setComments(incident.getComments());
        return incidentViewVo;
    }

    /**
     * 获取事件vo列表
     *
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return
     */
    private List<IncidentVo> getIncidentVoList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Incident> incidentList =  incidentMapper.selectListOrderByTime();
        return assembleIncidentVoList(incidentList);
    }

    /**
     * 装配事件vo列表
     *
     * @param incidentList
     * @return
     */
    private List<IncidentVo> assembleIncidentVoList(List<Incident> incidentList) {
        if (incidentList == null) {
            return null;
        }
        List<IncidentVo> incidentVoList = new ArrayList<>();
        for (Incident i : incidentList) {
            incidentVoList.add(assembleIncidentVo(i));
        }
        return incidentVoList;
    }

    /**
     * 装配IncidentVo
     *
     * @param incident
     * @return
     */
    private IncidentVo assembleIncidentVo(Incident incident) {
        IncidentVo incidentVo = new IncidentVo();
        incidentVo.setId(incident.getId());
        incidentVo.setAnon(incident.getAnon());
        incidentVo.setViews(incident.getViews());
        incidentVo.setAttentions(incident.getAttentions());
        incidentVo.setComments(incident.getComments());
        incidentVo.setTitle(incident.getTitle());
        incidentVo.setOccurTime(DateTimeUtil.dateToStr(incident.getOccurTime()));
        incidentVo.setAddress(incident.getAddress());
        incidentVo.setAddressName(incident.getAddressName());
        incidentVo.setDescription(incident.getDescription());
        incidentVo.setMainImageUrl(incident.getMainImageUrl());
        incidentVo.setMainVideoUrl(incident.getMainVideoUrl());
        incidentVo.setLatitude(incident.getLatitude());
        incidentVo.setLongitude(incident.getLongitude());
        return incidentVo;
    }

    /**
     * 装配IncidentVo
     *
     * @param incident
     * @param user
     * @param tagList
     * @param incidentCommentVoList
     * @return
     */
    private IncidentVo assembleIncidentVo(
            Incident incident, User user, List<Tag> tagList,
            List<IncidentCommentVo> incidentCommentVoList) {
        IncidentVo incidentVo = new IncidentVo();
        incidentVo.setId(incident.getId());
        incidentVo.setUserVo(assembleUserVo(user));
        incidentVo.setAnon(incident.getAnon());
        incidentVo.setViews(incident.getViews());
        incidentVo.setAttentions(incident.getAttentions());
        incidentVo.setComments(incident.getComments());
        incidentVo.setTagVoList(assembleTagVoList(tagList));
        incidentVo.setTitle(incident.getTitle());
        incidentVo.setOccurTime(DateTimeUtil.dateToStr(incident.getOccurTime()));
        incidentVo.setAddress(incident.getAddress());
        incidentVo.setAddressName(incident.getAddressName());
        incidentVo.setDescription(incident.getDescription());
        incidentVo.setMainImageUrl(incident.getMainImageUrl());
        incidentVo.setMainVideoUrl(incident.getMainVideoUrl());
        incidentVo.setLatitude(incident.getLatitude());
        incidentVo.setLongitude(incident.getLongitude());
        incidentVo.setIncidentCommentVoList(incidentCommentVoList);
        return incidentVo;
    }

    /**
     * 装配TagVoList
     *
     * @param tagList
     * @return
     */
    private List<TagVo> assembleTagVoList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(assembleTagVo(tag));
        }
        return tagVoList;
    }

    /**
     * 装配TagVo
     *
     * @param tag
     * @return
     */
    private TagVo assembleTagVo(Tag tag) {
        TagVo tagVo = new TagVo();
        tagVo.setId(tag.getId());
        tagVo.setName(tag.getName());
        tagVo.setNum(tag.getNum());
        return tagVo;
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
     * 获取标签名字的列表并拼接成字符串
     *
     * @param tags 标签id数组
     * @return 标签字符串
     */
    private String assembleTags(int[] tags) {
        List<String> tagList = tagMapper.selectTagListById(tags);
        return StringUtil0.listToString(tagList);
    }

}
