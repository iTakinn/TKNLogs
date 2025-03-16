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
    private final TKNLogs plugin;
    private long lastPosition = 0;
    private Path logFilePath;
    private WatchService watchService;

    public LoggerReader(TKNLogs plugin) {
        this.plugin = plugin;
        File serverDir = Bukkit.getServer().getWorldContainer();
        this.logFilePath = Paths.get(serverDir.getAbsolutePath(), "logs", "latest.log");
    }

    public void start() {
        if (!Files.exists(logFilePath)) {
            plugin.getLogger().severe("Arquivo latest.log não encontrado!");
            return;
        }

        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path logDir = logFilePath.getParent();
            logDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        WatchKey key = watchService.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            if (event.context().toString().equals("latest.log")) {
                                readNewLines();
                            }
                        }
                        key.reset();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

        } catch (IOException e) {
            plugin.getLogger().severe("Erro ao iniciar monitor: " + e.getMessage());
        }
    }

    private void readNewLines() {
        try (RandomAccessFile raf = new RandomAccessFile(logFilePath.toFile(), "r")) {
            if (raf.length() < lastPosition) {
                lastPosition = 0;
            }

            raf.seek(lastPosition);
            String line;
            while ((line = raf.readLine()) != null) {
                String utf8Line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                sendToDiscord(utf8Line);
            }
            lastPosition = raf.getFilePointer();

        } catch (IOException e) {
            plugin.getLogger().severe("Erro ao ler latest.log: " + e.getMessage());
        }
    }

    private void sendToDiscord(String message) {
        if (TKNLogs.getDiscord() != null) {
            TKNLogs.getDiscord().sendMessage("```" + message + "```");
        }
    }

    public void stop() {
        try {
            if (watchService != null) {
                watchService.close();
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Erro ao parar monitor: " + e.getMessage());
        }
    }
}