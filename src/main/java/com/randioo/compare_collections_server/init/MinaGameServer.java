package com.randioo.compare_collections_server.init;

import com.randioo.compare_collections_server.GlobleConstant;
import com.randioo.compare_collections_server.quartz.JobListener;
import com.randioo.randioo_server_base.config.GlobleMap;
import com.randioo.randioo_server_base.init.GameServer;
import com.randioo.randioo_server_base.scheduler.SchedulerManager;
import com.randioo.randioo_server_base.service.ServiceManager;
import com.yiya.yiya_platform_sdk.YiyaPlatformSdk;
import org.apache.mina.core.service.IoAcceptor;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.quartz.impl.matchers.EverythingMatcher.allJobs;

public class MinaGameServer implements GameServer {
    private final static Logger logger = LoggerFactory.getLogger(MinaGameServer.class);

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private SchedulerManager schedulerManager;

    @Autowired
    private IoAcceptor ioAcceptor;

    @Autowired
    private YiyaPlatformSdk yiyaPlatformSdk;

    @Autowired
    private Scheduler quartzScheduler;

    public void start() {
        yiyaPlatformSdk.setActiveProjectName(GlobleMap.String(GlobleConstant.ARGS_PLATFORM_PACKAGE_NAME));
        logger.info("init yiyaPlatformSdk");

        serviceManager.initServices();
        logger.info("init Services");

        schedulerManager.start();
        logger.info("scheduler start");

        try {
            quartzScheduler.getListenerManager().addJobListener(new JobListener(), allJobs());
            quartzScheduler.start();
            logger.info("quartzScheduler start");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        InetSocketAddress inetSocketAddress = new InetSocketAddress(GlobleMap.Int(GlobleConstant.ARGS_PORT));
        try {
            ioAcceptor.bind(inetSocketAddress);
            logger.info("WANSERVER : START SERVER SUCCESS -> socket port:{}", inetSocketAddress.getPort());
        } catch (IOException e) {
            logger.error("", e);
        }
        logger.info("socket start");

        GlobleMap.putParam(GlobleConstant.ARGS_LOGIN, true);
    }

}
