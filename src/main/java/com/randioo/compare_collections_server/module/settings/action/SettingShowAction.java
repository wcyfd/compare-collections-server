package com.randioo.compare_collections_server.module.settings.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.protobuf.GeneratedMessage;
import com.randioo.compare_collections_server.entity.bo.Role;
import com.randioo.compare_collections_server.module.settings.service.SettingService;
import com.randioo.compare_collections_server.protocol.Settings.SettingsShowRequest;
import com.randioo.randioo_server_base.annotation.PTAnnotation;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.template.IActionSupport;
import com.randioo.randioo_server_base.utils.SessionUtils;

@Controller
@PTAnnotation(SettingsShowRequest.class)
public class SettingShowAction implements IActionSupport {

    @Autowired
    private SettingService settingService;

    @Override
    public void execute(Object data, Object session) {
        SettingsShowRequest request = (SettingsShowRequest) data;
        Role role =  RoleCache.getRoleBySession(session);
        GeneratedMessage sc = settingService.getSettings(role);
        SessionUtils.sc(session, sc);
    }

}
