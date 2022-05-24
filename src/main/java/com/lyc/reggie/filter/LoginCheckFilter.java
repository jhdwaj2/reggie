package com.lyc.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.lyc.reggie.common.BaseContent;
import com.lyc.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Date: 2022/5/16
 * Author: 3378
 * Description:
 */

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    private static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
//        获取请求的uri
        String requestURI = request.getRequestURI();
        log.info("拦截到请求:{}",requestURI);

//        定义不需要处理的请求路径
        String[] urls=new String[]{
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout",
                "/favicon.ico"
        };
//        判断请求是否需要处理
        boolean check = check(urls, requestURI);

        if(check){
//            不需要处理直接放行
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request, response);
        }else{
//            判断登录状态
            if(request.getSession().getAttribute("employee")==null){
//                登录状态为空,返回登录页面
                log.info("请求{}被拦截",requestURI);
                log.info("用户未登录");
                response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            }else{
//                已登录,放行
                Long empId = (Long)request.getSession().getAttribute("employee");

                log.info("用户{}已登陆",empId);

                BaseContent.setCurrentId(empId);

                filterChain.doFilter(request, response);
            }
        }

//        long id = Thread.currentThread().getId();
//        log.info("线程id{}",id);

    }

    /**
     * 判断uri是否是不需要拦截的
     * @param uris 不需要拦截的uri集
     * @param requestURI 传入的请求uri
     * @return
     */
    private boolean check(String[] uris,String requestURI){
        for(String uri:uris){
            boolean match = PATH_MATCHER.match(uri, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }

}
