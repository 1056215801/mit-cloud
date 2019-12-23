package com.mit.workflow.controller;

import org.flowable.ui.common.model.UserRepresentation;
import org.flowable.ui.common.security.DefaultPrivileges;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *  实现免密登录
 *  对应js配置
 *  resources\static\scripts\configuration url-config.js
 */
@RestController
@RequestMapping("/flowapp")
public class AppController {

    /*@GetMapping(value = "/rest/account")
    public UserRepresentation getAccount() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("1");
        userRepresentation.setEmail("admin@flowable.org");
        userRepresentation.setFullName("admin");
        userRepresentation.setLastName("admin");
        userRepresentation.setFirstName("admin");
        List<String> privileges = new ArrayList<>();
        privileges.add(DefaultPrivileges.ACCESS_MODELER);
        privileges.add(DefaultPrivileges.ACCESS_IDM);
        privileges.add(DefaultPrivileges.ACCESS_ADMIN);
        privileges.add(DefaultPrivileges.ACCESS_TASK);
        privileges.add(DefaultPrivileges.ACCESS_REST_API);
        userRepresentation.setPrivileges(privileges);
        return userRepresentation;
    }*/
}
