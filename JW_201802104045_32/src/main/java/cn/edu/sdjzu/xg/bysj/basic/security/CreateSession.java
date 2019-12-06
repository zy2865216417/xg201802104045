package cn.edu.sdjzu.xg.bysj.basic.security;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/createSession")
public class CreateSession extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //如果当前请求对应着服务器内存中的一个session对象，则返回该对象
        //如果服务器内存中没有session对象与当前请求对应，则创建一个session对象并返回该对象
        HttpSession session = request.getSession();
        //如果session不活跃大于60秒钟，则该session失效
        session.setMaxInactiveInterval(60);
        response.getWriter().println("session will last 60 seconds");
    }
}
