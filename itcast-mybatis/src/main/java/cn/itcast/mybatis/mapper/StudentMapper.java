package cn.itcast.mybatis.mapper;

import cn.itcast.mybatis.pojo.Student;

import java.util.List;

public interface StudentMapper {

    List<Student> queryAllStudents();
}
