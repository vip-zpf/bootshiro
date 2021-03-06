package com.usthe.bootshiro.shiro.matcher;

import com.alibaba.fastjson.JSON;
import com.usthe.bootshiro.domain.vo.JwtAccount;
import com.usthe.bootshiro.domain.vo.Message;
import com.usthe.bootshiro.service.AccountService;
import com.usthe.bootshiro.util.JsonWebTokenUtil;
import com.usthe.bootshiro.util.RequestResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/* *
 * @Author tomsun28
 * @Description 
 * @Date 18:01 2018/3/3
 */
@Component
public class JwtMatcher implements CredentialsMatcher {


    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {

        String jwt = (String) authenticationInfo.getCredentials();
        String appId = (String) authenticationToken.getPrincipal();
        JwtAccount jwtAccount = null;
        try{
            jwtAccount = JsonWebTokenUtil.parseJwt(jwt,JsonWebTokenUtil.SECRET_KEY);
        } catch(SignatureException e){
            throw new AuthenticationException("errJwt"); // 令牌错误
        } catch(ExpiredJwtException e){

            throw new AuthenticationException("expiredJwt"); // 令牌过期
        } catch(Exception e){
            throw new AuthenticationException("errJwt");
        }
        if(null == jwtAccount){
            throw new AuthenticationException("errJwt");
        }

        return true;
    }
}
