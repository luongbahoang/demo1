package com.example.demo.security.userPrinciple;

import com.example.demo.Repository.IUserRepository;
import com.example.demo.model.User;
import com.example.demo.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
//===========================================================================================
//UserDetailsService interface có một phương thức để lấy thông tin người dùng bằng username

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    UserService userService;
    @Override
    @Transactional

    //tìm đối tượng username trong database
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("User not found -> username or password"+username));
        // gán UserPrinciple = user được tìm thấy trong database
//===========================================================================================
//và trả về một đối tượng UserDetails mà Spring Security có thể sử dụng để xác thực và xác nhận.
//UserDetails chứa các thông tin cần thiết
// (như: username, password, authorities - quyền hạn) để xây dựng một đối tượng Authentication.
        return UserPrinciple.build(user);
    }

    //HAM LAY RA USER HIEN TAI DE THUC HIEN THAO TAC VOI DB
    public User getCurrentUser(){
        Optional<User> user;
        String userName;
        //Lay 1 object principal trong SecurityContexHolder
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //So sanh obj voi Userdetails neu ma dung thi gan userName = principal.getUsername();
        if(principal instanceof UserDetails){
            userName = ((UserDetails) principal).getUsername();
        } else {
            //neu khong phai user hien tai thi userName = principal.toString();
            userName = principal.toString();
        }
        //kiem tra neu userName ton tai trong DB thi gan user = ham tim kiem trong DB theo userName do
        if(userRepository.existsByUsername(userName)){

            user = userService.findByUserName(userName);
        } else {
            //Neu chua ton tai thi tra ve 1 the hien cua lop User thong qua Optional.of
            user = Optional.of(new User());
            //set cho no 1 cai ten user an danh Day la truong hop ma tuong tac qua dang nhap kieu FB hay GG
            user.get().setUsername("Anonymous");
        }
        return user.get();
    }
}
