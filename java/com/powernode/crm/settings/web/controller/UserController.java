package com.powernode.crm.settings.web.controller;

import com.powernode.crm.commons.constant.LoginConstant;
import com.powernode.crm.settings.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//返回的响应信息，是哪个页面，如果整个页面在一个独立的资源目录，那么整个资源目录就对应整个一个controller类
//一张表对应一个mapper，而一个service只处理一个mapper的业务
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 整个请求地址，要和当前controller方法将来响应信息返回的资源目录保持一致
     * @return
     */
    @RequestMapping("/settings/px/user/toLogin.do")
    public String toLogin(){
        System.out.println("33333");
        return "settings/qx/user/login";
    }
    @ResponseBody
    @RequestMapping("/settings/px/user/login.do")//请求路径。这个方法将来处理好请求之后，响应的信息的资源的路径一致//很显然响应信息依旧回到了登录页面(响应信息响应道哪里，一般是谁发的请求响应道哪里)
    public Object login(HttpServletRequest request, HttpServletResponse response, HttpSession session, String loginAct, String loginPwd, String isRemPwd){
        Map<String,Object> map=new HashMap<>();
        String ip=request.getRemoteHost();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        map.put("ip",ip);
        map.put("time",new Date().getTime());
        Map<String, Object> result = userService.verifyUserByLoinActAndPwd(map);
        if(result.get("code").equals("1")){
            session.setAttribute(LoginConstant.SESSION_USER,result.get(LoginConstant.SESSION_USER));
            if(isRemPwd.equals("true")){
                Cookie username = new Cookie("username",loginAct);
                Cookie password = new Cookie("password",loginPwd);
                username.setMaxAge(10*24*60*60);
                password.setMaxAge(10*24*60*60);
                username.setPath("/");
                password.setPath("/");
                response.addCookie(username);
                response.addCookie(password);
            }else{
//                把没有过期的cookie删掉
                Cookie username=new Cookie("username","1");
                Cookie password=new Cookie("password","1");
                username.setMaxAge(0);
                password.setMaxAge(0);
                response.addCookie(username);
                response.addCookie(password);
            }
        }
        return result;
    }
    @RequestMapping("setting/px/user/logout.do")
    public String logout(HttpSession session,HttpServletResponse response){
        userService.SafeLogOut(response,session);
        return "redirect:/";//框架帮我们重定向的时候已经帮我们加了/crm了.借助springmvc来重定向。，response.sendRedirect(request.getContextPath()+"/")
    }
}
