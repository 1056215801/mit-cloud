package com.mit.workflow.controller;

import com.mit.common.dto.LoginAppUser;
import com.mit.common.feign.UserServiceFeign;
import com.mit.common.model.SysUser;
import com.mit.common.web.Result;
import io.swagger.annotations.Api;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.common.model.UserRepresentation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程设计审核人
 */
@Api(tags = "用户资源")
@RestController
@RequestMapping("/app")
public class EditorUsersResource {

    @Resource
    private UserServiceFeign userServiceFeign;

    @RequestMapping(value = "/rest/editor-users", method = RequestMethod.GET)
    public ResultListDataRepresentation getUsers(@RequestParam(value = "filter", required = false) String filter) {
        /*Result<LoginAppUser> loginAppUserResult = userServiceFeign.findByUsername(filter);
        LoginAppUser loginAppUser = loginAppUserResult.getDatas();
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation userRepresentation = new UserRepresentation();
        if (null != loginAppUser) {
            userRepresentation.setId(String.valueOf(loginAppUser.getId()));
            userRepresentation.setFirstName(loginAppUser.getUsername());
            userRepresentation.setFullName(loginAppUser.getUsername());
            userRepresentation.setEmail(loginAppUser.getEmail());
            userRepresentations.add(userRepresentation);
        }*/

        Result<List<SysUser>> result = userServiceFeign.filterByUserName(filter);
        List<SysUser> users = result.getDatas();
        if (null == users) {
            return new ResultListDataRepresentation(new ArrayList<>());
        }
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        for (SysUser user : users) {
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setId(String.valueOf(user.getId()));
            userRepresentation.setFirstName(user.getUsername());
            userRepresentation.setFullName(user.getUsername());
            userRepresentation.setEmail(user.getEmail());
            userRepresentations.add(userRepresentation);
        }
        return new ResultListDataRepresentation(userRepresentations);
    }

}
