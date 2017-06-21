package com.zhaoyun.docmanager.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zhaoyun.docmanager.biz.service.NavBarService;
import com.zhaoyun.docmanager.common.constants.NavTypeEnum;
import com.zhaoyun.docmanager.model.vo.NavBarContext;

/**
 * 导航栏
 * @author user
 * @version $Id: NavbarInterceptor.java, v 0.1 2016年8月26日 下午5:57:02 user Exp $
 */
public class NavbarInterceptor extends HandlerInterceptorAdapter {

    Logger        logger = LoggerFactory.getLogger(NavbarInterceptor.class);

    @Autowired
    NavBarService navBarService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String navType = request.getParameter("navType");
        String navId = request.getParameter("navId");
        if (navType != null) {
            try {
                NavBarContext navBarContext = new NavBarContext();
                navBarContext.setNavType(navType);
                NavTypeEnum navTypeEnum = NavTypeEnum.valueOf(navType.toUpperCase());

                switch (navTypeEnum) {
                    case APP:
                    case MESSAGE:
                    case ROOT_API:
                        navBarContext.setAppId(Integer.valueOf(navId));
                        break;
                    case SERVICE:
                    case DEPRECATED_SERVICE:
                        navBarContext.setServiceId(Integer.valueOf(navId));
                        break;
                    case API:
                    case DEPRECATED_API:
                    case DEPRECATED_SERVICE_API:
                        navBarContext.setApiId(Integer.valueOf(navId));
                        break;
                    default:
                        break;
                }

                navBarService.doService(navBarContext, navTypeEnum);
                while (navTypeEnum.getParentType() != null) {
                    navTypeEnum = navTypeEnum.getParentType();
                    navBarService.doService(navBarContext, navTypeEnum);
                }
                modelAndView.addObject("navbar", navBarContext);
            } catch (Exception e) {
                logger.error("navBarContext error", e);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 注意要清除threadlocal
    }
}
