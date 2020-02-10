package com.mit.community.module.hik.face.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.web.Result;
import com.mit.community.entity.hik.PersonFaceImages;
import com.mit.community.entity.hik.Vo.FaceComparsionVo;
import com.mit.community.service.com.mit.community.service.hik.PersonFaceImagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@Api(tags = "人脸布控")
@RequestMapping(value = "/faceControl", method = RequestMethod.POST)
public class FaceControlController {
    @Autowired
    private PersonFaceImagesService personFaceImagesService;
    @Autowired
    private HKFaceController hkFaceController;
    @ApiOperation("获取人脸布控列表")
    @PostMapping("/getControlList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "姓名",paramType = "query"),
            @ApiImplicitParam(name = "faceClassification",value = "姓名",paramType = "query")
    })
    public Result getControlList(String name,String faceClassification,Integer iDType,
                                 String idNo,String startTime,String endTime,Integer pageNum,Integer pageSize){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start=null;
        Date end=null;
        try {
            if (StringUtils.isNotBlank(startTime)) {
                start = format.parse(startTime);
            }
            if (StringUtils.isNotBlank(endTime)) {
                end = format.parse(endTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
        }
        IPage<PersonFaceImages> page=new Page<>(pageNum,pageSize);
        QueryWrapper<PersonFaceImages> wrapper=new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like("name",name);
        }
        if (StringUtils.isNotBlank(faceClassification)) {
            wrapper.eq("face_classification",faceClassification);
        }
        if (iDType!=null) {
            wrapper.eq("iDType",iDType);
        }
        if (StringUtils.isNotBlank(idNo)) {
            wrapper.like("idNo",idNo);
        }
        if (start!=null){
            wrapper.ge("create_time",start);
        }
        if (end!=null) {
            wrapper.le("create_time",end);
        }
        wrapper.eq("face_database_name","布控-黑名单");
        IPage<PersonFaceImages> iPage = personFaceImagesService.page(page, wrapper);
        return Result.succeed(iPage);
    }
    @PostMapping("/getFaceComparison")
    @ApiOperation("获取人脸对比")
    @ApiImplicitParam(name = "name",value = "姓名",paramType = "query")
    public Result getFaceComparison(String name){
          List<FaceComparsionVo> list=personFaceImagesService.getFaceComparison(name);
          return Result.succeed(list);
    }
    @PostMapping("/delete")
    @ApiOperation("删除人脸")
    public Result delete(Integer id){
        QueryWrapper<PersonFaceImages> wrapper=new QueryWrapper<>();
        if (id!=null) {
            wrapper.eq("id",id);
        }
        PersonFaceImages personFaceImages = personFaceImagesService.getOne(wrapper);
        String uncode="";
        String faceGroupIndexCode ="";
        if (personFaceImages!=null){
            uncode = personFaceImages.getUncode();
            faceGroupIndexCode = personFaceImages.getFaceGroupIndexCode();
        }
        String[] arr=new String[5];
        arr[0]=uncode;
        boolean remove = personFaceImagesService.remove(wrapper);
        if (!remove){
            return Result.failed("删除失败");
        }
        Result result = hkFaceController.deleteFace(arr, faceGroupIndexCode);
        Integer resp_code = result.getResp_code();
        if (resp_code==0){
            return Result.failed("删除失败");
        }
        return Result.succeed("删除成功");
    }

}
