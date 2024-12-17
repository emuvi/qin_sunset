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
        this.init_serves();
    }

    private void init_serves() throws Exception {
        this.server_auth();
        if (this.wayToRun.airCfg.setup.servesPub) {
            this.serves_pub();
        }
        if (this.wayToRun.airCfg.setup.servesApp) {
            this.serves_app();
        }
        if (this.wayToRun.airCfg.setup.servesDir) {
            this.serves_dir();
        }
        if (this.wayToRun.airCfg.setup.servesCmd) {
            this.serves_cmd();
        }
        if (this.wayToRun.airCfg.setup.servesBas) {
            this.serves_bas();
        }
        if (this.wayToRun.airCfg.setup.servesBas && this.wayToRun.airCfg.setup.servesReg) {
            this.serves_reg();
        }
        if (this.wayToRun.airCfg.setup.servesGiz) {
            this.serves_giz();
        }
        this.server_utils();
    }

    private void server_auth() {
        logger.info("Serving Auth...");
        ServerAuth.init(this.context);
    }

    private void serves_pub() throws Exception {
        logger.info("Serving Pub...");
        var holder = new ServletHolder(new ServesPUB());
        var pubDir = new File("pub");
        if (!pubDir.exists()) {
            Files.createDirectories(pubDir.toPath());
        }
        holder.setInitParameter("basePath", pubDir.getAbsolutePath());
        this.context.addServlet(holder, "/pub/*");
    }

    private void serves_app() {
        logger.info("Serving App...");
        ServesAPP.init(this.context);
    }

    private void serves_dir() {
        logger.info("Serving Dir...");
        ServesDIR.init(this.context);
    }

    private void serves_cmd() {
        logger.info("Serving Cmd...");
        ServesCMD.init(this.context);
    }

    private void serves_bas() {
        logger.info("Serving Bas...");
        ServesBAS.init(this.context);
    }

    private void serves_reg() {
        logger.info("Serving Reg...");
        ServesREG.init(this.context);
    }

    private void serves_giz() {
        logger.info("Serving Giz...");
        ServesGIZ.init(this.context);
    }

    private void server_utils() {
        logger.info("Serving Utils...");
        ServerUtils.init(this.context, this.wayToRun.airCfg.setup);
    }

    public void start() throws Exception {
        logger.info("Starting Server...");
        logger.info("Setup On AirCfg: " + this.wayToRun.airCfg.setup);
        logger.info("Users On AirCfg: " + this.wayToRun.airCfg.users);
        logger.info("Bases On AirCfg: " + this.wayToRun.airCfg.bases);
        this.server.start();
        this.server.join();
    }

}
