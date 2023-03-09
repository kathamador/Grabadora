package com.example.grabadora;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private MediaRecorder recorder;
    private MediaPlayer player;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permisos();

        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audio.mp3";

        Button grabar = findViewById(R.id.btngrabaraudio);
        Button detener = findViewById(R.id.btndetener);
        Button reproducir = findViewById(R.id.btnver);
        Button detenerRepro = findViewById(R.id.btnverdet);

        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarGrabacion();
            }
        });

        reproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reproducir();
            }
        });

        detener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detenerGrabacion();
            }
        });

        detenerRepro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pararReproduccion();
            }
        });
    }

    public void permisos()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO
                    }, 1000);
        }
    }


    private void iniciarGrabacion() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("AUDIO", "Error al grabar audio.");
        }

        recorder.start();
        Toast.makeText(this, "Grabación Iniciada", Toast.LENGTH_SHORT).show();
    }

    private void detenerGrabacion() {
        recorder.stop();
        recorder.release();
        recorder = null;
        Toast.makeText(this, "Grabación Detenida", Toast.LENGTH_SHORT).show();
    }

    private void reproducir() {
        player = new MediaPlayer();

        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            Toast.makeText(this, "Reproduciendo Audio", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("AUDIO", "Error al reproducir audio.");
        }
    }

    private void pararReproduccion() {
        player.release();
        player = null;
        Toast.makeText(this, "Audio Detenido", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (recorder != null) {
            detenerGrabacion();
        }

        if (player != null) {
            pararReproduccion();
        }
    }
}