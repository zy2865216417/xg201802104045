package cn.edu.sdjzu.xg.bysj.basic.school;

import cn.edu.sdjzu.xg.bysj.domain.School;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

// http://49.235.35.115:8080/bysj1845/myschool/school
@WebServlet("/school.ctl")
public class SchoolController extends HttpServlet {
    // Post http://49.235.35.115:8080/school.ctl  后台
    //  http://49.235.35.115:8080/bysj1845/myschool/addCollege  前台
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        //前台没有为id赋值，此处模拟自动生成id。Dao能实现数据库操作时，应删除此语句。
        schoolToAdd.setId(4 + (int)(Math.random()*100));
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            //增加School对象
            SchoolService.getInstance().add(schoolToAdd);
            //加入数据信息
            message.put("message", "增加成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应
        response.getWriter().println(message);
    }

    // Delete http://49.235.35.115:8080/school.ctl?id=26  后台
    //  http://49.235.35.115:8080/bysj1845/myschool/school?id=8  前台
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            //删除学院
            SchoolService.getInstance().delete(id);
            //加入数据信息
            message.put("message", "删除成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应
        response.getWriter().println(message);
    }

    //Put http://49.235.35.115:8080/school.ctl  后台
    //  http://49.235.35.115:8080/bysj1845/myschool/updateCollege  前端
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //根据request对象，获得代表参数的JSON字串
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为School对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //到数据库表修改School对象对应的记录
            SchoolService.getInstance().update(schoolToAdd);
            //加入数据信息
            message.put("message", "更新成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    //Get http://49.235.35.115:8080/school.ctl  后台
    //    http://49.235.35.115:8080/school.ctl?id=8 后台
    //  http://49.235.35.115:8080/bysj1845/myschool/school
    //  http://49.235.35.115:8080/bysj1845/myschool/detail/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                //调用返回所有school的方法来响应
                responseSchools(response);
            } else {
                //转化int
                int id = Integer.parseInt(id_str);
                //调用返回指定ID的school的方法来响应
                responseSchool(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }

    //  http://49.235.35.115:8080/bysj1845/myschool/school前台
    //响应一个学院对象
    private void responseSchool(int id, HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException {
        //根据id查找学院
        School school = SchoolService.getInstance().find(id);
        //把json类型转化为字符串
        String school_json = JSON.toJSONString(school);
        //响应
        //响应message到前端
        response.getWriter().println(school_json);
    }

    //  http://49.235.35.115:8080/bysj1845/myschool/detail/
    //响应所有学位对象
    private void responseSchools(HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException {
        //获得所有学院
        Collection<School> schools = SchoolService.getInstance().findAll();
        //将对象转化为json格式
        String schools_json = JSON.toJSONString(schools, SerializerFeature.DisableCircularReferenceDetect);
        //响应message到前端
        response.getWriter().println(schools_json);
    }
}
