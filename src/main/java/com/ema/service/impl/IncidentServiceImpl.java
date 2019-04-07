package com.ema.service.impl;

import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.dao.IncidentMapper;
import com.ema.dao.TagIncidentMapMapper;
import com.ema.dao.TagMapper;
import com.ema.dao.UserMapper;
import com.ema.pojo.Incident;
import com.ema.pojo.Tag;
import com.ema.pojo.TagIncidentMap;
import com.ema.pojo.User;
import com.ema.service.IIncidentService;
import com.ema.service.ITagService;
import com.ema.util.DateTimeUtil;
import com.ema.util.StringUtil0;
import com.ema.vo.IncidentVo;
import com.ema.vo.TagVo;
import com.ema.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserMapper userMapper;

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

        //把事件的id返回出去
        Map<String, Integer> map = new HashMap<>();
        map.put("id", incident.getId());
        return ServerResponse.createBySuccess("save incident success", map);
    }

    /**
     * 获取一个事件的详细信息,包括评论一级评论的简略信息
     *
     * @param id 事件id
     * @return
     */
    public ServerResponse getIncident(int id) {
        Incident incident = incidentMapper.selectByPrimaryKey(id);
        if (incident == null) {
            return ServerResponse.createByErrorMessage("This incident not exist");
        }
        User user = userMapper.selectByPrimaryKey(incident.getUserId());
        List<Tag> tagList = tagMapper.selectTagListByIncidentId(incident.getId());
        IncidentVo incidentVo = assembleIncidentVo(incident, user, tagList);
        // TODO: 2019/4/7 还没有封装一级评论 
        return ServerResponse.createBySuccess(incidentVo);
    }

    /**
     * 装配IncidentVo
     *
     * @param incident
     * @param user
     * @param tagList
     * @return
     */
    private IncidentVo assembleIncidentVo(Incident incident, User user, List<Tag> tagList) {
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
