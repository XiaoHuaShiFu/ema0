package com.ema.service;

import com.ema.common.ServerResponse;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-07 13:31
 */
public interface ITagService {
    ServerResponse saveTag(String tagName);

    ServerResponse getTagList(int pageNum, int pageSize);
}
