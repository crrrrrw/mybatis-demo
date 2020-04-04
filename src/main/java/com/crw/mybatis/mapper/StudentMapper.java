package com.crw.mybatis.mapper;

import com.crw.mybatis.model.Student;

import java.util.List;
import java.util.Map;

public interface StudentMapper {

    Student getById(Long id);

    Map<String, Object> getByIdReturnMap(Long id);

    List<Student> getListByNameLike(String name);

    Student getByMap(Map<String, Object> map);

    Student getByIdAndName(Long id, String name);

    void insert(Student student);

    int update(Student student);

    int deleteById(Long id);
}
