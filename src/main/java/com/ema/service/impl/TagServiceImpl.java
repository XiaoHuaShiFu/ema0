package com.ema.service.impl;

import com.ema.common.ServerResponse;
import com.ema.dao.TagMapper;
import com.ema.pojo.Tag;
import com.ema.service.ITagService;
import com.ema.vo.TagVo;
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
 * @create 2019-04-07 13:31
 */
@Service("iTagService")
public class TagServiceImpl implements ITagService {

    @Autowired
    private TagMapper tagMapper;

    /**
     * 保存Tag
     *
     * @param tagName 标签名字
     * @return 如果成功返回Tag的信息vo
     */
    public ServerResponse saveTag(String tagName) {
        //检查标签存不存在
        int rowCount = tagMapper.selectCountByName(tagName);
        //如果数量大于1说明标签已经存在
        if (rowCount >= 1) {
            return ServerResponse.createByErrorMessage("this tag has been exist");
        }

        //插入标签
        Tag tag = new Tag();
        tag.setName(tagName);
        tagMapper.insertTagIfNotExist(tag);
        //返回标签
        return ServerResponse.createBySuccess(assembleTagVo(tag));
    }

    /**
     * 获取标签列表
     *
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return 标签列表
     */
    public ServerResponse getTagList(int pageNum, int pageSize) {
        PageInfo<TagVo> result = new PageInfo<>(getTagVoList(pageNum, pageSize));
        return ServerResponse.createBySuccess(result);
    }

    /**
     * 获取TagVoList
     *
     * @param pageNum 页码
     * @param pageSize 页条数
     * @return TagVoList
     */
    private List<TagVo> getTagVoList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Tag> tagList = tagMapper.selectTagList();
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag t : tagList) {
            tagVoList.add(assembleTagVo(t));
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



}
