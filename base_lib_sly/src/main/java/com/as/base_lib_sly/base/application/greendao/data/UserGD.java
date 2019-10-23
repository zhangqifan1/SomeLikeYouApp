package com.as.base_lib_sly.base.application.greendao.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * -----------------------------
 * Created by zqf on 2019/10/16.
 * 用户数据  数据库 这里是个例子
 * ---------------------------
 */

@Entity
public class UserGD {

    //不能用int
    @Id(autoincrement = true)
    private Long id;

    private String name ;

    @Generated(hash = 263774966)
    public UserGD(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1359848696)
    public UserGD() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
