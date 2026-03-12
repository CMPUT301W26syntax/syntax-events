package com.example.syntaxappproject;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class QRCodeService {
    private static String openAppString = "syntaxappproject://open";

    public static Bitmap generateQRCode (String deep_link) {
        BitMatrix matrix = makeQRCodeMatrix(deep_link);
        return toBitmap(matrix);
    }

    private static BitMatrix makeQRCodeMatrix(String deep_link) {
        // Code mostly from https://lknuth.dev/writings/generating_qrcodes_with_zxing/
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(
                    deep_link, BarcodeFormat.QR_CODE, 512, 512
            );
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    // Code from https://lknuth.dev/writings/generating_qrcodes_with_zxing/
    private static Bitmap toBitmap(BitMatrix matrix){
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    private static String decodeQRCode(Bitmap bMap) {
        String contents = null;

        // 1. Convert the Bitmap into an int array of pixels
        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        // 2. Create a LuminanceSource from the pixel array
        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);

        // 3. Convert to a BinaryBitmap (ZXing's internal format)
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        // 4. Use MultiFormatReader to decode the bitmap
        MultiFormatReader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            contents = result.getText();
        } catch (Exception e) {
            // This exception is common if no QR code is found in the frame
            e.printStackTrace();
        }

        return contents;
    }



    // Will be used later in development for storing QR codes
    // Code from https://lknuth.dev/writings/generating_qrcodes_with_zxing/
    /*
    public static void storeBitMap(Bitmap qrcode_bmp) {
        File sdcard = Environment.getExternalStorageDirectory();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(sdcard, "qrcode.jpg"));
            boolean success = qrcode_bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            if (success){
                // specify where to store the image
            } else {
                System.out.println("Unable to store image"); //useful for debugging if needed
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    */
}
