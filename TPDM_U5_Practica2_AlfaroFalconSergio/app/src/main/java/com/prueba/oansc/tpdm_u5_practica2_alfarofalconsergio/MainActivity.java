package com.prueba.oansc.tpdm_u5_practica2_alfarofalconsergio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText consulta, numero;
    Button enviar, ayuda;
    ListView resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        consulta = findViewById(R.id.consulta);
        numero = findViewById(R.id.numero);
        enviar = findViewById(R.id.enviar);
        resultado = findViewById(R.id.resultado);
        ayuda = findViewById(R.id.ayuda);

        solicitarPermisos();

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje();
            }
        });

        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarComandos();
            }
        });

        Intent intent = getIntent();
        String res = intent.getStringExtra("respuesta");
        if (res != null) {
            String[] lista =  res.split("\n");
            ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
            resultado.setAdapter(adaptador);
        }
    }

    private void solicitarPermisos () {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS},1);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECEIVE_SMS},2);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS},4);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_PHONE_STATE},3);
        }
    }

    private void enviarMensaje () {
        try{
            String tel = numero.getText().toString();
            String men = consulta.getText().toString();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(tel,null,men,null,null);
            Toast.makeText(this, "Instrucción enviada", Toast.LENGTH_SHORT).show();
            consulta.setText("");
            numero.setText("");
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarComandos () {
        AlertDialog.Builder cuadro = new AlertDialog.Builder(this);
        cuadro.setTitle("Consultas")
                .setMessage("Ver todos los contactos:\nTODOS\n\nAgregar un contacto:\nAGREGAR SERGIO 3111215684\n\nBuscar por nombre:\nNOMBRE SERGIO\n\nBuscar por teléfono:\nTELEFONO 3111215684")
                .setPositiveButton("Si", null)
                .show();
    }

}
