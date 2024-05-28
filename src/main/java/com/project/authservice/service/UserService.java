package com.project.authservice.service;

import com.project.authservice.dto.request.UserResquest;
import com.project.authservice.dto.response.UserResponse;
import com.project.authservice.entity.User;
import com.project.authservice.enums.Role;
import com.project.authservice.exception.AppException;
import com.project.authservice.exception.ErrorCode;
import com.project.authservice.mapper.UserMapper;
import com.project.authservice.repository.RoleRepository;
import com.project.authservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserResquest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXIST);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles =  new HashSet<>();
        roles.add(Role.USER.name());

        //user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }
    @PreAuthorize("hasAnyAuthority('UPDATE_DATA')")
    public List<UserResponse> getUsers(){
        log.info("In method getUsers");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public List<UserResponse> getUsersPaging(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAllUserPaging(pageable).stream().map(userMapper::toUserResponse).toList();
    }
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String id){
        log.info("In method getUser by Id");
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST)));
    }

    public UserResponse updateUser(String userId, UserResquest request){
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        userMapper.updateUser(user,request);
        if(request.getPassword() != null){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if(request.getRoles() != null){
            var roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public Boolean deleteUser(String userId){
        userRepository.deleteById(userId);
        return true;
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User byUsername = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXIST));
        return userMapper.toUserResponse(byUsername);
    }

}
