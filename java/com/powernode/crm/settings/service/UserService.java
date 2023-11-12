package com.powernode.crm.settings.service;

import com.powernode.crm.settings.pojo.User;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String,Object> verifyUserByLoinActAndPwd(Map<String,Object> map);
    void SafeLogOut(HttpServletResponse response, HttpSession session);
    List<User> queryAllUsers();
}
