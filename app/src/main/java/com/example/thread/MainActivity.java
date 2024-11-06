package com.example.thread;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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

    // Inner class
    private class TareaAsincrona extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            for (int i=1 ; i<=10 ; i++){
                ejecutarTareaLaraga();
                publishProgress(i*10);
                if (isCancelled()){
                    break;
                }
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            barra.setMax(100);
            barra.setProgress(0);
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Boolean result) {
        if (result){
            Toast.makeText(MainActivity.this,"Tareas AsyncTask terminada", Toast.LENGTH_SHORT).show();
        }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int val=values[0].intValue();
            barra.setProgress(val);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(MainActivity.this,"Tarea cancelada",Toast.LENGTH_SHORT).show();
        }
    }

    private class TareaAsincronaDialog extends AsyncTask<Void, Integer, Boolean> {



        @Override
        protected Boolean doInBackground(Void... params) {
            for (int i=1 ; i<=10 ; i++){
                ejecutarTareaLaraga();
                publishProgress(i*10);
                if (isCancelled()){
                    break;
                }
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    TareaAsincronaDialog.this.cancel(true);

                }
            });
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Boolean result) {
            if (result){
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Tareas AsyncTask terminada", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int val=values[0].intValue();
            progressDialog.setProgress(val);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(MainActivity.this,"Tarea cancelada",Toast.LENGTH_SHORT).show();
        }
    }

ProgressDialog progressDialog;
ProgressBar barra;
Button boton_sin_hilos;
Button boton_con_hilos;
Button boton_con_async;
Button boton_con_dialogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        barra = findViewById(R.id.progressBar);
        boton_sin_hilos= findViewById(R.id.btn_sin_hilos);
        boton_con_hilos=findViewById(R.id.btn_con_hilos);
        boton_con_async= findViewById(R.id.btn_con_async);
        boton_con_dialogo=findViewById(R.id.btn_Dialogo);


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
        }); // fin del boton con hilos

        boton_con_async.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TareaAsincrona tarea= new TareaAsincrona();
                tarea.execute();
            }
        });

        boton_con_async.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              progressDialog=new ProgressDialog(MainActivity.this);
              progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
              progressDialog.setMessage("Ejecutando una tarea...");
              progressDialog.setCancelable(true);
              progressDialog.setMax(100);
              TareaAsincronaDialog task = new TareaAsincronaDialog();
              task.execute();
            }
        }); // fin de boton con async
    }

    public void ejecutarTareaLaraga(){
        try {
        Thread.sleep(1000);
        }catch (InterruptedException e){

        }
    }

}