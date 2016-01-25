package org.http.chain.util;

	
import org.http.chain.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionLog {
    /**
     * HttpSession attribute key: prefix string
     */
    public static final String PREFIX = SessionLog.class.getName() + ".prefix";

    /**
     * HttpSession attribute key: {@link Logger}
     */
    public static final String LOGGER = SessionLog.class.getName() + ".logger";

    private static Class<?> getClass(HttpSession session) {
        return session.getHandler().getClass();
    }

    public static void debug(HttpSession session, String message) {
        Logger log = getLogger(session);
        if (log.isDebugEnabled()) {
            log.debug(String.valueOf(session.getAttribute(PREFIX)) + message);
        }
    }

    public static void debug(HttpSession session, String message, Throwable cause) {
        Logger log = getLogger(session);
        if (log.isDebugEnabled()) {
            log.debug(String.valueOf(session.getAttribute(PREFIX)) + message,
                    cause);
        }
    }

    public static void info(HttpSession session, String message) {
        Logger log = getLogger(session);
        if (log.isInfoEnabled()) {
            log.info(String.valueOf(session.getAttribute(PREFIX)) + message);
        }
    }

    public static void info(HttpSession session, String message, Throwable cause) {
        Logger log = getLogger(session);
        if (log.isInfoEnabled()) {
            log.info(String.valueOf(session.getAttribute(PREFIX)) + message,
                    cause);
        }
    }

    public static void warn(HttpSession session, String message) {
        Logger log = getLogger(session);
        if (log.isWarnEnabled()) {
            log.warn(String.valueOf(session.getAttribute(PREFIX)) + message);
        }
    }

    public static void warn(HttpSession session, String message, Throwable cause) {
        Logger log = getLogger(session);
        if (log.isWarnEnabled()) {
            log.warn(String.valueOf(session.getAttribute(PREFIX)) + message,
                    cause);
        }
    }

    public static void error(HttpSession session, String message) {
        Logger log = getLogger(session);
        if (log.isErrorEnabled()) {
            log.error(String.valueOf(session.getAttribute(PREFIX)) + message);
        }
    }

    public static void error(HttpSession session, String message, Throwable cause) {
        Logger log = getLogger(session);
        if (log.isErrorEnabled()) {
            log.error(String.valueOf(session.getAttribute(PREFIX)) + message,
                    cause);
        }
    }

    public static boolean isDebugEnabled(HttpSession session) {
        return getLogger(session).isDebugEnabled();
    }

    public static boolean isInfoEnabled(HttpSession session) {
        return getLogger(session).isInfoEnabled();
    }

    public static boolean isWarnEnabled(HttpSession session) {
        return getLogger(session).isWarnEnabled();
    }

    public static boolean isErrorEnabled(HttpSession session) {
        return getLogger(session).isErrorEnabled();
    }

    private static Logger getLogger(HttpSession session) {
        Logger log = (Logger) session.getAttribute(LOGGER);
        if (log == null) {
            log = LoggerFactory.getLogger(getClass(session));
            String prefix = (String) session.getAttribute(PREFIX);
            if (prefix == null) {
                prefix = "[" + session.getName() + "] ";
                session.setAttribute(PREFIX, prefix);
            }

            session.setAttribute(LOGGER, log);
        }

        return log;
    }
}
