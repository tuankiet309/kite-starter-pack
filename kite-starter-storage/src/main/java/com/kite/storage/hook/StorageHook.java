package com.kite.storage.hook;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;

/**
 * Storage Hook Interface for pre/post-processing
 */
public interface StorageHook {

    /**
     * Called before upload. Exception here stops the upload.
     */
    default void beforeUpload(MultipartFile file) {}

    /**
     * Called after upload.
     * @param file The original multipart file
     * @param filename The stored filename
     * @param path The full path or object name where it was stored
     */
    default void afterUpload(MultipartFile file, String filename, String path) {}
}
