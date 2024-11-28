package org.tragoit.service;

import org.tragoit.dto.UserDto;
import org.tragoit.model.User;

import java.util.List;

public interface IUserService {
    UserDto saveUser(UserDto userDto);

    List<UserDto> findAllUsers();

    UserDto findUserByEmail(String email);
}
