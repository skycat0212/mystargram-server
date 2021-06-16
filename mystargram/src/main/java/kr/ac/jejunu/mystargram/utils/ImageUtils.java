package kr.ac.jejunu.mystargram.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtils {

    public static void writeImageAtFile(byte[] imageBytes, File file) throws IOException {
        if (!file.exists())
            file.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(imageBytes);
        fileOutputStream.close();

    }

    public static String base64Encode(String string) {
        byte[] encoded = Base64.getEncoder().encode(string.getBytes());
        return  new String(encoded);
    }

    public static byte[] base64ToByteArray(String base64String) {
        byte[] decoded = Base64.getDecoder().decode(base64String);
        return  decoded;
    }

    public static String generateImgPath(String directoryName, String fileName, String extension) {
        String baseUrl = new File("").getAbsolutePath() + "/src/main/webapp/";
        String imagePath = String.format("%s/images/%s/%s.%s", baseUrl, directoryName, fileName, extension);
        return imagePath;
    }


}
