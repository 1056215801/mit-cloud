package com.mit.auth.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.auth.server.model.Client;
import com.mit.common.web.PageResult;

import java.util.Map;

public interface IClientService extends IService<Client> {

    /**
     * 查询应用列表
     * @param params
     * @param isPage 是否分页
     */
    PageResult<Client> listClient(Map<String, Object> params, boolean isPage);
}
