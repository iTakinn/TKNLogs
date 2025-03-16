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

    public LoggerReader(TKNLogs pl) {
        this.plugin = pl;
        Path logDir = Paths.get(Bukkit.getServer().getWorldContainer().getAbsolutePath(), "logs");
        this.lastReadPosition = 0;
        
    }

    public void start() {
        try {

            watchService = FileSystems.getDefault().newWatchService();
            logDir.register(watchService, ENTRY_MODIFY);

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                while (!Thread.currentThread().isInterrupted()) {
                    WatchKey key;
                    try {
                        key = watchService.take();
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

            if (raf.length() < lastReadPosition) {
                lastReadPosition = 0;
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
