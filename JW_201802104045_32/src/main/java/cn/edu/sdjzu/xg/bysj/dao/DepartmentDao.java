package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public final class  DepartmentDao {
	private static DepartmentDao departmentDao=new DepartmentDao();
	private DepartmentDao(){}
	public static DepartmentDao getInstance(){
		return departmentDao;
	}

	public Collection<Department> findAll() throws SQLException,ClassNotFoundException{
		Collection<Department>departments = new HashSet<Department>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("select * from department");
		ResultSet resultSet = preparedStatement.executeQuery();
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			departments.add(new Department(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					SchoolService.getInstance().find(resultSet.getInt("school_id"))
					));
		}
		//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		JdbcHelper.close(preparedStatement,connection);
		return departments;
	}

	public Department find(Integer id) throws SQLException,ClassNotFoundException {
		Department department = null;
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("select * from department where id=?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//若结果集仍然有下一条记录，则执行循环体
		if(resultSet.next()){
			department = new Department(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					SchoolService.getInstance().find(resultSet.getInt("school_id"))
			);
		}
		//执行预编译语句
		JdbcHelper.close(preparedStatement,connection);
		return department;
	}
	public Collection<Department> findAllBySchool(Integer schoolId) throws SQLException,ClassNotFoundException{
		//创建集合
		Collection<Department> department = new HashSet<Department>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("select * from department where school_id = ?");
		//赋值
		preparedStatement.setInt(1,schoolId);
		ResultSet resultSet = preparedStatement.executeQuery();
		//若结果集仍然有下一条记录，则执行循环体
		while(resultSet.next()){
			department.add(new Department(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					SchoolService.getInstance().find(resultSet.getInt("school_id"))
			));
		}
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		//返回department集合
		return department;
	}
	public boolean update(Department department) throws SQLException,ClassNotFoundException {
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备语句对象
		PreparedStatement preparedStatement = connection.prepareStatement("update department set description=?,no=?,remarks=?,school_id=? where id=?");
		//为预编译语句参数赋值
		preparedStatement.setString(1,department.getDescription());
		preparedStatement.setString(2,department.getNo());
		preparedStatement.setString(3,department.getRemarks());
		preparedStatement.setInt(4,department.getSchool().getId());
		preparedStatement.setInt(5,department.getId());
		//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		int affectedRowNum = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum >0;
	}

	public boolean add(Department department) throws SQLException,ClassNotFoundException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备语句对象，如果sql语句为多行，注意语句不同部分之间要有空格
		PreparedStatement pstmt = connection.prepareStatement("insert into department" +
				"(no,description,remarks,school_id)" +
				" values (?,?,?,?)");
		pstmt.setString(1, department.getNo());
		pstmt.setString(2, department.getDescription());
		pstmt.setString(3, department.getRemarks());
		pstmt.setInt(4,department.getSchool().getId());
		int affectedRowNum = pstmt.executeUpdate();
		//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		JdbcHelper.close(pstmt, connection);
		return affectedRowNum > 0;
	}
	public boolean delete(Integer id) throws SQLException,ClassNotFoundException{
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String Department_sql = "DELETE FROM department" + " WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(Department_sql);
		//为预编译语句赋值
		pstmt.setInt(1,id);
		int affectedRows = pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
		return affectedRows>0;
	}

	public boolean delete(Department department) throws SQLException,ClassNotFoundException{
		return delete(department.getId());
	}

	public static void main(String[] args) throws SQLException,ClassNotFoundException{
//		//输出原来的信息
//		Department department1 = DepartmentService.getInstance().find(2);
//		//将department对象提取出来
//		System.out.println(department1);
//		//修改
//		department1.setDescription("管理");
//		//更新
//		DepartmentDao.getInstance().update(department1);
//		//找到更新的对象
//		Department department2 = DepartmentService.getInstance().find(2);
//		//输出更新对象的description
//		System.out.println(department2);
		Collection<Department> department = DepartmentService.getInstance().findAllBySchool(8);
		System.out.println(department);
	}
}

