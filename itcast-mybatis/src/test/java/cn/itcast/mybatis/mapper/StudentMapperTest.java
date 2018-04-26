package cn.itcast.mybatis.mapper;

import cn.itcast.mybatis.pojo.Student;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    @Test
    public void queryAllStudentsInPage() {
        //设置分页:第一个参数：页号，第二个参数：页大小；只对紧接着的查询语句生效
        PageHelper.startPage(2,2);

        List<Student> list = studentMapper.queryAllStudents();

        //构造分页信息对象
        PageInfo<Student> pageInfo = new PageInfo<>(list);

        System.out.println("总记录数为：" + pageInfo.getTotal() + "；总页数为：" + pageInfo.getPages() +
                "；当前页号为：" + pageInfo.getPageNum() + "；页大小为：" + pageInfo.getPageSize());

        for (Student student : pageInfo.getList()) {
            System.out.println(student);
        }
    }
}