package com.vidlus.qin_sunset;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vidlus.qin_sunset.core.WayToRun;
import com.vidlus.qin_sunset.hook.ServesApp;
import com.vidlus.qin_sunset.hook.ServesBas;
import com.vidlus.qin_sunset.hook.ServesCmd;
import com.vidlus.qin_sunset.hook.ServesDir;
import com.vidlus.qin_sunset.hook.ServesGiz;
import com.vidlus.qin_sunset.hook.ServesPub;
import com.vidlus.qin_sunset.hook.ServesReg;
import com.vidlus.qin_sunset.hook.ServesUtl;
import com.vidlus.qin_sunset.hook.ServesWay;

public class Service {

    public static final String KEY_QIN_SUNSET_SERVICE = "QinSunset.Service";
    public static final String KEY_QIN_SUNSET_WAY_TO_RUN = "QinSunset.WayToRun";

    private static Logger log = LoggerFactory.getLogger(Service.class);

    private final WayToRun wayToRun;
    private final QueuedThreadPool threadPool;
    private final Server server;
    private final HttpConfiguration httpConfig;
    private final HttpConnectionFactory httpFactory;
    private final ServerConnector connector;
    private final ServletContextHandler context;

    public Service(WayToRun wayToRun) throws Exception {
        this.wayToRun = wayToRun;
        this.threadPool = new QueuedThreadPool(this.wayToRun.airWays.setup.threadsMax,
                        this.wayToRun.airWays.setup.threadsMin,
                        this.wayToRun.airWays.setup.threadsIdleTimeout);
        this.server = new Server(this.threadPool);
        this.httpConfig = new HttpConfiguration();
        this.httpConfig.setSendDateHeader(false);
        this.httpConfig.setSendServerVersion(false);
        this.httpFactory = new HttpConnectionFactory(this.httpConfig);
        this.connector = new ServerConnector(this.server, httpFactory);
        connector.setHost(this.wayToRun.airWays.setup.serverHost);
        connector.setPort(this.wayToRun.airWays.setup.serverPort);
        this.server.setConnectors(new Connector[] {this.connector});
        this.context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        this.context.setContextPath("");
        this.server.setHandler(this.context);
        this.initServes();
        this.context.setAttribute(KEY_QIN_SUNSET_SERVICE, this);
        this.context.setAttribute(KEY_QIN_SUNSET_WAY_TO_RUN, this.wayToRun);
    }

    private void initServes() throws Exception {
        this.servesPub();
        this.servesApp();
        this.servesDir();
        this.servesCmd();
        this.servesBas();
        this.servesReg();
        this.servesGiz();
        this.servesUtl();
        this.servesWay();
    }

    private void servesPub() throws Exception {
        log.info("Initializing Serves Pub...");
        var holder = new ServletHolder(new ServesPub());
        this.context.addServlet(holder, "/pub/*");
    }

    private void servesApp() {
        log.info("Initializing Serves App...");
        ServesApp.init(this.context);
    }

    private void servesDir() {
        log.info("Initializing Serves Dir...");
        ServesDir.init(this.context);
    }

    private void servesCmd() {
        log.info("Initializing Serves Cmd...");
        ServesCmd.init(this.context);
    }

    private void servesBas() {
        log.info("Initializing Serves Bas...");
        ServesBas.init(this.context);
    }

    private void servesReg() {
        log.info("Initializing Serves Reg...");
        ServesReg.init(this.context);
    }

    private void servesGiz() {
        log.info("Initializing Serves Giz...");
        ServesGiz.init(this.context);
    }

    private void servesUtl() {
        log.info("Initializing Serves Utl...");
        ServesUtl.init(this.context, this.wayToRun.airWays.setup);
    }

    private void servesWay() {
        log.info("Initializing Serves Way...");
        ServesWay.init(this.context);
    }

    public void start() throws Exception {
        this.server.start();
        this.server.join();
    }

    public void stop() throws Exception {
        log.info("Stopping...");
        this.server.stop();
        this.server.destroy();
        log.info("Stopped.");
    }

}
