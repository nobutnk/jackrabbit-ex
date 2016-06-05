package com.blogspot.tanakanbb.webdav;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.ResourceUtils;

public abstract class AbstractWebDavTemplate implements WebDavTemplate {

    public void put(String filePath, String uri) throws WebDavException {
        File file;
        try {
            file = ResourceUtils.getFile(filePath);
            put(file, uri);
        } catch (FileNotFoundException e) {
            throw new WebDavException(e);
        }
        
    }

    public byte[] getFile(String uri) throws WebDavException {
        InputStream inputStream = getStream(uri);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[1024];

        try {
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
              buffer.write(data, 0, nRead);
            }
            buffer.flush();

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new WebDavException(e);
        }
    }

}
