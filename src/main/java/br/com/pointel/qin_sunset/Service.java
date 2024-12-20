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
import br.com.pointel.qin_sunset.hook.ServerUtils;
import br.com.pointel.qin_sunset.hook.ServesApp;
import br.com.pointel.qin_sunset.hook.ServesBas;
import br.com.pointel.qin_sunset.hook.ServesCmd;
import br.com.pointel.qin_sunset.hook.ServesDir;
import br.com.pointel.qin_sunset.hook.ServesGiz;
import br.com.pointel.qin_sunset.hook.ServesPub;
import br.com.pointel.qin_sunset.hook.ServesReg;

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
        this.context.setAttribute("QinSunset.Way", this.wayToRun);
        this.server.setHandler(this.context);
        this.initServes();
    }

    private void initServes() throws Exception {
        this.serverUtils();
        if (Boolean.TRUE.equals(this.wayToRun.airWays.setup.servesPub)) {
            this.servesPub();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airWays.setup.servesApp)) {
            this.servesApp();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airWays.setup.servesDir)) {
            this.servesDir();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airWays.setup.servesCmd)) {
            this.servesCmd();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airWays.setup.servesBas)) {
            this.servesBas();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airWays.setup.servesBas)
                        && Boolean.TRUE.equals(this.wayToRun.airWays.setup.servesReg)) {
            this.servesReg();
        }
        if (Boolean.TRUE.equals(this.wayToRun.airWays.setup.servesGiz)) {
            this.servesGiz();
        }
    }

    private void servesPub() throws Exception {
        logger.info("Serving Pub...");
        var holder = new ServletHolder(new ServesPub());
        var pubDir = new File("pub");
        if (!pubDir.exists()) {
            Files.createDirectories(pubDir.toPath());
        }
        holder.setInitParameter("basePath", pubDir.getAbsolutePath());
        this.context.addServlet(holder, "/pub/*");
    }

    private void servesApp() {
        logger.info("Serving App...");
        ServesApp.init(this.context);
    }

    private void servesDir() {
        logger.info("Serving Dir...");
        ServesDir.init(this.context);
    }

    private void servesCmd() {
        logger.info("Serving Cmd...");
        ServesCmd.init(this.context);
    }

    private void servesBas() {
        logger.info("Serving Bas...");
        ServesBas.init(this.context);
    }

    private void servesReg() {
        logger.info("Serving Reg...");
        ServesReg.init(this.context);
    }

    private void servesGiz() {
        logger.info("Serving Giz...");
        ServesGiz.init(this.context);
    }

    private void serverUtils() {
        logger.info("Serving Utils...");
        ServerUtils.init(this.context, this.wayToRun.airWays.setup);
    }

    public void start() throws Exception {
        logger.info("Starting Server...");
        logger.info("Server AirWays Setup: {}", this.wayToRun.airWays.setup);
        this.server.start();
        this.server.join();
    }

}
