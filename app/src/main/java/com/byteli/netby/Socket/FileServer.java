package com.byteli.netby.Socket;

import android.content.Context;

import com.byteli.netby.KeyNetBy;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by byte on 6/16/16.
 */
public class FileServer {
    ServerSocket server;
    Context context;

    public FileServer(Context context) {
        this.context = context;
    }

    /*public void enabledFileServer(final File file) {
        try {
            server = new ServerSocket(8087);
            while (true) {
                final Socket accept = server.accept();
                final DataOutputStream outputStream = new DataOutputStream(accept.getOutputStream());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Socket temp = accept;
                        byte[] tempBytes = new byte[KeyNetBy.BUFFER_SIZE];
                        try {
                            FileInputStream in = new FileInputStream(file.getPath());
                            while ((in.read(tempBytes)) != -1)
                                System.out.println(tempBytes.toString());
                            System.out.println("File read finish!");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/
}
