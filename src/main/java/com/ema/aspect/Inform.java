package com.ema.aspect;

import com.ema.common.ServerResponse;
import com.ema.dao.IncidentMapper;
import com.ema.dao.UserMapper;
import com.ema.dao.UserNoticeMapper;
import com.ema.pojo.*;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Aspect
public class Inform {
    @Autowired
    UserNoticeMapper userNoticeMapper;
    @Autowired
    IncidentMapper incidentMapper;
    @Autowired
    UserMapper userMapper;

    @Pointcut("execution(* com.ema.service.impl.UserServiceImpl.follow(..))&& args(user,followederId,..)")
    public void pointCutFollow(User user,Integer followederId){
    }
    @Pointcut("execution(* com.ema.service.impl.IncidentServiceImpl.attentionIncident(..))&& args(userId,id,..)")
    public void PointCutIncidentFollow(Integer userId, Integer id){
    }
    @Pointcut("execution(* com.ema.service.impl.IncidentCommentServiceImpl.saveComment(..))&& args(incidentComment,sessionUser,..)")
    public void pointCutComment(IncidentComment incidentComment, User sessionUser){
    }
    @Pointcut("execution(* com.ema.service.impl.IncidentScndCommentServiceImpl.saveComment(..))&& args(incidentScndComment,sessionUser,..)")
    public void pointCutScndComment(IncidentScndComment incidentScndComment, User sessionUser){
    }

    //关注用户后
    @AfterReturning(value = "pointCutFollow(user,followederId)",returning = "args")
    public void afterFollow(User user,Integer followederId,Object args) {
        ServerResponse returning = (ServerResponse) args;
        short type = 10;
        if (returning.getData() == "follow success"){
            //user关注者，followederId被关注者
            UserNotice userNotice = new UserNotice();
            //设置被关注者的id
            userNotice.setUserId(followederId);
            //设置关注者的id
            userNotice.setFollowerId(user.getId());
            //设置通知为未看
            userNotice.setView(0);
            //设置通知标题
            userNotice.setTitle("新粉丝");
            //设置通知类型
            userNotice.setType(type);
            //设置通知内容
            userNotice.setContent(user.getNickName() + " 关注了你");
            //关注后判断一下通知里是否已经存在这个消息，如果存在且通知没被看就不往里面添加了
            int count = userNoticeMapper.selectViewOrNot(userNotice);
            if(count < 1){
                userNoticeMapper.insert(userNotice);
            }
        }
        else
            return;
    }

    //关注事件后
    @AfterReturning(value = "PointCutIncidentFollow(userId,id)",returning = "args")
    public void afterIncidentFollow(Integer userId, Integer id,Object args){
        ServerResponse retuning = (ServerResponse) args;
        short type = 20;
        if(retuning.getData() =="attention false"){
            return;
        }
        else{
            //userId关注事件的用户  id事件的ID
            UserNotice userNotice = new UserNotice();
            //设置为未看
            userNotice.setView(0);
            //设置通知类型
            userNotice.setType(type);
            //根据关注事件的id找到发布者的id
            int user_id = incidentMapper.selectUserIdById(id);
            userNotice.setUserId(user_id);
            //设置标题
            userNotice.setTitle("用户关注了事件");
            //关注者的id
            userNotice.setFollowerId(userId);
            //根据关注者的id找到关注者的nickName
            String user_nickName = userMapper.selectNickNameById(userId);
            //根据关注事件的id找到事件的标题
            String title = incidentMapper.selectTitleById(id);
            userNotice.setContent(user_nickName+"关注了你的"+" "+title + " 事件");
            //关注后判断一下通知里是否已经存在这个消息，如果存在且通知没被看就不往里面添加了
            int count = userNoticeMapper.selectViewOrNot(userNotice);
            if(count < 1){
                userNoticeMapper.insert(userNotice);
            }
        }
    }

    //评论事件后
    @AfterReturning(value = "pointCutComment(incidentComment,sessionUser)",returning = "args")
    public void afterComment(IncidentComment incidentComment,User sessionUser,Object args){
        ServerResponse returning = (ServerResponse) args;
        System.out.println(returning.getMsg());
        short type = 21;
        if (returning.getMsg() == "upload comment success"){
            //incidentComment事件评论的详细信息 seesionUser评论者的信息
            UserNotice userNotice = new UserNotice();
            //设置为未看
            userNotice.setView(0);
            //设置通知类型
            userNotice.setType(type);
            //根据评论事件的id找到事件发布者的id，即被通知的用户的id
            int user_id = incidentMapper.selectUserIdById(incidentComment.getIncidentId());
            userNotice.setUserId(user_id);
            //根据评论事件的id找到事件的标题
            String title = incidentMapper.selectTitleById(incidentComment.getIncidentId());
            //设置通知标题
            userNotice.setTitle(sessionUser.getNickName()+"评论了你的"+" "+title);
            //设置通知内容
            userNotice.setContent(incidentComment.getComment());
            userNoticeMapper.insert(userNotice);
        }
        else
            return;
    }

    //二级评论后
    @AfterReturning(value = "pointCutScndComment(incidentScndComment,sessionUser)",returning = "args")
    public void afterScndComment(IncidentScndComment incidentScndComment, User sessionUser, Object args){
        ServerResponse returning = (ServerResponse) args;
        short type = 22;
        if (returning.getMsg() == "save comment success"){
            UserNotice userNotice = new UserNotice();
            //设置消息为未看
            userNotice.setView(0);
            //设置被通知的id，也就是父id
            userNotice.setUserId(incidentScndComment.getParScndCommentId());
            //设置通知标题
            userNotice.setTitle("你的评论有回复");
            //设置通知的类型
            userNotice.setType(type);
            //设置二级评论者的id
            userNotice.setIncidentScndCommentId(sessionUser.getId());
            //设置通知内容
            userNotice.setContent(sessionUser.getNickName()+"回复了你:"+incidentScndComment.getComment());
            userNoticeMapper.insert(userNotice);
        }
        else
            return;
    }
}
