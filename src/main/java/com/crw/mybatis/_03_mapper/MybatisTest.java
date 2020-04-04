package com.crw.mybatis._03_mapper;

import com.crw.mybatis.mapper.StudentMapper;
import com.crw.mybatis.mapper.StudentMapperAnnoation;
import com.crw.mybatis.model.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StatementHandler：处理sql语句预编译，设置参数等相关工作；
 * ParameterHandler：设置预编译参数用的
 * ResultHandler：处理结果集
 * TypeHandler：在整个过程中，进行数据库类型和javaBean类型的映射
 */
public class MybatisTest {

    public static void main(String[] args) throws IOException {
        crud();
        testGet();

    }

    public static SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "03mapper/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }


    /**
     * 测试增删改
     * 1、mybatis允许增删改直接定义以下类型返回值
     * Integer、Long、Boolean、void
     * 2、我们需要手动提交数据
     * sqlSessionFactory.openSession();===》手动提交
     * sqlSessionFactory.openSession(true);===》自动提交
     *
     * @throws IOException
     */
    public static void crud() throws IOException {

        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1、获取到的SqlSession不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            StudentMapper mapper = openSession.getMapper(StudentMapper.class);
            //测试添加
            Student student = new Student(null, "老陈", 24);
            mapper.insert(student);
            System.out.println(student.getId());

            //测试修改
            Student student2 = new Student(student.getId(), "老陈被修改", 28);
            int update = mapper.update(student2);
            System.out.println(update);

            //测试删除
            mapper.deleteById(student.getId());
            //2、手动提交数据
            openSession.commit();
        } finally {
            openSession.close();
        }

    }

    public static void testGet() throws IOException {

        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1、获取到的SqlSession不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            StudentMapper mapper = openSession.getMapper(StudentMapper.class);
            //Employee employee = mapper.getEmpByIdAndLastName(1, "tom");
            Map<String, Object> map = new HashMap<>();
            map.put("id", 2);
            map.put("lastName", "Tom");
            map.put("tableName", "student");
            Student student = mapper.getByMap(map);

            System.out.println(student);

            List<Student> list = mapper.getListByNameLike("%e%");
            for (Student st : list) {
                System.out.println(st);
            }

            Map<String, Object> map1 = mapper.getByIdReturnMap(1L);
            System.out.println(map1);
        } finally {
            openSession.close();
        }
    }

}
