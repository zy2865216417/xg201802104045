package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Degree;
import util.JdbcHelper;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public final class DegreeDao {
    private static DegreeDao degreeDao =
            new DegreeDao();

    private DegreeDao() {
    }

    public static DegreeDao getInstance() {
        return degreeDao;
    }

    public Set<Degree> findAll() throws SQLException,ClassNotFoundException {
        Set<Degree>degrees = new HashSet<Degree>();
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建语句盒子对象
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from degree");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            //以当前记录中的id,description,no,remarks值为参数，创建Degree对象
           degrees.add(new Degree(resultSet.getInt("id"),
                   resultSet.getString("description"),
                   resultSet.getString("no"),
                   resultSet.getString("remarks")));
        }
        //执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
        JdbcHelper.close(stmt,connection);
        return degrees;
    }

    public Degree find(Integer id)  throws SQLException,ClassNotFoundException{
        Degree degree = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("select * from degree where id=?");
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集仍然有下一条记录，则执行循环体
        if(resultSet.next()){
            degree = new Degree(resultSet.getInt("id"),
                    resultSet.getString("description"),
                    resultSet.getString("no"),
                    resultSet.getString("remarks"));
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return degree;
    }


    public boolean update(Degree degree)  throws SQLException,ClassNotFoundException{
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //根据连接对象准备语句对象
        PreparedStatement preparedStatement = connection.prepareStatement("update degree set description=?,no=?,remarks=? where id=?");
        //为预编译语句赋值
        preparedStatement.setString(1,degree.getDescription());
        preparedStatement.setString(2,degree.getNo());
        preparedStatement.setString(3,degree.getRemarks());
        preparedStatement.setInt(4,degree.getId());
        //执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
        int affectedRowNum = preparedStatement.executeUpdate();
        JdbcHelper.close(preparedStatement,connection);
        System.out.println("更新了"+affectedRowNum+"条记录");
        return affectedRowNum >0;
    }

    public boolean add(Degree degree) throws SQLException,ClassNotFoundException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //根据连接对象准备语句对象，如果sql语句为多行，注意语句不同部分之间要有空格
        PreparedStatement pstmt = connection.prepareStatement("insert into degree" +
                "(no,description,remarks)" +
                " values (?,?,?)");
        pstmt.setString(1, degree.getNo());
        pstmt.setString(2, degree.getDescription());
        pstmt.setString(3, degree.getRemarks());
        int affectedRowNum = pstmt.executeUpdate();
        //执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
        JdbcHelper.close(pstmt, connection);
        return affectedRowNum > 0;
    }

    public boolean delete(Integer id) throws SQLException,ClassNotFoundException{
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //创建sql语句
        String deleteDegree_sql = "DELETE FROM degree" + " WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt = connection.prepareStatement(deleteDegree_sql);
        //为预编译语句赋值
        pstmt.setInt(1,id);
        int affectedRows = pstmt.executeUpdate();
        JdbcHelper.close(pstmt,connection);
        return affectedRows>0;
    }

    public boolean delete(Degree degree) throws SQLException,ClassNotFoundException{
        return delete(degree.getId());
    }

    public static void main(String[] args) throws SQLException,ClassNotFoundException{
        //获得对应的degree对象
        Degree degree1 = DegreeDao.getInstance().find(13);
        //读出数据库中的原来的信息
        System.out.println(degree1.getDescription());
        //修改
        degree1.setDescription("博士2");
        //更新
        DegreeDao.getInstance().update(degree1);
        //找出更新对象
        Degree degree2 = DegreeDao.getInstance().find(13);
        //输出更新对象的description
        System.out.println(degree2.getDescription());
    }
}

