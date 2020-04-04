package com.crw.mybatis._02_config;

import com.crw.mybatis.mapper.StudentMapper;
import com.crw.mybatis.mapper.StudentMapperAnnoation;
import com.crw.mybatis.model.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 1、SqlSession代表和数据库的一次会话；用完必须关闭；
 * 2、SqlSession和connection都是非线程安全。每次使用都应该去获取新的对象。
 * 3、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。（将接口和xml进行绑定）
 * 4、两个重要的配置文件：
 * - mybatis的全局配置文件：包含数据库连接池信息，事务管理器信息等...系统运行环境信息
 * - sql映射文件：保存了每一个sql语句的映射信息：
 * 将sql抽取出来。
 */
public class MybatisTest {

    public static void main(String[] args) throws IOException {
        test01();
        test02();
        test03();
    }

    public static SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "02config/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    public static void test01() throws IOException {

        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            Student student = openSession.selectOne(
                    "com.crw.mybatis.mapper.StudentMapper.getById", 1);
            System.out.println(student);
        } finally {
            openSession.close();
        }

    }

    public static void test02() throws IOException {
        // 1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        // 2、获取sqlSession对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            // 3、获取接口的实现类对象
            //会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
            StudentMapper mapper = openSession.getMapper(StudentMapper.class);
            Student student = mapper.getById(1L);
            System.out.println(mapper.getClass());
            System.out.println(student);
        } finally {
            openSession.close();
        }
    }

    public static void test03() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            StudentMapperAnnoation mapper = openSession.getMapper(StudentMapperAnnoation.class);
            Student student = mapper.getById(1L);
            System.out.println(student);
        } finally {
            openSession.close();
        }
    }

}
