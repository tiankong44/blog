package com.tiankong44.dao;

import com.tiankong44.model.vo.UserInfoVo;
import com.tiankong44.model.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SystemMapper {
    UserInfoVo getUserInfo(Long userId);

    UserVo getSysUserInfo(Long userId);

    boolean updateAvatar(String path, String userId);

    boolean insertAvatarLog(String userId);
}
