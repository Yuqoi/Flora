package com.example.aplikacja.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikacja.R;
import com.example.aplikacja.ml.PlantRecognize;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

public class CameraActivity extends AppCompatActivity {

    TextView twojKwiat, nazwaKwiata, result, importImageText, camera_infotext;
    AppCompatButton przycisk;
    AppCompatImageButton returnButton;

    CardView cardVievForImage;
    ImageView imageView;

    private static final float CONFIDENCE_THRESHOLD = 0.8f;

    int imageSize = 224;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        twojKwiat = findViewById(R.id.camera_twojkwiat);
        nazwaKwiata = findViewById(R.id.camera_nazwakwiata);
        result = findViewById(R.id.camera_result);
        returnButton = findViewById(R.id.camera_return_button);
        camera_infotext = findViewById(R.id.camera_infotext);
        przycisk = findViewById(R.id.camera_takepicture);
        importImageText = findViewById(R.id.camera_importimage);
        cardVievForImage = findViewById(R.id.camera_cardview_takenimage);
        imageView = findViewById(R.id.camera_takenimage);

        camera_infotext.setVisibility(View.VISIBLE);
        cardVievForImage.setVisibility(View.GONE);
        importImageText.setVisibility(View.VISIBLE);
        twojKwiat.setVisibility(View.GONE);
        nazwaKwiata.setVisibility(View.GONE);
        result.setVisibility(View.GONE);



        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        przycisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                }else{
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            assert data != null;
            Bitmap image = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            assert image != null;
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image,dimension,dimension);
            imageView.setImageBitmap(image);

            camera_infotext.setVisibility(View.GONE);
            cardVievForImage.setVisibility(View.VISIBLE);
            importImageText.setVisibility(View.GONE);
            twojKwiat.setVisibility(View.VISIBLE);
            nazwaKwiata.setVisibility(View.VISIBLE);
            result.setVisibility(View.VISIBLE);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        }



    }
    private void classifyImage(Bitmap image){
        try{
            PlantRecognize model = PlantRecognize.newInstance(getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intVal = new int[imageSize * imageSize];
            image.getPixels(intVal, 0, image.getWidth(), 0,0, image.getWidth(), image.getHeight());

            int pixel = 0;
            for (int i = 0; i < imageSize; i++){
                for (int j = 0; j < imageSize; j++){
                    int val = intVal[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            PlantRecognize.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeatures0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence = outputFeatures0.getFloatArray();

            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidence.length; i++){
                if (confidence[i] > maxConfidence){
                    maxConfidence = confidence[i];
                    maxPos = i;
                }
            }

            if (maxConfidence < CONFIDENCE_THRESHOLD) {
                displayRecognitionError();
            } else {
                String[] classes = {"Lilak", "Krokus", "Goździk", "bratek",
                        "Fiołek", "Trójskrzyn", "Zielistka sternberga", "Paproć",
                        "Pelargonia", "róża", "Różanecznik", "Stokrotka",
                        "Storczyk", "Tulipan", "Nagietek", "Mak",
                        "Magnolia", "Lawenda", "Irys", "Hortensja",
                        "Hiacynt", "astra chińska"
                };
                result.setText(classes[maxPos]);
                nazwaKwiata.setText(result.getText());
                result.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q="+result.getText())));
                    }
                });
            }

            model.close();
        }catch (IOException e){
            Toast.makeText(getApplicationContext(), "Nie znaleziono", Toast.LENGTH_SHORT).show();
        }
    }
    private void displayRecognitionError() {
        // Show a toast message or an alert dialog
        camera_infotext.setVisibility(View.VISIBLE);
        cardVievForImage.setVisibility(View.GONE);
        importImageText.setVisibility(View.VISIBLE);
        twojKwiat.setVisibility(View.GONE);
        nazwaKwiata.setVisibility(View.GONE);
        result.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Error: Nie udało się odczytać rośliny", Toast.LENGTH_LONG).show();
    }
}