package com.example.demo.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserDetailsService userDetailsService;
    //==========================================================================================================
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{

            String token= getToken(request);
            //kiểm tra token có bị null hay không hợp lệ hay ko
            if(token!=null&& jwtProvider.validateToken(token))  {
                //lấy username từ token
            String username= jwtProvider.getUserNameFromToken(token);
                // gán UserDetails trong hệ thống = userdetails tìm được trong database
                UserDetails userDetails= userDetailsService.loadUserByUsername(username);
                //tạo đối tượng Authentication
                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities());
                //set user lên website
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              //  Lưu đối tượng Authentication vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

        }catch(Exception e){
            logger.error("cannot set user authentication -> message:{}",e);
        }

        filterChain.doFilter(request,response);
    }

    //==========================================================================================================
    // tìm token trong request
    public String getToken(HttpServletRequest request){
        String authHeader= request.getHeader("Authorization");
        if (authHeader!=null){

            return authHeader.replace("Bearer","");
        }
        return null;
    }
}
