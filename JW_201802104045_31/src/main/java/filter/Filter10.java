package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Filter10",urlPatterns = "/*")
public class Filter10 implements Filter {
    /*销毁时调用*/
    public void destroy() {
    }

    /*过滤方法 主要是对request和response进行一些处理，然后交给下一个过滤器或Servlet处理*/
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 10 - encoding begins");
        //将req强制类型转换为HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) req;
        //将resp强制类型转换为HttpServletResponse
        HttpServletResponse response = (HttpServletResponse) resp;
        //获得path
        String path = request.getRequestURI();
        //如果不包含字串"/login"，则考虑设置字符编码
        if(!path.contains("/login")){
            //所有资源都设置响应字符编码为UTF-8
            response.setContentType("text/html;charset=UTF-8");
            //如果post或者put在request.getMethod()种
            if("POST-PUT".contains(request.getMethod())){
                //设置请求字符编码为UTF-8
                request.setCharacterEncoding("UTF-8");
            }
        }
        //执行其他过滤器，如过滤器已经执行完毕，则执行原请求
        chain.doFilter(req, resp);
        System.out.println("Filter 10 - encoding ends");
    }

    /*初始化方法  接收一个FilterConfig类型的参数 该参数是对Filter的一些配置*/
    public void init(FilterConfig config) throws ServletException {
    }

}
