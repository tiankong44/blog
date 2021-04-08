package com.tiankong44.dao;

import com.tiankong44.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author zhanghao_SMEICS
 * @Date 2020/11/23  15:09
 **/

@Repository
@Mapper
public interface TestMapper {

    public boolean test(Map<String, Object> paramMap);

    public boolean test2(List<Student> list);

    public boolean test3(Map<String, Object> paramMap);

    void insrt(Student student);

    Map<String, String> test4(Map<String, Object> map);
}
