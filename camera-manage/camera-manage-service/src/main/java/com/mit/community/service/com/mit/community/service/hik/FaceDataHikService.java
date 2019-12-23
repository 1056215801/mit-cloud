package com.mit.community.service.com.mit.community.service.hik;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.community.entity.hik.FaceDatabase;
import com.mit.community.entity.hik.PersonFaceImages;
import com.mit.community.mapper.FaceDatabaseMapper;
import com.mit.community.mapper.PersonFaceImagesMapper;
import com.mit.community.mapper.PersonFaceSortInfoMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class FaceDataHikService extends ServiceImpl<FaceDatabaseMapper,FaceDatabase> {
	@Autowired
	private PersonFaceSortInfoMapper personFaceSortInfoMapper;
    @Autowired
	private PersonFaceImagesMapper personFaceImagesMapper;

    String  ARTEMIS_PATH = "/artemis";

	/**
	 *
	 * @param
	 * @return
	 * @company mitesofor
	 */

	@Transactional(rollbackFor = Exception.class)
	public int  saveSinglePersonFaceLocal (PersonFaceImages personFaceImages) throws Exception {
/*		PersonFaceSortInfo personFaceSortInfo=new PersonFaceSortInfo();
		String groupName=faceInfo.getFaceGroupName();
		// １白名单　，２黑名单　，３，陌生人，４　重点关注人员 5 特殊关爱人员
		if("白名单".equals(groupName)){
			personFaceSortInfo.setGroupType(1);
		}else if("黑名单".equals(groupName)){
			personFaceSortInfo.setGroupType(2);
		}else if("陌生人".equals(groupName)){
			personFaceSortInfo.setGroupType(3);
		}else if("重点关注人员".equals(groupName)){
			personFaceSortInfo.setGroupType(4);
		}else if ("特殊关爱人员".equals(5)){
			personFaceSortInfo.setGroupType(5);
		}else{
			personFaceSortInfo.setGroupType(1);
		}
        personFaceSortInfo.setFaceClassification(faceInfo.getFaceClassification());
		personFaceSortInfo.setMobile(faceInfo.getMobile());

		java.util.Date date = new java.util.Date();
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime createTime = LocalDateTime.ofInstant(instant, zone);
		personFaceSortInfo.setCreateTime(createTime);
		personFaceSortInfo.setModifiedTime(createTime);
		personFaceSortInfo.setDeleted(0);
		int i=0;
		i=personFaceSortInfoMapper.insert(personFaceSortInfo);
		int personFaceSortInfoId=personFaceSortInfo.getId();*/

//		i=personFaceImagesMapper.insert(personFaceImages);

		return 0;
	}


}
