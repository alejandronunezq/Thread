package com.example.thread;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
ProgressBar barra;
Button boton_sin_hilos;
Button boton_con_hilos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        barra = findViewById(R.id.progressBar);
        boton_sin_hilos= findViewById(R.id.btn_sin_hilos);
        boton_con_hilos=findViewById(R.id.btn_con_hilos);

        boton_sin_hilos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barra.setMax(100);
                barra.setProgress(0);

                for(int i=0 ; i <10; i++){
                    ejecutarTareaLaraga();
                    barra.incrementProgressBy(10);
                }
                Toast.makeText(getApplicationContext(),"Tarea finalizada", Toast.LENGTH_SHORT).show();

            }
        }); // click del otro boton sin hilos
        boton_con_hilos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        barra.post(new Runnable() {
                            @Override
                            public void run() {
                                barra.setMax(100);
                                barra.setProgress(0);
                            }
                        });

                        for (int i = 0; i < 10; i++) {
                            ejecutarTareaLaraga();

                            barra.post(new Runnable() {
                                @Override
                                public void run() {
                                    barra.incrementProgressBy(10);
                                }
                            });
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Tarea terminada", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });


    }

    public void ejecutarTareaLaraga(){
        try {
        Thread.sleep(1000);
        }catch (InterruptedException e){

        }
    }

}