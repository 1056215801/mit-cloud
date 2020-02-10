package com.mit.community.feign;

import com.mit.common.dto.DeptTree;
import com.mit.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author qishengjun
 * @Date Created in 9:02 2019/10/22
 * @Company: mitesofor </p>
 * @Description:~
 */
@FeignClient(value = "user-center")
@Component
public interface SysDeptFeign {

    @GetMapping(value = "/dept/tree")
    public Result<List<DeptTree>> listDeptTrees();
    @GetMapping("/user/info?access_token={access_token}")
    public Result info(@PathVariable(value = "access_token") String access_token);
}
