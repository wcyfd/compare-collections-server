package com.randioo.compare_collections_server.module.fight.component.manager;

import com.randioo.compare_collections_server.cache.local.GameCache;
import com.randioo.compare_collections_server.entity.bo.Role;
import com.randioo.compare_collections_server.entity.po.Game;
import com.randioo.compare_collections_server.entity.po.RoleGameInfo;
import com.randioo.compare_collections_server.module.match.service.MatchService;
import com.randioo.compare_collections_server.protocol.Entity.GameType;
import com.randioo.randioo_server_base.cache.RoleCache;
import com.randioo.randioo_server_base.config.GlobleClass;
import com.randioo.randioo_server_base.module.key.Key;
import com.randioo.randioo_server_base.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameManager {
    @Autowired
    private MatchService matchService;

    @Autowired
    private AudienceManager audienceManager;

    /**
     * 游戏获取器
     *
     * @param gameId
     * @return
     * @author wcy 2017年9月22日
     */
    public Game get(int gameId) {
        return GameCache.getGameMap().get(gameId);
    }

    public boolean isGoldMode(Game game) {
        return game.getGameType() == GameType.GAME_TYPE_GOLD;
    }

    public RoleGameInfo remove(Game game, String gameRoleId) {
        game.getRoleIdList().remove(gameRoleId);
        RoleGameInfo roleGameInfo = game.getRoleIdMap().remove(gameRoleId);
        game.getSeatMap().remove(roleGameInfo.seat);

        Role role = (Role) RoleCache.getRoleById(roleGameInfo.roleId);
        role.setGameId(0);
        return roleGameInfo;
    }

    public void destroyGame(Game game) {
        for (RoleGameInfo roleGameInfo : game.getRoleIdMap().values()) {
            Role role = (Role) RoleCache.getRoleById(roleGameInfo.roleId);
            if (role != null) {
                role.setGameId(0);
            }
        }
        Key key = game.getLockKey();
        if (key != null) {
            String lockString = matchService.getLockString(key);
            GameCache.getGameLockStringMap().remove(lockString);
            key.recycle();
        }
        GameCache.getGameMap().remove(game.getGameId());
        // 移除金币模式的房间号存储
        GameCache.getGoldModeGameIdList().remove(Integer.valueOf(game.getGameId()));
        // 移除观众
        int audienceSize = audienceManager.getAudiences(game.getGameId()).size();
        audienceManager.extractCount(game.getGameId(), audienceSize);
    }

    /**
     * 记录倒计时
     *
     * @param game
     */
    public void recordCountdown(Game game) {
        game.countdown = GlobleClass._G.wait_time + TimeUtils.getNowTime();
    }

    public int getCountdown(Game game) {
        return game.countdown == 0 ? -1 : TimeUtils.getNowTime() - game.countdown;
    }

    /**
     * 当前游戏有几个人
     *
     * @param game
     * @return
     */
    public int roleCount(Game game) {
        return game.getRoleIdMap().size();
    }

}
