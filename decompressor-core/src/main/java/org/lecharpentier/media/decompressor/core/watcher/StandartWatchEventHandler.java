package org.lecharpentier.media.decompressor.core.watcher;

import org.lecharpentier.media.decompressor.core.extraction.Decompressor;
import org.lecharpentier.media.decompressor.core.extraction.DecompressorRegistry;
import org.lecharpentier.media.decompressor.core.extraction.ExtractionException;
import org.lecharpentier.media.decompressor.core.utils.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StandartWatchEventHandler implements WatchEventHandler {

    private final ExecutorService pool = Executors.newCachedThreadPool();

    @Override
    public void onEvent(WatchEvent.Kind eventKind, final Path eventPath) {
        pool.submit(new DecompressionJob(eventPath));
    }


    private static class DecompressionJob implements Runnable {
        private final DecompressorRegistry decompressorRegistry = DecompressorRegistry.getInstance();

        private final Path eventPath;

        public DecompressionJob(Path eventPath) {
            this.eventPath = eventPath;
        }

        @Override
        public void run() {
            String fileName = eventPath.getFileName().toString();
            String extension = getExtension(fileName);
            File file = eventPath.toFile();

            if (decompressorRegistry.supportsExtension(extension)) {

                try {
                    FileUtils.waitForFileOperationCompletion(file, 100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    return;
                }

                try {
                    Decompressor decompressor = decompressorRegistry.getForExtension(extension);
                    decompressor.decompress(file);
                } catch (ExtractionException e) {
                    e.printStackTrace();
                }
            }
        }

        public String getExtension(String fileName) {
            int lastDotIndex = fileName.lastIndexOf('.');
            return lastDotIndex == -1 ? null : fileName.substring(lastDotIndex + 1);
        }
    }
}
