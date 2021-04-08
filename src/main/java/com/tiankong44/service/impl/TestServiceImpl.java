package com.tiankong44.service.impl;

import cn.hutool.core.date.DateUtil;
import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.TestMapper;
import com.tiankong44.model.Student;
import com.tiankong44.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author zhanghao_SMEICS
 * @Date 2020/11/23  15:08
 **/
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;

    @Override
    public BaseRes test() {
        BaseRes res = new BaseRes();
        Map<String, Object> map = new HashMap();
        List<Student> list = new ArrayList();
        Student s1 = new Student();
        s1.setId(1);
        s1.setAge(15);

        Student s2 = new Student();
        s2.setId(2);
        s2.setAge(16);


        Student s3 = new Student();
        s3.setId(3);
        s3.setAge(17);

        Student s4 = new Student();
        s4.setId(4);
        s4.setAge(18);

        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);
        map.put("updateTime", DateUtil.formatDateTime(new Date()));
        map.put("list", list);
        System.out.println(list);
        boolean flag = testMapper.test(map);

        Map<String, String> resMap = new HashMap<>();
        resMap = testMapper.test4(map);
        res.setData(resMap);
        if (flag) {
            res.setCode(0);
            res.setDesc("批量修改成功");
        } else {
            res.setCode(1);
            res.setDesc("失败");
        }
        return res;
    }

    @Override
    public BaseRes inset() {
        BaseRes res = new BaseRes();
        Student s1 = new Student();
        s1.setName("tiankong");
        s1.setAge(15);
        testMapper.insrt(s1);
        return res;
    }


    public static void main(String[] args) {
        System.out.println(DateUtil.formatDateTime(new Date()));
    }
}
