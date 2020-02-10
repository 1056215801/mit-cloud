package com.mit.community.module.device.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.mit.common.dto.DeptTree;
import com.mit.common.dto.TreeNode;
import com.mit.common.model.SysDept;
import com.mit.common.web.Result;
import com.mit.community.entity.Equipment;

import com.mit.community.entity.WebSysDepartment;
import com.mit.community.entity.vo.DeptTreeVo;
import com.mit.community.feign.CommunityFeign;
import com.mit.community.feign.SysDeptFeign;
import com.mit.community.service.EquipmentService;
import com.mit.community.service.WebSysDepartmentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;

import static com.mit.community.module.device.controller.QfqzController.getSHA256Str;

/**
 * @Author qishengjun
 * @Date Created in 17:00 2019/9/11
 * @Company: mitesofor </p>
 * @Description:~
 */
@RestController
@RequestMapping(value = "/equipment")
@Slf4j
@Api(value = "设备管理", tags = {"设备管理"})
public class equipmentController {

    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private WebSysDepartmentService webSysDepartmentService;

    @Autowired
    private SysDeptFeign sysDeptFeign;

    @Autowired
    private CommunityFeign communityFeign;
    @ApiOperation(value = "添加设备")
    @PostMapping("/addEquipment")
    public Result addEquipment(Equipment equipment){
        if (equipment == null) {
            return Result.failed("参数异常");
        }
        equipment.setGmtCreate(LocalDateTime.now());
        equipment.setGmtModified(LocalDateTime.now());
        equipmentService.insert(equipment);
        return Result.succeed("添加成功");
    }
    @ApiOperation(value = "修改设备")
    @ApiImplicitParam
    @PostMapping("/updateEquipment")
    public Result updateEquipment(Equipment equipment){
        if (equipment == null) {
            return Result.failed("参数异常");
        }
        equipment.setGmtModified(LocalDateTime.now());
        equipmentService.updateById(equipment);
        return Result.succeed("修改成功");
    }

    @ApiOperation(value = "删除设备")
    @ApiImplicitParam(name = "id",value = "设备id",dataType = "String",paramType = "query",required = true)
    @PostMapping("/deleteEquipment")
    public Result deleteEquipment(Integer id){
        if (id == null) {
            return Result.failed("参数异常");
        }
        equipmentService.deleteById(id);
        return Result.succeed("删除成功");
    }
    @ApiOperation(value = "获取设备部门列表")
    @PostMapping("/getDepartmentList")
    public Result getDepartmentList(){
        Result<List<DeptTree>> result = sysDeptFeign.listDeptTrees();
        List list=new ArrayList();
        List<DeptTree> deptTrees = result.getDatas();
        List<DeptTreeVo> deptTreeVoList=new ArrayList<>();
        for (DeptTree deptTree : deptTrees) {
            DeptTreeVo deptTreeVo=new DeptTreeVo();
            long id = deptTree.getId();
            String deptTreeName = deptTree.getName();
            EntityWrapper<Equipment> wrapper=new EntityWrapper<>();
            wrapper.eq("department_no",id);
            BeanUtils.copyProperties(deptTree,deptTreeVo);
            List<Equipment> equipmentList = equipmentService.selectList(wrapper);
            if (equipmentList.size()>0) {
            deptTreeVo.setEquipmentList(equipmentList);
            }
            deptTreeVoList.add(deptTreeVo);
            List<TreeNode> treeNodeList = deptTreeVo.getChildren();
        }

        for (DeptTreeVo deptTreeVo : deptTreeVoList) {
            List<TreeNode> childrenList = deptTreeVo.getChildren();
        }
        return Result.succeed(deptTreeVoList);
    }
    public List<DeptTreeVo> getDeptTreeVoList(List<TreeNode> list){
        for (TreeNode treeNode : list) {
            if (treeNode.getChildren().size()>0) {
                getDeptTreeVoList(treeNode.getChildren());
            }
            DeptTreeVo deptTreeVo=new DeptTreeVo();
            long id = treeNode.getId();
            EntityWrapper<Equipment> wrapper=new EntityWrapper<>();
            wrapper.eq("department_no",id);
            List<Equipment> equipmentList = equipmentService.selectList(wrapper);
            if (equipmentList.size()>0) {
                deptTreeVo.setEquipmentList(equipmentList);
            }

        }
        return null;
    }
    @ApiOperation(value = "获取设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentNo",value = "部门编号",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "serialNumber",value = "设备编号",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "deviceHolder",value = "设备持有人",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "job",value = "岗位",dataType = "String",paramType = "query")
    })
    @PostMapping("/getEquipmentList")
    public Result getEquipmentList(String departmentNo,String serialNumber,String deviceHolder,String job,Integer pageNum,Integer pageSize){

        String url="http://120.76.189.28:1241/queryOnlinePosInfos";
        String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
        String seq= UUID.randomUUID().toString();
        String sign=getSHA256Str(serviceKey+seq);
        //NameValuePair[] data =null;
        NameValuePair[] data = {new NameValuePair("seq", seq),
                new NameValuePair("sign", sign),
                new NameValuePair("departmentId", "6014"),
                new NameValuePair("currPageIndex", "1"),
                new NameValuePair("pageSize", "1000")

        };
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpClient.getParams().setContentCharset("utf-8");
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Connection", "close");
        postMethod.setRequestBody(data);

        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        List list=new ArrayList<>();
        String result = StringUtils.EMPTY;
        try {
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
//            JSONArray array=JSONArray.fromObject(result);
            JSONObject jsonObject = JSONObject.fromObject(result);
            if("0".equals(jsonObject.get("code").toString())){
                list= (List<Map>) jsonObject.get("posInfos");
            }
            System.out.println(list);
//            List list1 = JSONArray.toList(array, new PosInfo(), new JsonConfig());
        } catch (Exception e) {
            log.error("发送post请求错误", e);
        return Result.failed("发送post请求错误");
    } finally {
        postMethod.releaseConnection();
    }
        Page<Equipment> page=equipmentService.getPageList(departmentNo,serialNumber,deviceHolder,job,pageNum,pageSize,list);

        return Result.succeed(page);
    }

    @PostMapping("/getinfo")
    public Result getinfo(){
        Result communitylist = communityFeign.getCommunitylist();
        return Result.succeed("成功");
    }
}
