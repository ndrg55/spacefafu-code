package com.spacefafu.spit.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Spit implements Serializable {
    @Id
    private String _id;
    private String content;
    private Date publishtime;
    private String userid;
    private String nickname;   //因吐槽表没有设计好   ，将nickname 定义为 targetUserId
    private Integer visits;
    private Integer thumbup;
    private Integer share;
    private Integer comment;
    private String state;   //因吐槽表没有设计好       将state设置为最高级吐槽id

    private String parentid;
    @Transient
    private Object User;
    @Transient
    private Object targetUser;
    @Transient
    private List<Spit> childSpit;

    public Object getUser() {
        return User;
    }

    public void setUser(Object user) {
        User = user;
    }

    public Object getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(Object targetUser) {
        this.targetUser = targetUser;
    }

    public List<Spit> getChildSpit() {
        return childSpit;
    }

    public void setChildSpit(List<Spit> childSpit) {
        this.childSpit = childSpit;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Integer getThumbup() {
        return thumbup;
    }

    public void setThumbup(Integer thumbup) {
        this.thumbup = thumbup;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }
}
