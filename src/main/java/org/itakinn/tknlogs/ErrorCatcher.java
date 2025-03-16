package org.itakinn.tknlogs;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;


public class ErrorCatcher extends Handler {
    @Override
    public void publish(LogRecord record) {
        if (record.getLevel() == Level.ALL) {
            String mensagem = record.getMessage();
            Throwable excecao = record.getThrown();
            TKNLogs.getDiscord().sendMessage(mensagem);
            
            //Bukkit.getConsoleSender().sendMessage("§c[ERROR] " + mensagem);

            if (excecao != null) {
                TKNLogs.getDiscord().sendMessage(excecao.toString());
                excecao.printStackTrace();
            }
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
