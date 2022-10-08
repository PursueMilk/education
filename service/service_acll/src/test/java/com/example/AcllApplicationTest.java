package com.example;

import com.example.AcllApplication;
import com.example.entity.Permission;
import com.example.mapper.PermissionMapper;
import com.example.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AcllApplicationTest {
    @Autowired
    private PermissionService permissionService;

//    @Autowired
//    private PermissionMapper permissionMapper;

    @Test
    public void test() {
        System.out.println(permissionService);
        List<String> list = permissionService.selectPermissionValueByUserId("1");
        System.out.println(list);
    }

}
