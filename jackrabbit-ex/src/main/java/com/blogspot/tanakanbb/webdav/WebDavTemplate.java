package com.blogspot.tanakanbb.webdav;

import java.io.File;
import java.io.InputStream;

/**
 * WebDAV Client interface
 *
 */
public interface WebDavTemplate {

    void put(File file, String uri) throws WebDavException;
    void put(String filePath, String uri) throws WebDavException;
    byte[] getFile(String uri) throws WebDavException;
    InputStream getStream(String uri) throws WebDavException;
}
