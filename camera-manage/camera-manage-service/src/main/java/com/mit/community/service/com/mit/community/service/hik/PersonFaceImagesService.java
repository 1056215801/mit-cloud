package com.mit.community.service.com.mit.community.service.hik;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.community.entity.hik.PersonFaceImages;
import com.mit.community.mapper.PersonFaceImagesMapper;
import org.springframework.stereotype.Service;

/**
 * @Author qishengjun
 * @Date Created in 16:17 2019/10/28
 * @Company: mitesofor </p>
 * @Description:~
 */
@Service
public class PersonFaceImagesService extends ServiceImpl<PersonFaceImagesMapper,PersonFaceImages> {

   /* public void save(PersonFaceImages personFaceImages) {

        EntityWrapper<PersonFaceImages> entityWrapper=new EntityWrapper<>();
        baseMapper.insert(personFaceImages);
    }*/
}
