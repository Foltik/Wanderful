package com.example.smax.hackprinceton.util.serialize;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer<T> {
    private String fileName;

    public Serializer(String file) {
        fileName = file;
    }

    public void save(T value) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(value);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (java.lang.Throwable e) {
            Log.e("ERROR", e.toString());
            Log.e("ERROR", "AAAAAAAA", e);
        }
    }

    public T load() {
        T val = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            val = (T) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (java.lang.Throwable e) {
            Log.e("ERROR", e.toString());
            Log.e("ERROR", "AAAAAAAA", e);
        }
        return val;
    }

    public void clear() {
        new File(fileName).delete();
    }

    public boolean exists() {
        return new File(fileName).exists();
    }
}
