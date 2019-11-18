package com.mit.workflow.controller;

import com.mit.common.feign.DeptServiceFeign;
import com.mit.common.model.SysDept;
import com.mit.common.web.Result;
import io.swagger.annotations.Api;
import org.flowable.ui.common.model.GroupRepresentation;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 组资源
 */
@Api(tags = "组资源")
@RestController
@RequestMapping("/app")
public class EditorGroupsResource {

    @Resource
    private DeptServiceFeign deptServiceFeign;

    @GetMapping(value = "/rest/editor-groups")
    public ResultListDataRepresentation getGroups(@RequestParam(required = false, value = "filter") String filter) {
        Result<List<SysDept>> result = deptServiceFeign.filterByDeptName(filter);
        List<SysDept> list = result.getDatas();
        if (null == list) {
            return new ResultListDataRepresentation(new ArrayList<>());
        }

        List<GroupRepresentation> groupRepresentations = new ArrayList<>();
        for (SysDept sysDept : list) {
            GroupRepresentation groupRepresentation = new GroupRepresentation();
            groupRepresentation.setId(String.valueOf(sysDept.getId()));
            groupRepresentation.setName(sysDept.getName());
            groupRepresentations.add(groupRepresentation);
        }
        return new ResultListDataRepresentation(groupRepresentations);
    }
}
