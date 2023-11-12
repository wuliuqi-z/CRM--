package com.powernode.crm.settings.service.impl;

import com.powernode.crm.commons.constant.LoginConstant;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.settings.mapper.UserMapper;
import com.powernode.crm.settings.pojo.User;
import com.powernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public Map<String,Object> verifyUserByLoinActAndPwd(Map<String, Object> map) {
        Map<String,Object> result=new HashMap<>();
        System.out.println(userMapper);
        User user = userMapper.selectUserByLoginActAndPwd(map);
        if(user==null){
            result.put("code",LoginConstant.RETURN_OBJECT_CODE_FAIL);
            result.put("reason","账户或者密码错误");
            return result;
        }else{
            String[] allowIps = user.getAllowIps().split(",");
            String ip=(String) map.get("ip");
            Date expireTime=DateUtils.StrToDate(user.getExpireTime());
            long exTime=expireTime.getTime();
            if(exTime<(long)map.get("time")){
                result.put("code", LoginConstant.RETURN_OBJECT_CODE_FAIL);
                result.put("reason","账户已经失效");
                return result;
            }
            if("0".equals(user.getLockState())){
                result.put("code",LoginConstant.RETURN_OBJECT_CODE_FAIL);
                result.put("reason","账号被锁定");
                return result;
            }
            for(String Ip:allowIps){
                if(ip.equals(Ip)){
                    result.put("code",LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
                    result.put(LoginConstant.SESSION_USER,user);
                    return result;
                }

            }
        }
        result.put("code",LoginConstant.RETURN_OBJECT_CODE_FAIL);
        result.put("reason","ip地址不允许");
        return result;
    }

    @Override
    public void SafeLogOut(HttpServletResponse response, HttpSession session) {
        Cookie username=new Cookie("username","1");
        Cookie password=new Cookie("password","1");
        username.setPath("/");
        password.setPath("/");
        username.setMaxAge(0);
        password.setMaxAge(0);
        response.addCookie(username);
        response.addCookie(password);
//        销毁session
        session.invalidate();
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAll();
    }
}
