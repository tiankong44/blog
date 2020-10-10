package com.tiankong44.controller.adminController;

import com.tiankong44.model.User;
import com.tiankong44.service.impl.UserServiceImpl;
import com.tiankong44.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

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
    public String login(@Param("username") String username, @Param("password") String password, HttpSession session, RedirectAttributes RedirectAttributes) {


        User user = userService.findUser(username, MD5Utils.code(password));
        if (user != null) {
            user.setPassword("");
            session.setAttribute("user",user);
            session.setAttribute("user_id", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("avatar", user.getAvatar());
            session.removeAttribute("failedmessage");
            return "redirect:/admin/blogs";
        } else {
            session.setAttribute("failedmessage", "用户名或者密码错误！");

            return "redirect:/admin ";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("avatar");
        session.removeAttribute("failedmessage");

        return "redirect:/admin ";

    }


}
