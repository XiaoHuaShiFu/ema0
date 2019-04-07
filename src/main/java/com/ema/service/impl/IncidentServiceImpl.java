package com.ema.service.impl;

import com.ema.common.ResponseCode;
import com.ema.common.ServerResponse;
import com.ema.dao.IncidentMapper;
import com.ema.dao.TagMapper;
import com.ema.pojo.Incident;
import com.ema.pojo.User;
import com.ema.service.IIncidentService;
import com.ema.service.ITagService;
import com.ema.util.StringUtil0;
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
    private ITagService iTagService;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private IncidentMapper incidentMapper;

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
        incident.setTags(assembleTags(tags)); //把tags数组查询之后并拼接成字符串
        //插入事件
        incidentMapper.insertSelective(incident);
        Map<String, Integer> map = new HashMap<>();
        map.put("id", incident.getId());
        return ServerResponse.createBySuccess("save incident success", map);
    }

    public ServerResponse getIncident(int id, User user) {
        Incident incident = incidentMapper.selectByPrimaryKey(id);
        if (!incident.getUserId().equals(user.getId())) {
            return ServerResponse.createByError("unauthorized operation");
        }
        return null;
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
