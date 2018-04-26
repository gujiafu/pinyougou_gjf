package cn.itcast.mybatis.mapper;

import cn.itcast.mybatis.pojo.Student;
import cn.itcast.mybatis.pojo.Student2;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class Student2MapperTest {

    private Student2Mapper student2Mapper;

    @Before
    public void setUp() throws Exception {

        String resource = "mybatis-config.xml";

        //利用Resources读取mybatis的总配置文件的输入流
        InputStream inputStream = Resources.getResourceAsStream(resource);

        //sqlSessionFactoryBuilder创建sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        MapperHelper mapperHelper = new MapperHelper();
        //特殊配置
        Config config = new Config();
        //设置配置
        mapperHelper.setConfig(config);
        // 注册自己项目中使用的通用Mapper接口，这里没有默认值，必须手动注册
        mapperHelper.registerMapper(Mapper.class);
        //配置完成后，执行下面的操作
        mapperHelper.processConfiguration(sqlSession.getConfiguration());


        student2Mapper = sqlSession.getMapper(Student2Mapper.class);
    }

    @Test
    public void insertSelective() {
        //选择性新增；如果对象中的属性为空的话；那么insert into 语句中则不会出现该字段
        Student2 stu = new Student2();
        stu.setAccount("itcast");
        stu.setBirthday(new Date());
        stu.setAge(12);
        stu.setName("传智播客");
        stu.setGender(0);
        stu.setCreateTime(new Date());

        student2Mapper.insertSelective(stu);
    }

    @Test
    public void selectAll() {
        List<Student2> list = student2Mapper.selectAll();
        for (Student2 student2 : list) {
            System.out.println(student2);
        }
    }

    @Test
    public void selectByPrimaryKey() {
        Student2 student2 = student2Mapper.selectByPrimaryKey(7L);
        System.out.println(student2);
    }

    @Test
    public void selectOne() {
        //如果结果返回是多个的那么查询失败
        Student2 param = new Student2();
        param.setAccount("itcast");
        Student2 student2 = student2Mapper.selectOne(param);
        System.out.println(student2);

    }

    @Test
    public void updateByPrimaryKeySelective() {
        //选择性新增；如果对象中的属性为空的话；那么insert into 语句中则不会出现该字段
        Student2 stu = new Student2();
        stu.setId(7L);
        stu.setName("黑马");
        stu.setGender(1);

        student2Mapper.updateByPrimaryKeySelective(stu);

    }

    @Test
    public void deleteByPrimaryKey() {
        student2Mapper.deleteByPrimaryKey(7L);
    }

    /**
     * 查询第2页（每页2条）年龄大于等于14的男性学生并按照年龄降序排序
     */
    @Test
    public void selectByExample() {
        //设置分页
        PageHelper.startPage(2, 2);

        //创建查询对象
        Example example = new Example(Student2.class);

        //创建查询条件对象
        Example.Criteria criteria = example.createCriteria();

        //男性学生
        criteria.andEqualTo("gender", 1);

        //年龄大于等于14
        criteria.andGreaterThanOrEqualTo("age", 14);

        //按照年龄降序排序
        example.orderBy("age").desc();

        List<Student2> list = student2Mapper.selectByExample(example);

        //创建分页信息对象
        PageInfo<Student2> pageInfo = new PageInfo<>(list);

        System.out.println("总记录数为：" + pageInfo.getTotal() + "；总页数为：" + pageInfo.getPages() +
                "；当前页号为：" + pageInfo.getPageNum() + "；页大小为：" + pageInfo.getPageSize());

        for (Student2 student : pageInfo.getList()) {
            System.out.println(student);
        }
    }
}