package com.sw.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.order.config.handler.GlobalExceptionHandler.UniversalException;
import com.sw.order.entity.*;
import com.sw.order.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Map;

/**
 * 用户增删改查
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpSession session;

    private String userFilePath = new File("").getCanonicalPath() + "\\src\\main\\resources" + File.separator + "static" + File.separator + "userfile";

    public UserController() throws IOException {
    }

    /**
     * 当前用户上传头像
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/user/uploadAvatar")
    public Result uploadImg(MultipartFile file) throws IOException {
        if(file == null) {
            return Result.clientError("请上传正确的文件");
        }
        User user = (User) session.getAttribute("user");
        File file1 = new File(userFilePath,  user.getUsername());
        if(file1.exists() == false)
            file1.mkdirs();
        file.transferTo(new File(file1, "avatar.jpg"));
        return Result.ok();
    }

    /**
     * 获取当前登录用户的头像
     * @return
     * @throws IOException
     */
    @GetMapping("/user/downloadAvatar")
    public ResponseEntity<Resource> downloadImg() throws IOException {
        User user = (User) session.getAttribute("user");
        File file1 = new File(userFilePath,  user.getUsername());
        if(file1.exists() == false) {
            file1 = new File(userFilePath, "default");
        }
//        file1.createNewFile();

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"avatar.jpg\"")
                .body(new UrlResource(new File(file1, "avatar.jpg").toURI()));
    }

    /**
     * 获取用户头像
     * @param username
     * @return
     * @throws IOException
     */
    @GetMapping("/user/downloadAvatar/{username}")
    public ResponseEntity<Resource> download(@PathVariable("username") String username) throws IOException {
        File file1 = new File(userFilePath,  username);
        if(file1.exists() == false) {
            file1 = new File(userFilePath, "default");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"avatar.jpg\"")
                .body(new UrlResource(new File(file1, "avatar.jpg").toURI()));
    }

    @GetMapping("/loginOut")
    public Result loginOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {//清除认证
            new SecurityContextLogoutHandler().logout(request, response, auth);
            session.invalidate();
            return Result.ok();
        }
        return Result.clientError();
    }

    /**
     * 用户注册
     */
    @PostMapping("/register.do")
    public Result register(@RequestBody User user) throws UniversalException {
        if(!user.getUsername().matches("[0-9a-zA-Z\\u4E00-\\u9FA5]{4,16}") ||
                !user.getPassword().matches("[0-9a-zA-Z]{8,16}")) {
            return Result.clientError("用户名或密码有误");
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", user.getUsername());
        if(userService.getOne(queryWrapper) != null) {
            // 用户已存在
            return Result.clientError("用户已存在");
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userService.save(user);
        return Result.ok("注册成功");
    }

    @RequestMapping("/putwallet")
    public Result wallet(Integer n, HttpSession session) {
        if(n <= 0 || n > 100000) {
            return Result.clientError();
        }
        User user = (User) session.getAttribute("user");
        user.setWallet(user.getWallet() + n);
        userService.updateById(user);

        return Result.ok();
    }

    /**
     * 新增或删除用户
     */
    @Secured("ROLE_USER_SOU")
    @RequestMapping("/user/sou")
    public Result userAdd( @RequestBody User user, HttpSession session) {
//        if(!(user1.getPassword().equals(user.getPassword()) && user1.getUserId().intValue() == user.getUserId().intValue() && user1.getUsername().equals(user.getUsername()))) {
//            return Result.clientError("您无权更改用户密码");
//        }
//        if() {
//        } else if(user.getPassword().matches("[0-9a-zA-Z]{8,16}")){
//
//        }
        userService.saveOrUpdate(user);
        session.setAttribute("user", user);
        return Result.ok();
    }

    /**
     * 删除用户
     *
     */
    @Secured("ROLE_USER_DEL")
    @RequestMapping("/user/del")
    public Result goodDel(Integer userId) {
        return userService.removeById(userId) ? Result.ok() : Result.clientError();
    }

    /**
     * 获取用户列表
     */
    @Secured("ROLE_USER_SHOW")
    @RequestMapping("/userlist")
    public Result goods(@RequestBody Map<String, String> map) {
        Long curPage = Long.parseLong(map.get("curPage"));
        Long pageSize = Long.parseLong(map.get("pageSize"));
        if(curPage < 1 && (pageSize >= 10 && pageSize <= 200)) {
            return Result.clientError("页码错误");
        }
        String search = map.get("search");
        String search_content = map.get("search_content");
        Long n = null;
        boolean isName = false;
        boolean isId = false;
        if(search != null && search_content != null) {
            if(search_content.length() > 0) {
                if("username".equals(search)) {
                    isName = true;
                } else if("userId".equals(search)) {
                    n = Long.parseLong(search_content);
                    isId = true;
                }
            }
        }
        QueryWrapper qe = new QueryWrapper();
        qe.like(isName, search, search_content);
        qe.eq(isId,"user_id", n);
        IPage<User> iPage = userService.page(new Page<>(curPage, pageSize), qe);
        ResultPage<User> ePage = new ResultPage<>();
        ePage.setCurrent((long)curPage);
        ePage.setSize(pageSize);
        ePage.setTotal(iPage.getTotal());
        ePage.setEList(iPage.getRecords());
        return Result.ok().put("list", ePage);
    }
}
