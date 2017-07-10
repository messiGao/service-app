package com.gyc.interceptor;  
	  
import com.gyc.dto.user;
import com.gyc.common.Constants;
import com.gyc.common.RedisUtils;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Component;  
import org.springframework.web.method.HandlerMethod;  
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;  	  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import java.lang.reflect.Method;  	  
import static com.gyc.common.Constants.MOBILE_NUMBER_SESSION_KEY;  
import static com.gyc.common.Constants.ACCESS_TOKEN;  
import static com.gyc.common.Constants.USER_ID_SESSION_KEY;  
	  
	/** 
	 * Created by messigao. 
	 */  
	@Component  
	public class LoginInterceptor extends HandlerInterceptorAdapter {  
	    @Autowired  
	    private RedisUtils redisUtils;  
	      
	    public boolean preHandle(HttpServletRequest request,  
	                             HttpServletResponse response, Object handler) throws Exception {  
	        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {  
	            return true;  
	        }  
	        handlerSession(request);  
	  
	        final HandlerMethod handlerMethod = (HandlerMethod) handler;  
	        final Method method = handlerMethod.getMethod();  
	        final Class<?> clazz = method.getDeclaringClass();  
	        if (clazz.isAnnotationPresent(Auth.class) ||  
	                method.isAnnotationPresent(Auth.class)) {  
	            if(request.getAttribute(USER_ID_SESSION_KEY) == null){  
	    
	                 //throw new Exception();  
	            	HttpServletResponse httpResponse = (HttpServletResponse) response;  
	                httpResponse.setCharacterEncoding("UTF-8");    
	                httpResponse.setContentType("application/json; charset=utf-8");   
	                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  
	                httpResponse.getWriter().write("认证不通过");
	                return false;
	      
	                 
	            }else{  
	                return true;  
	            }  
	        }  
	  
	        return true;  
	  
	    }  
	    public void  handlerSession(HttpServletRequest request) {  
	        String accessToken = request.getHeader(ACCESS_TOKEN);  
	        if(org.apache.commons.lang3.StringUtils.isBlank(accessToken)){  
	            accessToken=(String) request.getSession().getAttribute(ACCESS_TOKEN);  
	        }  
	        if (org.apache.commons.lang3.StringUtils.isNotBlank(accessToken)) {  
	            user model = (user) redisUtils.get(Constants.SESSION_KEY_PREFIX+accessToken);  
	            if (model == null) {  
	                return ;  
	            }  
	            request.setAttribute(ACCESS_TOKEN,accessToken);  
	            Integer userId = model.getUserId();  
	            if (userId != null) {  
	                request.setAttribute(USER_ID_SESSION_KEY, userId);  
	            }  
	            String mobile = model.getPhone();  
	            if (mobile != null) {  
	                request.setAttribute(MOBILE_NUMBER_SESSION_KEY, mobile);  
	            }  
	        }  
	        return ;  
	    }  
	}  
	
	

