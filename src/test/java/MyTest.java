import cn.kgc.dao.BooksMapper;
import cn.kgc.domain.Books;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTest {
    private SqlSession sqlSession;
    //单元格测试之前运行，得到sqlSession对象
    @Before
    public void init() throws Exception{
        //读取mybatis的核心配置文件
        Reader reader = Resources.getResourceAsReader("sqlMapConfig.xml");
        //创建工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        //得到sqlSession对象
        sqlSession = sqlSessionFactory.openSession();
    }
    //查询全部
    @Test
    public void selectTest(){
        List<Books> booksList = sqlSession.selectList("dao.BooksMapper.selectBooks");
        for (Books books : booksList) {
            System.out.println(books);
        }
    }
    //查询全部
    @Test
    public void selectTest11(){
        List<Books> booksList = sqlSession.getMapper(BooksMapper.class).getBooksList();
        for (Books books : booksList) {
            System.out.println(books);
        }
    }
    //价格区间查询
    @Test
    public void selectTest2(){
        Map<String,Integer> map = new HashMap<>();
        map.put("minPrice",20);
        map.put("maxPrice",100);
        List<Map<String,Object>> mapList = sqlSession.selectList("dao.booksMapper.selectBooksByPrice",map);
        /*for (Map<String, Object> stringObjectMap : mapList) {
            System.out.println(mapList);
        }*/
        mapList.forEach(temp-> System.out.println(temp));
    }

    //价格和数量区间查询
    @Test
    public void selectTest3(){
        Books books = new Books();
        books.setNum(100);
        books.setPrice(20.0);
        List<Books> booksList = sqlSession.selectList("dao.booksMapper.selectBooksBySome", books);
        for (Books books1 : booksList) {
            System.out.println(books1);
        }
    }
    //模糊查询
    // 第一种方法：配置文件"%"#{bookName}"%",传参："入门";
    @Test
    public void selectTest4(){
        List<Books> booksList = sqlSession.selectList("dao.booksMapper.selectBooksByName", "入门");
        for (Books books : booksList) {
            System.out.println(books);
        }
    }
    //第二种方法：配置文件：#{bookName} ,,传参："%入门%"
    @Test
    public void selectTest5(){
        List<Books> booksList = sqlSession.selectList("dao.booksMapper.selectBooksByName2", "%优秀%");
        for (Books books : booksList) {
            System.out.println(books);
        }
    }
    //第三种方法：配置文件："%${value}%" , 传参："入门"
    @Test
    public void selectTest6(){
        List<Books> booksList = sqlSession.selectList("dao.booksMapper.selectBooksByName3", "mysql");
        for (Books books : booksList) {
            System.out.println(books);
        }
    }
    @Test
    public void selectTest7(){
        Books books = new Books();
        books.setBookName("入门");
        List<Books> booksList = sqlSession.selectList("dao.booksMapper.selectBooksByName4", books);
        for (Books book : booksList) {
            System.out.println(book);
        }
    }
    //增加
    @Test
    public void insertBooks(){
        Books books = new Books(null,"mybatis从入门到放弃",1300.0,5);
        int insert = sqlSession.insert("dao.booksMapper.insertBooks",books);
        /*注意：增/删/改 都要提交事务*/
        sqlSession.commit();
        System.out.println("受影响的行数："+insert);
    }

    //删除
    @Test
    public void deleteBooks(){
        int delete = sqlSession.delete("dao.booksMapper.deleteBooks", 8);
        sqlSession.commit();
        System.out.println("受影响的行数："+delete);
    }
    //修改
    @Test
    public void updateBooks(){
        Books books = new Books(9,"mybatis从入门到优秀！！！！！！！！",1111.99,2);
        int update = sqlSession.update("dao.booksMapper.updateBooks", books);
        sqlSession.commit();
        System.out.println("受影响的行数："+update);
    }
}
