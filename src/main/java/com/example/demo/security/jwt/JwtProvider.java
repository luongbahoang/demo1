package com.example.demo.security.jwt;

import com.example.demo.security.userPrinciple.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private static final Logger logger= LoggerFactory.getLogger(JwtProvider.class);
    private String jwtSecret="luongbahoang";
    private int jwtExpiration= 86400;
    //==========================================================================================================
    //tạo token
    // được gọi qua lớp log in
    public String createToken(Authentication authentication){

        UserPrinciple userPrinciple= (UserPrinciple) authentication.getPrincipal();
        System.out.println("========================================================createToken");
        return Jwts.builder()
                .setSubject(userPrinciple.getUsername()) // set username vào trong token
                .setIssuedAt(new Date())                  // set thời điểm khởi tạo token
                .setExpiration(new Date(new Date().getTime()+jwtExpiration* 1000))// set thời gian token có hiệu lực
                .signWith(SignatureAlgorithm.HS512,jwtSecret) // set chữ kí bí mật
                .compact();                                  // kết thúc việc khởi tạo
    }

    //==========================================================================================================
    // kiểm tra token có hợp lệ hay ko
    // lớp jwtTokenFilter sẽ gọi hàm này
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }
        catch (ExpiredJwtException e){
            logger.error("Expired JWT token-> Message()"+e);
        }
        catch (UnsupportedJwtException e){
            logger.error("Unsupported JWT signature-> Message()"+e);
        }
        catch (MalformedJwtException e){
            logger.error("Invalid format JWT token-> Message()"+e);
        }
        catch (IllegalArgumentException e){
            logger.error(" JWT claim is empty-> Message()"+e);
        }
        catch (SignatureException e){
            logger.error(" Invalid signature-> Message()"+e);
        }
        return false;
    }

    //==========================================================================================================
    //lấy username được gửi về từ frontend kèm với token
    // lớp jwtTokenFilter sẽ gọi hàm này
    public String getUserNameFromToken(String token){
        String username = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        return username;

    }
}
