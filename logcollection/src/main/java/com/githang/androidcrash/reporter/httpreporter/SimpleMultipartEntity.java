package com.githang.androidcrash.reporter.httpreporter;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Random;

public class SimpleMultipartEntity {

    /**
     * ASCII的字符池，用于生成分界线。
     */
    private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
            .toCharArray();

    private DataOutputStream out;
    /**
     * 是否设置了开头的分界线
     */
    boolean isSetFirst = false;
    /**
     * 是否设置了最后的分界线
     */
    boolean isSetLast = false;

    /**
     * 分界线
     */
    private String boundary;

    public SimpleMultipartEntity(HttpURLConnection urlConnection) {

        try {
            out = new DataOutputStream(urlConnection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        boundary = generateBoundary();
    }

    /**
     * 生成分界线
     *
     * @return 返回生成的分界线
     */
    protected String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30; // a random size from 30 to 40
        for (int i = 0; i < count; i++) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }

    public void writeFirstBoundaryIfNeeds() {
        if (!isSetFirst) {
            try {
                out.write(("--" + boundary + "\r\n").getBytes());
            } catch (final IOException e) {
                Log.w(e.getMessage(), e);
            }
        }
        isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
        if (!isSetLast) {
            try {
                out.write(("\r\n--" + boundary + "--\r\n").getBytes());
            } catch (final IOException e) {
                Log.w(e.getMessage(), e);
            }
            isSetLast = true;
        }
    }

    public void addPart(final String key, final String value) {
        writeFirstBoundaryIfNeeds();
        try {
            out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
            out.write(value.getBytes());
            out.write(("\r\n--" + boundary + "\r\n").getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void addPart(final String key, final String fileName, final InputStream fin,
                        final boolean isLast) {
        addPart(key, fileName, fin, "application/octet-stream", isLast);
    }

    public void addPart(final String key, final String fileName, final InputStream fin,
                        String type, final boolean isLast) {
        writeFirstBoundaryIfNeeds();
        try {
            type = "Content-Type: " + type + "\r\n";
            out.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\""
                    + fileName + "\"\r\n").getBytes());
            out.write(type.getBytes());
            out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

            final byte[] tmp = new byte[4096];
            int l = 0;
            while ((l = fin.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
            if (!isLast)
                out.write(("\r\n--" + boundary + "\r\n").getBytes());
            out.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPart(final String key, final File value, final boolean isLast) {
        try {
            addPart(key, value.getName(), new FileInputStream(value), isLast);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return out.size();
    }

    public boolean isStreaming() {
        return false;
    }

    public void consumeContent() throws IOException, UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
                    "Streaming entity does not implement #consumeContent()");
        }
    }

}