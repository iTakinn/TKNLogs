package org.itakinn.tknlogs;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;


public class ErrorCatcher extends Handler {
    @Override
    public void publish(LogRecord record) {
        String msg = record.getMessage();
        Throwable exce = record.getThrown();
        TKNLogs.getDiscord().sendMessage(msg);
        if (exce != null) {
            TKNLogs.getDiscord().sendMessage(exce.toString());
            exce.printStackTrace();
        }
    }
    public static void register() {
        java.util.logging.Logger rootLogger = java.util.logging.Logger.getLogger("");
        rootLogger.addHandler(new ErrorCatcher());
    }
    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
