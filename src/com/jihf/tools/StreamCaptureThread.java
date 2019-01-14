package com.jihf.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class StreamCaptureThread implements Runnable {
    InputStream stream;
    StringBuilder output;

    public StreamCaptureThread(InputStream stream) {
        this.stream = stream;
        this.output = new StringBuilder();
    }

    public void run() {
        try {
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(this.stream, Charset.forName("UTF-8")));
                String line = br.readLine();
                while (line != null) {
                    if (line.trim().length() > 0) {
                        output.append(line).append("\n");
                    }
                    line = br.readLine();
                }
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
