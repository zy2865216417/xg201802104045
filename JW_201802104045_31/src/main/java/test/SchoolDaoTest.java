package test;

import cn.edu.sdjzu.xg.bysj.dao.SchoolDao;
import cn.edu.sdjzu.xg.bysj.domain.School;

import java.sql.SQLException;

public class SchoolDaoTest {
    public static void main(String[] args) throws SQLException,ClassNotFoundException {
        //创建schoolToAdd对象
        School schoolToAdd = new School("计算机","02","ok");
        //创建Dao对象
        SchoolDao schoolDao = new SchoolDao();
        //执行Dao对象的方法
        School addedSchool = schoolDao.addWithSP(schoolToAdd);
        //打印添加返回的对象
        System.out.println(addedSchool);
        System.out.println("添加成功");
    }
}

