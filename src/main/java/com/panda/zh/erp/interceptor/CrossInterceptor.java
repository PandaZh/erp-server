package com.panda.zh.erp.interceptor;

import com.google.common.base.Strings;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.List;

/**
 * 跨域处理
 *
 * @author Panda.Z
 */
public class CrossInterceptor extends HandlerInterceptorAdapter {

    private List<String> accessAllowedDomain;

    public void setAccessAllowedDomain(List<String> accessAllowedDomain) {
        this.accessAllowedDomain = accessAllowedDomain;
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String referer = request.getHeader("Referer");
        if (!Strings.isNullOrEmpty(referer)){
            URL url = new URL(referer);
            String host = url.getHost().toLowerCase();
            for (String allowed : accessAllowedDomain) {
                if (host.matches(allowed)) {
                    response.setHeader("Access-Control-Allow-Origin", "*");
                    response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                    response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin, X-Requested-With, Content-Type, Accept");
                    response.setHeader("Content-Type", "application/json;charset=utf-8");
                    break;
                }
            }
        }
        return true;
    }
}
