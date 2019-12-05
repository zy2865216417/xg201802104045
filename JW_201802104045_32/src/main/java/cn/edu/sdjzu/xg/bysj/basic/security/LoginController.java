package cn.edu.sdjzu.xg.bysj.basic.security;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //所有资源都设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //获取用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //创建json对象message，以便王前端响应数据
        JSONObject message = new JSONObject();
        try{
            //定义User对象
            User loggedUser = UserService.getInstance().login(username,password);
            if(loggedUser != null){
                message.put("message","登录成功");
                HttpSession session = request.getSession();
                //十分钟没有操作，则使session失效
                session.setMaxInactiveInterval(10*60);
                session.setAttribute("currentUser",loggedUser);
                response.getWriter().println(message);
                //返回
                return;
            }else {
                message.put("message","用户名或密码错误");
            }
        }catch (SQLException e){
            message.put("message","数据库操作异常");
            e.printStackTrace();
        }catch (Exception e){
            message.put("message","网络异常");
            e.getMessage();
        }
        response.getWriter().println(message);
    }
}
