package com.mit.community.feign;

import com.mit.common.web.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author qishengjun
 * @Date Created in 10:19 2020/1/2
 * @Company: mitesofor </p>
 * @Description:~
 */
@FeignClient(value = "file-center")
@Component
public interface FileUploadFeign {
    @PostMapping("/file/upload")
    public Result upload(@RequestParam(value = "file") MultipartFile file);
    @PostMapping("/file/upload/base64")
    public Result base64(@RequestParam(value = "file") String file);
}
