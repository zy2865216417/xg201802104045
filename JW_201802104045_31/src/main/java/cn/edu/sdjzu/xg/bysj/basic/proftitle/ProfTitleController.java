package cn.edu.sdjzu.xg.bysj.basic.proftitle;

import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import cn.edu.sdjzu.xg.bysj.service.ProfTitleService;
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

@WebServlet("/profTitle.ctl")
public class ProfTitleController extends HttpServlet {
    //Post http://49.235.35.115:8080/profTitle.ctl
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            //增加ProfTitle对象
            ProfTitleService.getInstance().add(profTitleToAdd);
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

    //Delete http://49.235.35.115:8080/profTitle.ctl?id=8
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject message = new JSONObject();
        try {
            //删除学院
            ProfTitleService.getInstance().delete(id);
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

    //Put http://49.235.35.115:8080/profTitle.ctl
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //到数据库profTitle对象对应的记录
            ProfTitleService.getInstance().update(profTitleToAdd);
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

    //Get http://49.235.35.115:8080/profTitle.ctl
    //    http://49.235.35.115:8080/profTitle.ctl?id=5
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                //调用返回所有profTitle的方法来响应
                responseProfTitles(response);
            } else {
                //转化int
                int id = Integer.parseInt(id_str);
                //调用返回指定ID的profTitle的方法来响应
                responseProfTitle(id, response);
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

    //响应一个头衔对象
    private void responseProfTitle(int id, HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException {
        //根据id查找title
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String profTitle_json = JSON.toJSONString(profTitle);
        //响应message到前端
        response.getWriter().println(profTitle_json);
    }

    //响应所有学位对象
    private void responseProfTitles(HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException {
        //获得所有学院
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().findAll();
        String profTitles_json = JSON.toJSONString(profTitles);
        //响应message到前端
        response.getWriter().println(profTitles_json);
    }
}
