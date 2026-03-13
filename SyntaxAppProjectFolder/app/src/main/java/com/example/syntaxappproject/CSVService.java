package com.example.syntaxappproject;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.content.ContentResolver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CSVService {
    public static StringBuilder createCSV(Map<String, Profile> lotteryWinners) {
        StringBuilder csv = new StringBuilder();
        for(int i = 0; i < lotteryWinners.size(); i++){
            csv.append(lotteryWinners.get(i).getName());
            csv.append("\n");
        }
        csv.deleteCharAt(csv.length() - 1);
        return csv;
    }

    public static void storeCSV(Context context, Map<String, Profile> lotteryWinners) {
        StringBuilder csv = createCSV(lotteryWinners);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, "Lottery Winners");
        values.put(MediaStore.Downloads.MIME_TYPE, "text/csv");
        values.put(MediaStore.Downloads.IS_PENDING, 1);

        Uri collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        ContentResolver resolver =  context.getContentResolver();
        Uri fileUri = resolver.insert(collection, values);

        if (fileUri != null) {
            try (OutputStream os = context.getContentResolver().openOutputStream(fileUri)) {
                os.write(csv.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            values.clear();
            values.put(MediaStore.Downloads.IS_PENDING, 0);
            resolver.update(fileUri, values, null, null);
        }
    }
}
