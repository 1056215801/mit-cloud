package com.mit.datasource.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @Description 表字段自动填充处理
 */
public class MyMetaObjectHandler implements MetaObjectHandler {
    private final static String UPDATE_TIME = "updateTime";
    private final static String CREATE_TIME = "createTime";

    /**
     * 插入填充，字段为空自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object createTime = getFieldValByName(CREATE_TIME, metaObject);
        Object updateTime = getFieldValByName(UPDATE_TIME, metaObject);
        Date date = new Date();
        if (createTime == null) {
            setFieldValByName(CREATE_TIME, date, metaObject);
        }
        if (updateTime == null) {
            setFieldValByName(UPDATE_TIME, date, metaObject);
        }
    }

    /**
     * 更新填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Object updateTime = getFieldValByName(UPDATE_TIME, metaObject);
        Date date = new Date();
        if (updateTime == null) {
            setFieldValByName(UPDATE_TIME, date, metaObject);
        }
    }
}
