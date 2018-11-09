package com.heqichao.springBootDemo.base.filter;

import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by heqichao on 2018-2-14.
 */
@WebFilter(urlPatterns = "/service/*",filterName = "SessionAppFilter")
public class SessionAppFilter implements Filter {
    private static Map noLoginUrl=new HashMap();
    //允许不登陆的请求
    static {
        noLoginUrl.put("/service/login",new Object());
        noLoginUrl.put("/service/updatePassword",new Object());
        noLoginUrl.put("/service/checkLogin",new Object());
        noLoginUrl.put("/service/liangPost",new Object());
        noLoginUrl.put("/service/liangClear",new Object());
        noLoginUrl.put("/service/getDataChange",new Object());
        noLoginUrl.put("/service/queryLiteAll",new Object());
        noLoginUrl.put("/service/deleteLiteAll",new Object());
        noLoginUrl.put("/service/liteNaCallback2",new Object());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        //设置应用request、response
       HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            String uri = request.getServletPath();
            
            if(noLoginUrl.get(uri) == null&&!uri.contains("/service/nbiotCallback/")){
                if(ServletUtil.getSessionUser() == null){
                    ServletUtil.writeToResponse(response,ServletUtil.NO_LOGIN_RESULT);
                    return;
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception e){
            throw new ResponeException(e);
        }
    }

    @Override
    public void destroy() {

    }

}
