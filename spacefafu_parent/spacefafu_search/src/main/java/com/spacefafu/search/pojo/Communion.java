package com.spacefafu.search.pojo;

import org.elasticsearch.common.collect.HppcMaps;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.util.Date;

@Document(indexName = "spacefafu",type ="communion" )
public class Communion implements Serializable {
    @Id
    private String id;
    private String contentId;
    @Field(index = true,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String title;
    @Field(index = true,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String  content;
    private String  userid;
    private String  contentType;
    private Long reply;
    private String time;
    @Field(ignoreFields = "user")
    private Object user;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getReply() {
        return reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }
}
