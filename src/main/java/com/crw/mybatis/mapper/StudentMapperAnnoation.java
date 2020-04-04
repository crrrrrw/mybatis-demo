package com.crw.mybatis.mapper;

import com.crw.mybatis.model.Student;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface StudentMapperAnnoation {
    @Select("select id,age,name from student where id = #{id}")
    Student getById(@Param("id") Long id);
}
