package org.itakinn.tknlogs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;


public class LoggerReader {
    private final JavaPlugin plugin;
    private Path logDir;
    private WatchService watchService;
    private long lastReadPosition;

    public LoggerReader() {
        this.plugin = TKNLogs.getPlugin();
        this.logDir = Paths.get(plugin.getDataFolder().getParentFile().getParent(), "logs");
        this.lastReadPosition = 0;
    }

    public void start() {
        try {
            // Configura o WatchService para monitorar modificações no diretório
            watchService = FileSystems.getDefault().newWatchService();
            logDir.register(watchService, ENTRY_MODIFY);

            // Inicia a thread de monitoramento assíncrona
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                while (!Thread.currentThread().isInterrupted()) {
                    WatchKey key;
                    try {
                        key = watchService.take(); // Aguarda eventos
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == ENTRY_MODIFY) {
                            Path changedFile = logDir.resolve((Path) event.context());
                            if (changedFile.endsWith("latest.log")) {
                                processLogChanges(changedFile.toFile());
                            }
                        }
                    }
                    key.reset();
                }
            });

        } catch (IOException e) {
            plugin.getLogger().severe("Erro ao iniciar monitor de logs: " + e.getMessage());
        }
    }

    private void processLogChanges(File logFile) {
        try (RandomAccessFile raf = new RandomAccessFile(logFile, "r")) {
            // Pula para a última posição lida
            if (raf.length() < lastReadPosition) {
                lastReadPosition = 0; // Log foi reiniciado (ex: servidor reiniciou)
            }
            raf.seek(lastReadPosition);

            String line;
            while ((line = raf.readLine()) != null) {
                line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                TKNLogs.getDiscord().sendMessage(line);
            }

            lastReadPosition = raf.getFilePointer();

        } catch (IOException e) {
            plugin.getLogger().severe("Erro ao ler latest.log: " + e.getMessage());
        }
    }
}
