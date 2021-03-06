package com.just.teachersystem.Mapper;



import	java.util.List;
import java.util.Set;

import com.just.teachersystem.Entity.Department;
import com.just.teachersystem.Entity.Kind;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


/**
 * 通用mapper
 */
@Mapper
@Component
public interface CommonMapper {

    //获得类别
    @Select("Select class1,class2,class3 from kind")
    List<Kind> getTypeList();

    //获得级别
    @Select("select level from level")
    Set<String> getLevelSet();

    //得到部门
    @Select(" select id ,dptname from department")
    Set<Department> getDepartmentList();

    //添加类别
    @Insert("insert into kind (class1,class2,class3,computeDptId) values(#{class1},#{class2},#{class3},#{computeDptId}) ")
    boolean addType(Kind kind);

    @Delete("delete from kind where class2=#{class2} and class3=#{class3}")
    boolean deleteType(Kind kind);

    //添加级别
    @Insert("insert into level (level) value(#{level})")
    boolean addLevel(String level);

    //删除级别
    @Delete("delete from level where level=#{level}")
    boolean deleteLevel(String level);


    //查询奖项
    @Select("select prize from prize")
    Set<String> getPrizeSet();
    //添加奖项
    @Insert("insert into prize (prize) value(#{prize})")
    boolean addPrize(String prize);

    //删除奖项
    @Delete("delete from prize where prize=#{prize}")
    boolean deletePrize(String prize);

}