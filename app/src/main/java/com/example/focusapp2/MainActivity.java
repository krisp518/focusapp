package com.example.focusapp2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    //tickle me.

    Button btnNotify;
    Button btnPickImage;
    ImageView imageView;
    TextView message;
    EditText inputMessage;

    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //image
        btnPickImage = findViewById(R.id.btnPickImage);
        imageView = findViewById(R.id.imageView);
        registerResult();
        btnPickImage.setOnClickListener(view -> pickImage());

        //notification
        btnNotify = findViewById(R.id.btnNotify);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notifications", "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //notification code
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My Notifications");
                builder.setContentTitle("my title");
                builder.setContentText("lock in ong");
                builder.setSmallIcon(R.drawable.ic_launcher_background);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);

                //notification permission
                /*if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
                }
                else {
                    Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
                }*/
                managerCompat.notify(1, builder.build());
            }
        });

        //message
        message = (TextView) findViewById(R.id.message);
        inputMessage = (EditText) findViewById(R.id.inputMessage);
    }

    private void pickImage(){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    private void registerResult(){
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try{
                            Uri imageUri = result.getData().getData();
                            imageView.setImageURI(imageUri);
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this,"No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public void updateMessage(View view){
        message.setText("\"" + inputMessage.getText() + "\"");
        inputMessage.setText(" ");
        inputMessage.clearFocus();
    }




}