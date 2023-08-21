package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.service.EmailService;
import com.example.finalprojectbackend.lab2you.service.RoleService;
import com.example.finalprojectbackend.lab2you.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserManagementServiceProcessingController {
//    private final EmailService emailService;
//    private final UserService userService;
//    private final RoleService roleService;
//    private final UserDTOConverter  userDTOConverter;
//
//    @Autowired
//    public UserManagementServiceProcessingController(EmailService emailService, UserService userService, RoleService roleService) {
//        this.emailService = emailService;
//        this.userService = userService;
//        this.roleService = roleService;
//        userDTOConverter = new UserDTOConverter();
//    }
//
//    @PostMapping("/registerUserFromExternalRequest")
//    public ResponseEntity<String> registerUserFromAdminRequest(@RequestBody UserDTO userDTO) {
//        try {
//
//            return registrationUser(userDTO);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(Lab2YouConstants._LAB2YOU_REASON_CODE_UNEXPECTED_EXCEPTION);
//        }
//    }
//
//    @PostMapping("/registerUserFromInternalRequest")
//    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
//        try {
//
//            return registrationUser(userDTO);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(Lab2YouConstants._LAB2YOU_REASON_CODE_UNEXPECTED_EXCEPTION);
//        }
//    }
//
//    @PostMapping("/registerUserFromMedicalRequest")
//    public ResponseEntity<String> registerUserFromMedicalRequest(@RequestBody UserDTO userDTO) {
//        try {
//
//            Role role = roleService.findByName(Lab2YouConstants.lab2YouRoles.USER.getRole());
//            userDTO.setRoles(List.of(role));
//
//            return registrationUser(userDTO);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(Lab2YouConstants._LAB2YOU_REASON_CODE_UNEXPECTED_EXCEPTION);
//        }
//    }
//
//    private ResponseEntity<String> registrationUser(UserDTO userDTO) {
//        UserEntity newUser = null;
//        Role role = null;
//
//        newUser = userService.findByCui(userDTO.getCui());
//
//        if (newUser != null) {
//            return ResponseEntity.badRequest().body(Lab2YouConstants.lab2YouErrorCodes.USER_ALREADY_EXISTS.getDescription());
//        }
//
//        newUser = userDTOConverter.convertToEntity(userDTO);
//        role = roleService.findByName(userDTO.getRoles().get(0).getName());
//        newUser.addRole(role);
//
//        if (!fullyValidatedForRegistration(newUser)) {
//            return ResponseEntity.badRequest().body(Lab2YouConstants.lab2YouErrorCodes.INVALID_DATA.getDescription() +
//                    Lab2YouConstants.lab2YouErrorCodes.EMAIL_NOT_SENT.getDescription());
//        }
//
//        userService.save(newUser);
//        emailService.sendRegistrationEmail(userDTO);
//        return ResponseEntity.ok().body(Lab2YouConstants.lab2YouSuccessCodes.EMAIL_SENT.getDescription());
//    }
//
//
//    @GetMapping("/userList")
//    public ResponseEntity<List<UserWrapper>> getUsers() {
//        return new ResponseEntity<>(userService.executedReadAll(), HttpStatus.OK);
//    }
//
//    private boolean fullyValidatedForRegistration(UserEntity user) {
//        String email = user.getEmail();
//        String nit = user.getNit();
//        String cui = user.getCui().toString();
//
//        boolean isEmailValid = Lab2YouUtils.verifyEmailFormat(email);
//        boolean isNitValid = Lab2YouUtils.validateNit(nit);
//        boolean isCuiValid = Lab2YouUtils.validateCUI(cui);
//
//        return isEmailValid && isNitValid && isCuiValid;
//
//    }
}
