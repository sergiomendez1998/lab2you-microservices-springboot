package com.example.finalprojectbackend.lab2you.service;

import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudServiceProcessingController;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.UserWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.UserRepository;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService extends CrudServiceProcessingController<UserEntity> {
    private final UserRepository userRepository;
    private ResponseWrapper responseWrapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @ReadOnlyProperty
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public ResponseWrapper executeCreation(UserEntity entity) {
       throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResponseWrapper executeUpdate(UserEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<UserEntity> userEntityFound = userRepository.findById(entity.getId());
        userEntityFound.ifPresent(userEntity -> userEntity.setEmail(entity.getEmail()));
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("user updated successfully");
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeDeleteById(UserEntity entity) {
        Optional<UserEntity> userEntityFound = userRepository.findById(entity.getId());

        if (userEntityFound.isPresent()) {
            userEntityFound.get().setEnabled(false);
            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("user deleted successfully");
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("user not found");
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeReadAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected ResponseWrapper validateForCreation(UserEntity entity) {
        responseWrapper = new ResponseWrapper();

        if (!Lab2YouUtils.verifyEmailFormat(entity.getEmail())) {
            responseWrapper.addError("email","el formato del correo es invalido");
        }

        if (entity.getEmail() == null) {
            responseWrapper.addError("email","email es requiredo");
        }

        if (entity.getPassword() == null) {
            responseWrapper.addError("password","password es requiredo");
        }

        if (entity.getRole() == null) {
            responseWrapper.addError("role","role es requiredo");
        }

       UserEntity  userEntityFound = userRepository.findByEmail(entity.getEmail());
        if (userEntityFound != null) {
           responseWrapper.addError("email","el correo ya existe");
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForUpdate(UserEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getEmail() == null) {
            responseWrapper.addError("email","email es requiredo");
        }

        if (!Lab2YouUtils.verifyEmailFormat(entity.getEmail())) {
            responseWrapper.addError("email","formato del email no es valido");
        }

        UserEntity  userEntityFound = userRepository.findByEmail(entity.getEmail());
        if (userEntityFound != null) {
            if (!userEntityFound.getId().equals(entity.getId())) {
                responseWrapper.addError("email","el correo ya existe");
            }
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForDelete(UserEntity entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected ResponseWrapper validateForRead(UserEntity entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
