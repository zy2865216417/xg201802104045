package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebFilter(filterName = "Filter20",urlPatterns = "/*")
public class Filter20 implements Filter {
    /*销毁时调用*/
    public void destroy() {
    }
    /*过滤方法 主要是对request和response进行一些处理，然后交给下一个过滤器或Servlet处理*/
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter 20 - encoding begins");
        //将resp强制类型转换为HttpServletResponse
        HttpServletRequest request = (HttpServletRequest) req;
        //获取当前系统时间
        Date date=new Date();
        //设置日期格式   HH:mm:ss中的HH大写为24小时制。HH和hh的差别是前者为24小时制，后者为12小时制
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(request.getRequestURI() + "@" + df.format(date));
        //执行其他过滤器，如过滤器已经执行完毕，则执行原请求
        chain.doFilter(req, resp);
        System.out.println("Filter 20 - encoding ends");
    }
    /*初始化方法  接收一个FilterConfig类型的参数 该参数是对Filter的一些配置*/
    public void init(FilterConfig config) throws ServletException {
    }

}
