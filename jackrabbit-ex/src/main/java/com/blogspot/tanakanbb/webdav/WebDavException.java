package com.blogspot.tanakanbb.webdav;

public class WebDavException extends Exception {

    private static final long serialVersionUID = 1L;

    public WebDavException(Throwable t) {
        super(t);
    }
    
    public WebDavException(String message, Throwable t) {
        super(message, t);
    }
    
    public WebDavException(String message) {
        super(message);
    }
}
