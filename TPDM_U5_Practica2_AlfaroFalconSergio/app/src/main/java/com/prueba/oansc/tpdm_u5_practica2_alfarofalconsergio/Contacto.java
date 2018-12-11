package com.prueba.oansc.tpdm_u5_practica2_alfarofalconsergio;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class Contacto {

    int id;
    String nombre, telefono;
    BaseDatos base;

    public Contacto (Context activity) {
        base = new BaseDatos(activity, "agenda", null, 1);
    }

    public Contacto (int id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public boolean insertar (Contacto nuevo) {
        try {
            SQLiteDatabase agenda = base.getWritableDatabase();
            ContentValues datos = new ContentValues();
            datos.put("NOMBRE", nuevo.nombre);
            datos.put("TELEFONO", nuevo.telefono);
            long seInserto = agenda.insert("CONTACTO", "ID", datos);
            agenda.close();
            if (seInserto == -1) {
                return false;
            }
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    public Contacto[] obtenerTodos () {
        Contacto[] todos = null;
        try {
            SQLiteDatabase agenda = base.getReadableDatabase();
            Cursor c = agenda.rawQuery("SELECT * FROM CONTACTO", null);
            if (c.moveToFirst()){
                todos = new Contacto[c.getCount()];
                int pos = 0;
                do {
                    todos[pos++] = new Contacto(c.getInt(0), c.getString(1),c.getString(2));
                } while (c.moveToNext());
            }
            agenda.close();
        } catch (SQLiteException e) {
            return null;
        }
        return todos;
    }

    public Contacto buscarUnoPorNombre (String nombre) {
        Contacto recuperado = null;
        try {
            SQLiteDatabase agenda = base.getReadableDatabase();
            String[] columnas = {"ID", "NOMBRE", "TELEFONO"};
            String[] argumentos = {nombre};
            Cursor resultado = agenda.query("CONTACTO", columnas, "NOMBRE = ?", argumentos, null, null , null, null);
            if (resultado.moveToFirst()) {
                recuperado = new Contacto(resultado.getInt(0), resultado.getString(1), resultado.getString(2));
            }
            agenda.close();
        } catch (SQLiteException e) {
            return null;
        }
        return recuperado;
    }

    public Contacto buscarUnoPorTelefono (String telefono) {
        Contacto recuperado = null;
        try {
            SQLiteDatabase agenda = base.getReadableDatabase();
            String[] columnas = {"ID", "NOMBRE", "TELEFONO"};
            String[] argumentos = {telefono};
            Cursor resultado = agenda.query("CONTACTO", columnas, "TELEFONO = ?", argumentos, null, null , null, null);
            if (resultado.moveToFirst()) {
                recuperado = new Contacto(resultado.getInt(0), resultado.getString(1), resultado.getString(2));
            }
            agenda.close();
        } catch (SQLiteException e) {
            return null;
        }
        return recuperado;
    }

    public boolean actualizarPorNombre (String nombre, String telefono) {
        try {
            SQLiteDatabase agenda = base.getReadableDatabase();
            ContentValues contacto = new ContentValues();
            String[] params = {nombre};
            contacto.put("TELEFONO", telefono);
            long seActualizo = agenda.update("CONTACTO", contacto, "NOMBRE = ?", params);
            agenda.close();
            if (seActualizo == -1) {
                return false;
            }
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    public boolean actualizarPorTelefono (String nombre, String telefono) {
        try {
            SQLiteDatabase agenda = base.getReadableDatabase();
            ContentValues contacto = new ContentValues();
            String[] params = {telefono};
            contacto.put("NOMBRE", nombre);
            long seActualizo = agenda.update("CONTACTO", contacto, "TELEFONO = ?", params);
            agenda.close();
            if (seActualizo == -1) {
                return false;
            }
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    public boolean eliminarPorNombre (String nombre) {
        try {
            SQLiteDatabase agenda = base.getWritableDatabase();
            String[] params = {nombre};
            long eliminado = agenda.delete("CONTACTO", "NOMBRE = ?", params);
            agenda.close();
            if (eliminado == -1) {
                return false;
            }
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    public boolean eliminarPorTelefono (String telefono) {
        try {
            SQLiteDatabase agenda = base.getWritableDatabase();
            String[] params = {telefono};
            long eliminado = agenda.delete("CONTACTO", "TELEFONO = ?", params);
            agenda.close();
            if (eliminado == -1) {
                return false;
            }
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

}
