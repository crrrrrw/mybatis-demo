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
 * <p>
 * <p>
 * 补充点解释：
 * 单个参数：mybatis不会做特殊处理，
 *          #{参数名/任意名}：取出参数值。
 * <p>
 * 多个参数：mybatis会做特殊处理。
 *          多个参数会被封装成 一个map，
 *              key：param1...paramN,或者参数的索引也可以
 *              value：传入的参数值
 *          #{}就是从map中获取指定的key的值；
 * <p>
 * 【命名参数】：明确指定封装参数时map的key；@Param("id")
 *              多个参数会被封装成 一个map：
 *              key：使用@Param注解指定的值
 *              value：参数值
 *             #{指定的key}取出对应的参数值
 * <p>
 * POJO：
 * 如果多个参数正好是我们业务逻辑的数据模型，我们就可以直接传入pojo；
 * #{属性名}：取出传入的pojo的属性值
 * <p>
 * Map：
 * 如果多个参数不是业务模型中的数据，没有对应的pojo，不经常使用，为了方便，我们也可以传入map
 * #{key}：取出map中对应的值
 * <p>
 * TO：
 * 如果多个参数不是业务模型中的数据，但是经常要使用，推荐来编写一个TO（Transfer Object）数据传输对象
 * Page{
 * int index;
 * int size;
 * }
 * <p>
 * ========================思考================================
 * public Student get(@Param("id")Integer id,String lastName);
 * 取值：id==>#{id/param1}   lastName==>#{param2}
 * <p>
 * public Student get(Integer id,@Param("s")Student stud);
 * 取值：id==>#{param1}    lastName===>#{param2.name/s.name}
 * <p>
 * ##特别注意：如果是Collection（List、Set）类型或者是数组，也会特殊处理。也是把传入的list或者数组封装在map中。
 * key：Collection（collection）,如果是List还可以使用这个key(list)
 * 数组(array)
 * public Student getById(List<Integer> ids);
 * 取值：取出第一个id的值：   #{list[0]}
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
