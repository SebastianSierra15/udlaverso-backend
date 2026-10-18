package com.udlaverso.udlaversobackend.util;

import java.text.Normalizer;

/** Utilities for converting client-provided upload names into safe file names. */
public final class FileNameUtils {

    private FileNameUtils() {
    }

    public static String sanitize(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) {
            return "archivo";
        }

        String filename = originalFilename.replace('\\', '/');
        int lastSeparator = filename.lastIndexOf('/');
        filename = lastSeparator >= 0 ? filename.substring(lastSeparator + 1) : filename;

        filename = Normalizer.normalize(filename, Normalizer.Form.NFKC)
                .replaceAll("[^\\p{L}\\p{N}._-]", "_")
                .replaceAll("_+", "_");

        if (filename.isBlank() || filename.equals(".") || filename.equals("..")) {
            return "archivo";
        }

        return filename;
    }
}
