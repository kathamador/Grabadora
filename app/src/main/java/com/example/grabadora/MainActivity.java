package com.example.grabadora;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarGrabacion();
            }
        });

        Button reproducir = findViewById(R.id.btnver);
        reproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reproducir();
            }
        });

        Button detener = findViewById(R.id.btndetener);
        detener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detenerGrabacion();
            }
        });

        Button detenerRepro = findViewById(R.id.btnverdet);
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
    }

    private void detenerGrabacion() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void reproducir() {
        player = new MediaPlayer();

        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("AUDIO", "Error al reproducir audio.");
        }
    }

    private void pararReproduccion() {
        player.release();
        player = null;
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