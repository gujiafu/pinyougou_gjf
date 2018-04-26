package cn.itcast.mybatis.mapper;

import cn.itcast.mybatis.pojo.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class StudentMapperTest {

    private StudentMapper studentMapper;

    @Before
    public void setUp() throws Exception {

        String resource = "mybatis-config.xml";

        //利用Resources读取mybatis的总配置文件的输入流
        InputStream inputStream = Resources.getResourceAsStream(resource);

        //sqlSessionFactoryBuilder创建sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        studentMapper = sqlSession.getMapper(StudentMapper.class);
    }

    @Test
    public void queryAllStudents() {
        List<Student> list = studentMapper.queryAllStudents();
        for (Student student : list) {
            System.out.println(student);
        }
    }
}