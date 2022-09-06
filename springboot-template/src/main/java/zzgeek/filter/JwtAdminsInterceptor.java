package zzgeek.filter;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import zzgeek.annotions.*;
import zzgeek.commons.meta.ApiException;
import zzgeek.conf.GlobalConfig;
import zzgeek.enums.AdminRole;
import zzgeek.enums.ExceptionCodeAndMsg;
import zzgeek.utlis.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author ZZGeek
 * @date 2021年11月01日 9:41
 * @description JwtAdminsInterceptor
 */
public class JwtAdminsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if(method.getName().equals("error")){
            return true;
        }

        if(method.isAnnotationPresent(VisitorToken.class)){
            return true;
        }else {
            String token = request.getHeader("adminToken");
            if (token == null){
                throw new ApiException(ExceptionCodeAndMsg.NON_ADMINS_TOKEN,this.getClass().getName());
            }
            JwtUtil.verify(token, GlobalConfig.JWTCONSTANT_ADMINS_SECRET_KEY);

            String role = JwtUtil.decodeRole(token);
            if(role.equals(AdminRole.ADMIN.ordinal())){
                if(method.isAnnotationPresent(AdminToken.class)){
                    return true;
                }else {
                    throw new ApiException(ExceptionCodeAndMsg.NON_AUTHORITY,"JwtAdminsInterceptor");
                }
            }

            if(role.equals(AdminRole.ROOTER.ordinal())){
                if(method.isAnnotationPresent(RooterToken.class)){
                    return true;
                }else {
                    throw new ApiException(ExceptionCodeAndMsg.NON_AUTHORITY,"JwtAdminsInterceptor");
                }
            }

            if(role.equals(AdminRole.SITEADMIN.ordinal())){
                if(method.isAnnotationPresent(SiteAdminToken.class)){
                    return true;
                }else {
                    throw new ApiException(ExceptionCodeAndMsg.NON_AUTHORITY,"JwtAdminsInterceptor");
                }
            }

            if(role.equals(AdminRole.SITEROOTER.ordinal())){
                if(method.isAnnotationPresent(SiteRooterToken.class)){
                    return true;
                }else{
                    throw new ApiException(ExceptionCodeAndMsg.NON_AUTHORITY,"JwtAdminsInterceptor");
                }
            }else {
                throw new ApiException(ExceptionCodeAndMsg.INVALID_ADMINS_TOKEN,"JwtAdminsInterceptor");
            }
        }
    }
}
