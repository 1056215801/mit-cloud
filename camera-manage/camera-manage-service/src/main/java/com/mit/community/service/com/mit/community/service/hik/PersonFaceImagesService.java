package com.mit.community.service.com.mit.community.service.hik;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.community.entity.hik.PersonFaceImages;
import com.mit.community.entity.hik.Vo.FaceComparsionVo;
import com.mit.community.mapper.PersonFaceImagesMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author qishengjun
 * @Date Created in 16:17 2019/10/28
 * @Company: mitesofor </p>
 * @Description:~
 */
@Service
public class PersonFaceImagesService extends ServiceImpl<PersonFaceImagesMapper,PersonFaceImages> {
    public List<FaceComparsionVo> getFaceComparison(String name) {
        return baseMapper.getFaceComparison(name);
    }

   /* public void save(PersonFaceImages personFaceImages) {

        EntityWrapper<PersonFaceImages> entityWrapper=new EntityWrapper<>();
        baseMapper.insert(personFaceImages);
    }*/
}
