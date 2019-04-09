package com.ema.service;

import com.ema.common.ServerResponse;
import com.ema.pojo.Incident;
import com.ema.pojo.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * 描述:
 *
 * @author xhsf
 * @email 827032783@qq.com
 * @create 2019-04-03 23:48
 */
public interface IIncidentService {
    ServerResponse saveIncident(Incident incident, User user, int[] tags);

    ServerResponse getIncident(int id);

    ServerResponse deleteIncident(User sessionUser, Incident id);

    ServerResponse getIncidentList(int pageNum, int pageSize);

    ServerResponse uploadMainImage(String path, MultipartFile mainImage, Integer userId, Integer incidentId);

    ServerResponse uploadMainVideo(String path, MultipartFile mainVideo, Integer userId, Integer incidentId);

    ServerResponse updateIncident(User sessionUser, Incident incident);

    ServerResponse attentionIncident(Integer userId, Integer id);

    ServerResponse viewIncident(Integer userId, Integer incidentId);

    ServerResponse getViewList(Integer userId, int pageNum, int pageSize);
}
