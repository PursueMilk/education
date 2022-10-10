package com.example.service;

import java.util.Map;

public interface MsmService {
    boolean send(String code, String phone);

    boolean sendMail(String code, String mail);
}
