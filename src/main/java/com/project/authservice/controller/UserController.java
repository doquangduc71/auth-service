package com.project.authservice.controller;

import com.project.authservice.dto.request.ApiResponse;
import com.project.authservice.dto.request.UserResquest;
import com.project.authservice.dto.response.UserResponse;
import com.project.authservice.entity.User;
import com.project.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserResquest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers(){
        var authentication =  SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}",authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/paging")
    ApiResponse<List<UserResponse>> getUsersPaging(@RequestParam int page, @RequestParam int size){
        return ApiResponse.<List<UserResponse>>builder()
               .result(userService.getUsersPaging(page,size))
               .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId){
        return ApiResponse.<UserResponse>builder().result(userService.getUser(userId)).build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder().result(userService.getMyInfo()).build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse>  updateUser(@PathVariable("userId") String userId,
                    @RequestBody @Valid UserResquest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<Boolean> deleteUser(@PathVariable("userId") String userId){
        return ApiResponse.<Boolean>builder()
                .result(userService.deleteUser(userId))
                .build();
    }



}
