package com.ema.cache;

import java.util.Map;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-03-12 16:59
 */
public interface IRedisHash {

    Long set(String hash, String key);

    String get(String hash, String key);

    Long del(String hash, String key);

    Map<String, String> getAll(String key);

}
