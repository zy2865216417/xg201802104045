package test;

import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommitTest {
    public static void main(String[] args) {
        //已经向school表中添加了no字段唯一的约束。alter table school add unique(no);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            //获得数据库连接对象
            connection = JdbcHelper.getConn();
            //关闭自动提交
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("insert into school (description,no) values (?,?)");
            preparedStatement.setString(1,"管理学院");
            preparedStatement.setString(2,"02");
            //执行第一条Insert语句
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("insert into school(description,no) values (?,?)");
            preparedStatement.setString(1,"土木学院");
            preparedStatement.setString(2,"22");
            //执行第二条insert语句
            preparedStatement.executeUpdate();
            //提交当前连接所做的操作
            connection.commit();
        }catch (SQLException e){
            System.out.println(e.getMessage()+ "\n errorCode=" + e.getErrorCode());
            try{
                //回滚当前连接所做的操作
                if(connection != null){
                    connection.rollback();
                }
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }finally {
            try{
                //恢复自动提交
                if(connection != null){
                    connection.setAutoCommit(true);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            //关闭资源
            JdbcHelper.close(preparedStatement,connection);
        }
    }
}
