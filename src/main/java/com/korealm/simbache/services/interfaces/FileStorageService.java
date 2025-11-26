package com.korealm.simbache.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * Stores a file and returns the accessible URL.
     *
     * @param file     The file to store
     * @param subFolder Optional subfolder within the upload directory
     * @return The URL to access the stored file
     */
    String storeFile(MultipartFile file, String subFolder);

    /**
     * Stores a file in the default location and returns the accessible URL.
     *
     * @param file The file to store
     * @return The URL to access the stored file
     */
    String storeFile(MultipartFile file);

    /**
     * Deletes a file by its filename.
     *
     * @param filename The name of the file to delete
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteFile(String filename);

    /**
     * Extracts the filename from a full URL.
     *
     * @param fileUrl The full URL of the file
     * @return The filename
     */
    String getFilenameFromUrl(String fileUrl);
}