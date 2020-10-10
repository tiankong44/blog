package com.tiankong44.dao;

import com.tiankong44.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    User findUser(String username, String password);
}
