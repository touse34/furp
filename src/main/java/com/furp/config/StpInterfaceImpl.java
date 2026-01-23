package com.furp.config;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;

import java.util.ArrayList;
import java.util.List;

public class StpInterfaceImpl implements StpInterface {
    @Override
    public List<String> getPermissionList(Object o, String s) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getRoleList(Object loginId, String s) {

        SaSession session = StpUtil.getSessionByLoginId(loginId);

        String role = session.getString("role");

        return role != null ? List.of(role) : new ArrayList<>();
    }
}
