package io.renren.modules.sys.controller;

import io.renren.common.utils.R;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.form.SysLoginForm;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.sys.service.SysUserTokenService;
import io.renren.modules.sys.service.impl.SysUserServiceImpl;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author wtiaw
 * @date 2021/1/13 22:34
 */
@RestController
public class SysRegisterController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;


    /**
     * 登录
     */
    @PostMapping("/sys/register")
    public Map<String, Object> register(@RequestBody SysLoginForm form)throws IOException {
        System.out.println(form.getUsername()+form.getPassword());
        //用户信息
        SysUserEntity user = sysUserService.queryByUserName(form.getUsername());

        //账号不存在、密码错误
        if(user != null) {
            return R.error("账号已存在");
        }
        SysUserEntity user1 = new SysUserEntity();

        user1.setUsername(form.getUsername());
        user1.setPassword(form.getPassword());
        sysUserService.saveUser(user1);

        user1 = sysUserService.queryByUserName(form.getUsername());

        //生成token，并保存到数据库
        R r = sysUserTokenService.createToken(user1.getUserId());
        return r;
    }


}
