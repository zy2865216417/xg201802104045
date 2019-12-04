package cn.edu.sdjzu.xg.bysj.basic.degree;

import cn.edu.sdjzu.xg.bysj.domain.Degree;
import cn.edu.sdjzu.xg.bysj.service.DegreeService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/degree.ctl")
public class DegreeController extends HttpServlet {
    //Post http://49.235.35.115:8080/degree.ctl
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            //增加Degree对象
            DegreeService.getInstance().add(degreeToAdd);
            //加入数据信息
            resp.put("message", "增加成功");
        } catch (SQLException e) {
            resp.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            resp.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应
        response.getWriter().println(resp);
    }

    //Delete http://49.235.35.115:8080/degree.ctl?id=22
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            //删除学位
            DegreeService.getInstance().delete(id);
            //加入数据信息
            resp.put("message", "删除成功");
        } catch (SQLException e) {
            resp.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            resp.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应
        response.getWriter().println(resp);
    }

    //Put http://49.235.35.115:8080/degree.ctl
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //根据request对象，获得代表参数的JSON字串
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        JSONObject message = new JSONObject();
        try {
            //到数据库表修改degree对象对应的记录
            DegreeService.getInstance().update(degreeToAdd);
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

    //Get http://49.235.35.115:8080/degree.ctl
    //    http://49.235.35.115:8080/degree.ctl?id=16
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                //调用返回所有degree的方法来响应
                responseDegrees(response);
            } else {
                //转化int
                int id = Integer.parseInt(id_str);
                //调用返回指定ID的degree的方法来响应
                responseDegree(id, response);
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

    //响应一个学位对象
    private void responseDegree(int id, HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException {
        //根据id查找学位
        Degree degree = DegreeService.getInstance().find(id);
        //把json类型转化为字符串
        String degree_json = JSON.toJSONString(degree);
        //响应message到前端
        response.getWriter().println(degree_json);
    }

    //响应所有学位对象
    private void responseDegrees(HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException {
        //获得所有学位
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        //将对象转化为json格式
        String degrees_json = JSON.toJSONString(degrees);
        //响应message到前端
        response.getWriter().println(degrees_json);
    }
}
