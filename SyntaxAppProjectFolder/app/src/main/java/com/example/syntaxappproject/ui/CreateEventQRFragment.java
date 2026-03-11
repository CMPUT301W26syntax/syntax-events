package com.example.syntaxappproject.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.navigation.Navigation;

import com.example.syntaxappproject.QRCodeService;
import com.example.syntaxappproject.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CreateEventQRFragment extends HomeBar {

    private String eventId;
    private Bitmap qrBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_event_qr_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
        }

        TextView successText = view.findViewById(R.id.success_text);
        ImageView qrPreview = view.findViewById(R.id.event_qr_preview);

        if (eventId != null) {
            if (successText != null) {
                successText.setText("Event created!");
            }
            // Generate QR code based on eventId
            qrBitmap = QRCodeService.generateQRCode(eventId);
            if (qrPreview != null && qrBitmap != null) {
                qrPreview.setImageBitmap(qrBitmap);
            }
        }

        view.findViewById(R.id.back_button).setOnClickListener(v -> 
            Navigation.findNavController(v).popBackStack()
        );

        view.findViewById(R.id.download_button).setOnClickListener(v -> downloadQRCode());}

    private void downloadQRCode() {
        if (qrBitmap == null) return;

        String filename = "QR_" + eventId + ".png";
        OutputStream fos;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/SyntaxEvents");

                Uri imageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = requireContext().getContentResolver().openOutputStream(imageUri);
            } else {
                File imagesDir = new File(requireContext().getExternalFilesDir(null), "SyntaxEvents");
                if (!imagesDir.exists()) imagesDir.mkdirs();
                File image = new File(imagesDir, filename);
                fos = new FileOutputStream(image);
            }

            if (fos != null) {
                qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                Toast.makeText(getContext(), "QR Code downloaded to Pictures", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to download QR Code", Toast.LENGTH_SHORT).show();
        }
    }
}
