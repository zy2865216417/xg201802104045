package cn.edu.sdjzu.xg.bysj.basic.user;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user.ctl")
public class UserController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = null;
        //读取参数
        String username_str = request.getParameter("username");
        String password_str = request.getParameter("password");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try{
            //调用登录方法
            user = UserService.getInstance().login(username_str,password_str);
            //加入数据信息
            message.put("message", "登录成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","网络异常");
            e.getMessage();
        }
        //将对象转化为json格式
        String user_str = JSON.toJSONString(user, SerializerFeature.DisableCircularReferenceDetect);
        //向前台输出
        response.getWriter().println(user_str);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数
        String id_str = request.getParameter("id");
        String username_str = request.getParameter("username");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try{
            if(id_str!=null){
                //转化int
                int id = Integer.parseInt(id_str);
                //调用按照ID查找的方法
                responseById(id,response);
            }else {
                //调用按照用户名查找的方法
                responseByUsername(username_str,response);
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            response.getWriter().println(message);
        }catch (Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
            response.getWriter().println(message);
        }
    }

    public void responseById(int id, HttpServletResponse response) throws SQLException,ClassNotFoundException, IOException{
        User user = UserService.getInstance().find(id);
        //将对象转化为json字串
        String users_json = JSON.toJSONString(user, SerializerFeature.DisableCircularReferenceDetect);
        response.getWriter().println(users_json);
    }
    public void responseByUsername(String username, HttpServletResponse response)throws SQLException,ClassNotFoundException, IOException{
        User user = UserService.getInstance().findByUsername(username);
        //将对象转化为json字串
        String users_json = JSON.toJSONString(user, SerializerFeature.DisableCircularReferenceDetect);
        response.getWriter().println(users_json);
    }
}
