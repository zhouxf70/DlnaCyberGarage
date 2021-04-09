/******************************************************************
 *
 *	CyberUtil for Java
 *
 *	Copyright (C) Satoshi Konno 2002
 *
 *	File: Debug.java
 *
 *	Revision;
 *
 *	11/18/02
 *		- first revision.
 *
 ******************************************************************/

package org.cybergarage.util;

import android.util.Log;

public final class KLog {

    public static boolean isDebug = true;

    public static boolean isOn() {
        return isDebug;
    }

    public static void d(String s) {
        println(s);
    }

    public static void d(String m1, String m2) {
        println(m1);
        println(m2);
    }

    public static void w(String s) {
        println(s);
    }

    public static void w(String m, Exception e) {
        if (e.getMessage() == null) {
            println(m + " START");
            println(m + " END");
        } else {
            println(m + " (" + e.getMessage() + ")");
        }
    }

    public static void w(Exception e) {
        println(e.getMessage());
    }

    public static void println(String msg) {
        if (!isDebug) return;

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String fileName = stackTrace[4].getFileName();
        int lineNumber = stackTrace[4].getLineNumber();
        String methodName = stackTrace[4].getMethodName();
        String stack = "[(" + fileName + ":" + lineNumber + ")#" + methodName + "] ";

        Log.d("CyberGarage", stack + msg);
    }
}
