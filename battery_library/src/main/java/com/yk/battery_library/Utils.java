package com.yk.battery_library;

import java.io.Closeable;
import java.io.IOException;



public class Utils {
    public static void safeColose(Closeable closeable){
        if (null != closeable){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
