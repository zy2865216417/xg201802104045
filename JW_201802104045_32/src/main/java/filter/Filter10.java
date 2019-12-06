package filter;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter10",urlPatterns = "/*")
public class Filter10 implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 10 login begins");
        JSONObject message = new JSONObject();
        //将req强制类型转换为HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) req;
        //将resp强制类型转换为HttpServletResponse
        HttpServletResponse response = (HttpServletResponse) resp;
        //获得path
        String path = request.getRequestURI();
        //如果不包含字串"/login"，
        if(!path.contains("/login")){
            //访问权限验证
            HttpSession session = request.getSession(false);
            if(session==null || session.getAttribute("currentUser") == null){
                message.put("message","您没有登录，请登录");
                //响应到前端
                response.getWriter().println(message);
                //返回登录页面
                return;
            }
        }
        chain.doFilter(req, resp);
        System.out.println("Filter 10 login ends");
        return;
    }
    public void init(FilterConfig config) throws ServletException {
    }

}
