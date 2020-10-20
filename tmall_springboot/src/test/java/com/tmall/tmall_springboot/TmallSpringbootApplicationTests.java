package com.tmall.tmall_springboot;

import com.tmall.pojo.User;
import com.tmall.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TmallSpringbootApplicationTests {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityManager securityManager;


    @Test
	public void testRegister() {
        String username = "hello";
        String password = "123";
        User user = new User();
        user.setName(username);

        //encode
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";
        String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);

        //insert into db
        userService.add(user);
	}

    @Test
    public void testLogin() {
        String name =  "hello";
        String password = "1234";
        SecurityUtils.setSecurityManager(securityManager);


        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        try {
            //login verify
            subject.login(token);
            System.out.println("login success!");
        }catch (AuthenticationException e){
            System.out.println("login faild!");
        }
	}

}
