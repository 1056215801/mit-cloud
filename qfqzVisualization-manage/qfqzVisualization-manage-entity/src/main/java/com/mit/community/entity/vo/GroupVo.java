package com.mit.community.entity.vo;

import com.mit.community.entity.Equipment;
import com.mit.community.entity.Group;
import lombok.Data;

import java.util.List;

/**
 * @Author qishengjun
 * @Date Created in 10:54 2019/10/22
 * @Company: mitesofor </p>
 * @Description:~
 */
@Data
public class GroupVo extends Group {
    List<Equipment> equipmentList;
}
