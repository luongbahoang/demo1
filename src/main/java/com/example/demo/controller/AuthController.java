package com.example.demo.controller;

import com.example.demo.dto.request_dulieunhanvao.ChangeAvatar;
import com.example.demo.dto.request_dulieunhanvao.SignInForm;
import com.example.demo.dto.request_dulieunhanvao.SignUpForm;
import com.example.demo.dto.respond_dulieutrave.RespondMess;
import com.example.demo.dto.respond_dulieutrave.JwtRespond;
import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.model.User;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.jwt.JwtTokenFilter;
import com.example.demo.security.userPrinciple.UserPrinciple;
import com.example.demo.service.impl.RoleService;
import com.example.demo.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController  {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtTokenFilter jwtTokenFilter;
    @PostMapping("/signUp")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm){
        //==============================================================================================================
        // kiểm tra username và email đã tồn tại trong database hay chưa
        if(userService.existsByUserName(signUpForm.getUsername())){
            return new ResponseEntity<>(new RespondMess("nouser"), HttpStatus.OK);
        }
        if (userService.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new RespondMess("noemail"), HttpStatus.OK);
        }
        if(signUpForm.getAvatar() == null || signUpForm.getAvatar().trim().isEmpty()){
            signUpForm.setAvatar("https://firebasestorage.googleapis.com/v0/b/chinhbeo-18d3b.appspot.com/o/avatar.png?alt=media&token=3511cf81-8df2-4483-82a8-17becfd03211");
        }
           User user=new User(signUpForm.getName(),signUpForm.getUsername(),
                              signUpForm.getEmail(),passwordEncoder.encode(signUpForm.getPassword()),signUpForm.getAvatar() );
        //==============================================================================================================
            // kiểm tra giá trị của role và set vào đối tượng user
        Set<String> strRoles = signUpForm.getRoles();
            Set<Role> roles= new HashSet<>();
            strRoles.forEach(role->{
                    switch (role){
                    case "admin":
                        Role adminRole= roleService.findByName(RoleName.ADMIN).orElseThrow(()->new RuntimeException("Role not found"));
                       roles.add(adminRole);
                        break;
                    case "pm":
                        Role pmRole= roleService.findByName(RoleName.PM).orElseThrow(()->new RuntimeException("Role not found"));
                        roles.add(pmRole);
                        break;
                    default :
                        Role userRole= roleService.findByName(RoleName.USER).orElseThrow(()->new RuntimeException("Role not found"));
                        roles.add(userRole);
                    }});
            user.setRoles(roles);
        //==============================================================================================================
           // lưu vào database
            userService.save(user);
            return new ResponseEntity<>(new RespondMess("yes"),HttpStatus.OK);
    }
    //==============================================================================================================
// update avatar
    @PutMapping("/change-avatar")
    public ResponseEntity<?> changeAvatar(HttpServletRequest request, @Valid @RequestBody ChangeAvatar changeAvatar){
        //kiểm tra token trong request
        String jwt = jwtTokenFilter.getToken(request);
        // lấy username từ token
        String username = jwtProvider.getUserNameFromToken(jwt);

        User user;
        try {
            if(changeAvatar.getAvatar()==null){
                return new ResponseEntity<>(new RespondMess("no"), HttpStatus.OK);
            } else {
                user = userService.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("User Not Found -> username"+username));
                user.setAvatar(changeAvatar.getAvatar());
                userService.save(user);
            }
            return new ResponseEntity<>(new RespondMess("yes"), HttpStatus.OK);
        } catch (UsernameNotFoundException exception){
            return new ResponseEntity<>(new RespondMess(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> logIn(@Valid @RequestBody SignInForm signInForm){

        //Xác thực người dùng {username, pasword }
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(),signInForm.getPassword()));

       // Lưu đối tượng Authentication vào SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token= jwtProvider.createToken(authentication);

        UserPrinciple userPrinciple= (UserPrinciple) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtRespond(token,userPrinciple.getName(),userPrinciple.getAuthorities(),userPrinciple.getAvatar()));
    }

}
