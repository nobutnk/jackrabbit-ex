package com.blogspot.tanakanbb.webdav.jackrabbit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.blogspot.tanakanbb.webdav.AbstractWebDavTemplate;
import com.blogspot.tanakanbb.webdav.WebDavException;

public class WebDavTemplateJakrabbitImpl extends AbstractWebDavTemplate implements InitializingBean {
    
//    private static final Logger log =
//            LoggerFactory.getLogger(WebDavTemplateJakrabbitImpl.class);
    
    private HttpClient client;
    
    private String username;
    
    private String password;

    public void put(File file, String uri) throws WebDavException {
        try {
            String uploadUrl = uri + "/" + file.getName();

            PutMethod method = new PutMethod(uploadUrl);
            RequestEntity requestEntity =
                    new InputStreamRequestEntity(new FileInputStream(file));
            method.setRequestEntity(requestEntity);
            client.executeMethod(method);

            // 201 Created => No issues
            if (method.getStatusCode() == 204) {
                throw new WebDavException(String.format("%s overwritten with %s", uploadUrl, file.getName()));
            } else if (method.getStatusCode() != 201) {
                throw new WebDavException(String.format("Could not upload %s to %s", file.getName(), uploadUrl));
            } else {
                throw new WebDavException(String.format("Transferred %s to %s", file.getName(), uploadUrl));
            }
        } catch (Exception e) {
            throw new WebDavException("Error transferring " + file.getName(), e);
        }
    }

    public InputStream getStream(String uri) throws WebDavException {
        GetMethod method = new GetMethod(uri);
        try {
            client.executeMethod(method);
            //200 OK => No issues
            if (method.getStatusCode() != 200) {
                throw new WebDavException(method.getStatusCode() + " " + method.getStatusText());
            }
            
            InputStream inputStream = method.getResponseBodyAsStream();
            return inputStream;
        } catch (HttpException e) {
            throw new WebDavException(e);
        } catch (IOException e) {
            throw new WebDavException(e);
        }
    }

    public void afterPropertiesSet() throws Exception {
        client = new HttpClient();
        if (StringUtils.hasLength(username) && StringUtils.hasLength(password)) {
            Credentials creds = new UsernamePasswordCredentials(username, password);
            client.getState().setCredentials(AuthScope.ANY, creds);
        }
    }

}
