package io.hawt.web.plugin.karaf.terminal.karaf4;

import java.io.PipedInputStream;
import java.io.PrintStream;

import io.hawt.web.plugin.karaf.terminal.KarafConsoleFactory;
import io.hawt.web.plugin.karaf.terminal.WebTerminal;
import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.console.Console;
import org.apache.karaf.shell.console.factory.ConsoleFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Karaf4ConsoleFactory implements KarafConsoleFactory {

    private final static Logger LOG = LoggerFactory.getLogger(Karaf4ConsoleFactory.class);

    private static final String KARAF4_CONSOLE_FACTORY = "org.apache.karaf.shell.console.ConsoleFactory";

    public static final int TERM_WIDTH = 120;
    public static final int TERM_HEIGHT = 400;

    public CommandSession getSession(Object console) {
        try {
            return (CommandSession) console.getClass().getMethod("getSession", null).invoke(console);
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public void close(Object console, boolean param) {
        try {
            console.getClass().getMethod("close", boolean.class).invoke(console, param);
        } catch (Exception e) {
            // ignore
        }
    }

    public Object createConsole(PipedInputStream in,
                                PrintStream pipedOut,
                                BundleContext bundleContext) throws Exception {


        LOG.debug("Using Karaf 4.x Console API");
        ServiceReference ref = bundleContext.getServiceReference(KARAF4_CONSOLE_FACTORY);
        if (ref != null) {
            ConsoleFactory factory = (ConsoleFactory) bundleContext.getService(ref);
            Console console = factory.create(in, pipedOut, pipedOut, new WebTerminal(TERM_WIDTH, TERM_HEIGHT), null, null);
            return console;
        }

        return null;
    }

}
