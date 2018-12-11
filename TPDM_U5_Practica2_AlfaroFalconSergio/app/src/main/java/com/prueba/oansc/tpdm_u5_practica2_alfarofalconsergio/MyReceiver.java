package com.prueba.oansc.tpdm_u5_practica2_alfarofalconsergio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Object[] pdus = (Object[]) extras.get("pdus");
        SmsMessage mensaje=SmsMessage.createFromPdu((byte[])pdus[0]);

        Intent respuesta = new Intent(context, MainActivity.class);

        Contacto base = new Contacto(context);

        String texto = mensaje.getMessageBody();
        String numero = mensaje.getOriginatingAddress();

        if (texto.startsWith("RES")) {
            String params[] = texto.split("--");
            respuesta.putExtra("respuesta",params[1]);
            context.startActivity(respuesta);
            return;
        }
        if (texto.equals("TODOS")) {
            Contacto[] todos = base.obtenerTodos();
            if (todos != null) {
                String res = "";
                for (int i = 0; i < todos.length; i++) {
                    res += todos[i].nombre + " - " + todos[i].telefono + "\n";
                }
                enviarMensaje(numero, "RES--" + res);
            } else {
                enviarMensaje(numero,"RES--No hay contactos");
            }
            return;
        }
        if (texto.startsWith("AGREGAR")) {
            String[] parametros = texto.split(" ");
            if (parametros.length == 3) {
                Contacto nuevo = new Contacto(1, parametros[1], parametros[2]);
                if (base.insertar(nuevo)) {
                    enviarMensaje(numero, "RES--Se guardó el nuevo contacto");
                } else {
                    enviarMensaje(numero, "RES--Error al guardar");
                }
            }
            return;
        }
        if (texto.startsWith("NOMBRE")) {
            String[] parametros = texto.split(" ");
            if (parametros.length == 2) {
                Contacto contacto = base.buscarUnoPorNombre(parametros[1]);
                if (contacto != null) {
                    enviarMensaje(numero, "RES--" + contacto.nombre + " - " + contacto.telefono);
                } else {
                    enviarMensaje(numero, "RES--No se encontró el nombre " + parametros[1]);
                }
            }
            return;
        }
        if (texto.startsWith("TELEFONO")) {
            String[] parametros = texto.split(" ");
            if (parametros.length == 2) {
                Contacto contacto = base.buscarUnoPorTelefono(parametros[1]);
                if (contacto != null) {
                    enviarMensaje(numero, "RES--"+contacto.nombre + " - " + contacto.telefono);
                } else {
                    enviarMensaje(numero, "RES--No se encontró el teléfono " + parametros[1]);
                }
            }
            return;
        }
        enviarMensaje(numero, "RES--No se encontró la instrucción");
    }

    private void enviarMensaje (String tel, String men) {
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(tel,null,men,null,null);
        }catch (Exception e){
        }
    }

}
