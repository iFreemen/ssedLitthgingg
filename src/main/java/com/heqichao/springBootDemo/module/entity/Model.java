package com.heqichao.springBootDemo.module.entity;

import com.heqichao.springBootDemo.base.entity.BaseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by heqichao on 2018-11-17.
 */
@Component("model")
public class Model extends BaseEntity{
    //模板名字
    private String model_name;

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public Model(String model_name, Integer userId, Date date) {
        this.model_name = model_name;
        this.add_uid=userId;
        this.udp_uid=userId;
        this.add_date=date;
        this.udp_date=date;
    }
}
