package com.furp.service.impl;

import cn.dev33.satoken.model.wrapperInfo.SaDisableWrapperInfo;
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
        List<String> roles = new ArrayList<>();

        SaSession session = StpUtil.getSessionByLoginId(loginId);
        String role = session.getString("role");

        if (role != null) {
            roles.add(role);
        }

        return roles;
    }

}
