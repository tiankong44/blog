package com.tiankong44.controller.adminController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.User;
import com.tiankong44.service.impl.UserServiceImpl;
import com.tiankong44.util.ConstantUtil;
import com.tiankong44.util.JsonUtils;
import com.tiankong44.util.MD5Utils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author 12481
 * @Date 15:02
 * @Version 1.0
 **/
@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public String loginPage() {

        return "/admin/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public BaseRes login(@RequestBody String msg, HttpSession session, RedirectAttributes RedirectAttributes) {
        BaseRes res = new BaseRes();

        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "username", "password");
            if (checkMap != null) {
                res.setCode(1);
                res.setDesc("请求参数错误");
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(1);
            res.setDesc("请求参数异常");
            return res;
        }

        String username = reqJson.getString("username");
        String password = reqJson.getString("password");
        User user = userService.findUser(username, MD5Utils.code(password));
        if (user != null) {
            user.setPassword("");
            session.setAttribute(user.SESSION_KEY, user);
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setData(user);
            res.setDesc("登陆成功");
        } else {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("登陆失败！用户名或者密码错误！");
        }
        return res;
    }

    @PostMapping("/logout")
    @ResponseBody
    public BaseRes logout(HttpServletRequest request, HttpSession session) {
        BaseRes res=new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        session.removeAttribute(user.SESSION_KEY);
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setDesc("登出成功！");
        return res;

    }


}
