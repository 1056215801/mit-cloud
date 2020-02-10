package com.mit.community.entity.vo;

import com.mit.common.dto.DeptTree;
import com.mit.community.entity.Equipment;
import lombok.Data;

import java.util.List;

/**
 * @Author qishengjun
 * @Date Created in 11:19 2019/10/22
 * @Company: mitesofor </p>
 * @Description:~
 */
@Data
public class DeptTreeVo extends DeptTree {
    List<Equipment> equipmentList;
}
