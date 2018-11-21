package com.heqichao.springBootDemo.base.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by heqichao on 2018-7-1.
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    protected Integer id;

    //保存时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    protected Date add_date;

    //更新时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    protected Date udp_date;

    //保存人
    protected Integer add_uid;

    //更新人
    protected Integer udp_uid;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Date getAdd_date() {
        return add_date;
    }

    public void setAdd_date(Date add_date) {
        this.add_date = add_date;
    }

    public Date getUdp_date() {
        return udp_date;
    }

    public void setUdp_date(Date udp_date) {
        this.udp_date = udp_date;
    }

    public Integer getAdd_uid() {
        return add_uid;
    }

    public void setAdd_uid(Integer add_uid) {
        this.add_uid = add_uid;
    }

    public Integer getUdp_uid() {
        return udp_uid;
    }

    public void setUdp_uid(Integer udp_uid) {
        this.udp_uid = udp_uid;
    }
}
