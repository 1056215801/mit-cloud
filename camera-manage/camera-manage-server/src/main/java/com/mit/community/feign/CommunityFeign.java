package com.mit.community.feign;

import com.mit.common.web.Result;
import com.mit.community.entity.ClusterCommunity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author qishengjun
 * @Date Created in 11:08 2019/11/29
 * @Company: mitesofor </p>
 * @Description:~
 */
@FeignClient(value = "web-community-wanli")
@Component
public interface CommunityFeign {
    @PostMapping("/api/web/communitywanli/clusterCommunity/getBaseInfo")
    public Result getBaseInfo(@RequestParam(value = "communityCode") String communityCode);
    @PostMapping("/api/web/communitywanli/personBaseInfo/getCommunitylist")
    public Result getCommunitylist();

    @GetMapping("/api/web/communitywanli/clusterCommunity/getClusterCommunityByCommunityCode")
    public ClusterCommunity getClusterCommunityByCommunityCode(@RequestParam(value = "communityCode")String communityCode);

    @PostMapping("/api/web/communitywanli/personBaseInfo/getZonelist")
    public Result getZonelist(@RequestParam(value = "communityCode")String communityCode);
}
