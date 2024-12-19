package br.com.pointel.qin_sunset;

import java.io.File;
import java.nio.file.Files;
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
import br.com.pointel.qin_sunset.core.WayToRun;
import br.com.pointel.qin_sunset.hook.ServerAuth;
import br.com.pointel.qin_sunset.hook.ServerUtils;
import br.com.pointel.qin_sunset.hook.ServesAPP;
import br.com.pointel.qin_sunset.hook.ServesBAS;
import br.com.pointel.qin_sunset.hook.ServesCMD;
import br.com.pointel.qin_sunset.hook.ServesDIR;
import br.com.pointel.qin_sunset.hook.ServesGIZ;
import br.com.pointel.qin_sunset.hook.ServesPUB;
import br.com.pointel.qin_sunset.hook.ServesREG;

public class Service {

    private Logger logger = LoggerFactory.getLogger(Service.class);

    private final WayToRun wayToRun;
    private final QueuedThreadPool threadPool;
    private final Server server;
    private final HttpConfiguration httpConfig;
    private final HttpConnectionFactory httpFactory;
    private final ServerConnector connector;
    private final ServletContextHandler context;

    public Service(WayToRun wayToRun) throws Exception {
        this.wayToRun = wayToRun;
        this.threadPool = new QueuedThreadPool(this.wayToRun.airCfg.setup.threadsMax,
                        this.wayToRun.airCfg.setup.threadsMin,
                        this.wayToRun.airCfg.setup.threadsIdleTimeout);
        this.server = new Server(this.threadPool);
        this.httpConfig = new HttpConfiguration();
        this.httpConfig.setSendDateHeader(false);
        this.httpConfig.setSendServerVersion(false);
        this.httpFactory = new HttpConnectionFactory(this.httpConfig);
        this.connector = new ServerConnector(this.server, httpFactory);
        connector.setHost(this.wayToRun.airCfg.setup.serverHost);
        connector.setPort(this.wayToRun.airCfg.setup.serverPort);
        this.server.setConnectors(new Connector[] {this.connector});
        this.context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        this.context.setContextPath("");
        this.context.setAttribute("QinSunset.Way", this.wayToRun);
        this.server.setHandler(this.context);
        this.initServes();
    }

    private void initServes() throws Exception {
        this.serverAuth();
        if (Boolean.TRUE.equals(this.wayToRun.airCfg.setup.servesPub)) {
            this.servesPub();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airCfg.setup.servesApp)) {
            this.servesApp();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airCfg.setup.servesDir)) {
            this.servesDir();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airCfg.setup.servesCmd)) {
            this.servesCmd();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airCfg.setup.servesBas)) {
            this.servesBas();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airCfg.setup.servesBas)
                        && Boolean.TRUE.equals(this.wayToRun.airCfg.setup.servesReg)) {
            this.servesReg();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airCfg.setup.servesGiz)) {
            this.servesGiz();
        }
        this.serverUtils();
    }

    private void serverAuth() {
        logger.info("Serving Auth...");
        ServerAuth.init(this.context);
    }

    private void servesPub() throws Exception {
        logger.info("Serving Pub...");
        var holder = new ServletHolder(new ServesPUB());
        var pubDir = new File("pub");
        if (!pubDir.exists()) {
            Files.createDirectories(pubDir.toPath());
        }
        holder.setInitParameter("basePath", pubDir.getAbsolutePath());
        this.context.addServlet(holder, "/pub/*");
    }

    private void servesApp() {
        logger.info("Serving App...");
        ServesAPP.init(this.context);
    }

    private void servesDir() {
        logger.info("Serving Dir...");
        ServesDIR.init(this.context);
    }

    private void servesCmd() {
        logger.info("Serving Cmd...");
        ServesCMD.init(this.context);
    }

    private void servesBas() {
        logger.info("Serving Bas...");
        ServesBAS.init(this.context);
    }

    private void servesReg() {
        logger.info("Serving Reg...");
        ServesREG.init(this.context);
    }

    private void servesGiz() {
        logger.info("Serving Giz...");
        ServesGIZ.init(this.context);
    }

    private void serverUtils() {
        logger.info("Serving Utils...");
        ServerUtils.init(this.context, this.wayToRun.airCfg.setup);
    }

    public void start() throws Exception {
        logger.info("Starting Server...");
        logger.info("AirCfg Setup: {}", this.wayToRun.airCfg.setup);
        logger.info("AirCfg Bases: {}", this.wayToRun.airCfg.bases);
        logger.info("AirCfg Users: {}", this.wayToRun.airCfg.users);
        logger.info("AirCfg Groups: {}", this.wayToRun.airCfg.groups);
        this.server.start();
        this.server.join();
    }

}
