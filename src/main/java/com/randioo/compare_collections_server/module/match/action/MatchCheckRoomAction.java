package com.randioo.compare_collections_server.module.match.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.randioo.compare_collections_server.module.match.service.MatchService;
import com.randioo.compare_collections_server.protocol.Match.MatchCheckRoomRequest;
import com.randioo.randioo_server_base.annotation.PTAnnotation;
import com.randioo.randioo_server_base.template.IActionSupport;

@Controller
@PTAnnotation(MatchCheckRoomRequest.class)
public class MatchCheckRoomAction implements IActionSupport {
    @Autowired
    private MatchService matchService;

    @Override
    public void execute(Object data, Object session) {
        // TODO Auto-generated method stub
        MatchCheckRoomRequest request = (MatchCheckRoomRequest) data;

        
		matchService.checkRoom(request.getRoomId(), session);
    }

}
