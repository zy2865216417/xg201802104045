package cn.edu.sdjzu.xg.bysj.basic.security;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //所有资源都设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        if(session != null){
            session.invalidate();
        }
    }
}
