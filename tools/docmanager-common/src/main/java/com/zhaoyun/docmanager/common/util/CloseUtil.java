package com.zhaoyun.docmanager.common.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class CloseUtil {
    public static void closeQuietly(Closeable closeable) {
        if (closeable == null)
            return;
        try {
            closeable.close();
        } catch (IOException ex) {
            assert true;
        }
    }

    public static void closeQuietly(Socket socket) {
        if (socket == null)
            return;
        try {
            socket.close();
        } catch (IOException ex) {
            assert true;
        }
    }

    public static void closeQuietly(ServerSocket serverSocket) {
        if (serverSocket == null)
            return;
        try {
            serverSocket.close();
        } catch (IOException ex) {
            assert true;
        }
    }
}
