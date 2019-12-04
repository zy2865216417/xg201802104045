package cn.edu.sdjzu.xg.bysj.basic.teacher;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
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

@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {
    //Post http://49.235.35.115:8080/teacher.ctl
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //创建JSON对象,以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //向数据库中添加Teacher类型的对象
            TeacherService.getInstance().add(teacherToAdd);
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

    // Delete http://49.235.35.115:8080/teacher.ctl?id=
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //转化为int类型你
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            //到数据库中删除对应的老师
            TeacherService.getInstance().delete(id);
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

    //Put http://49.235.35.115:8080/teacher.ctl
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher象
        Teacher teacherToUpdate = JSON.parseObject(teacher_json, Teacher.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加加Teacher对象
            TeacherService.getInstance().update(teacherToUpdate);
            //加入数据信息
            message.put("message", "修改成功");
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

    //Get http://49.235.35.115:8080/teacher.ctl
    //    http://49.235.35.115:8080/teacher.ctl?id=
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                //调用返回所有teacher的方法来响应
                responseTeachers(response);
            } else {
                //转化int
                int id = Integer.parseInt(id_str);
                //调用返回指定ID的teacher的方法来响应
                responseTeacher(id, response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            response.getWriter().println(message);
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            response.getWriter().println(message);
        }
    }

    //响应一个老师对象
    private void responseTeacher(int id, HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException {
        //根据id查找老师
        Teacher teacher = TeacherService.getInstance().find(id);
        //把json类型转化为字符串
        String teacher_json = JSON.toJSONString(teacher, SerializerFeature.DisableCircularReferenceDetect);
        //响应
        //响应message到前端
        response.getWriter().println(teacher_json);
    }

    //响应所有学位对象
    private void responseTeachers(HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException {
        //获得所有学院
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        //将对象转化为json格式
        String teachers_json = JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);
        //响应
        //响应message到前端
        response.getWriter().println(teachers_json);
    }
}
