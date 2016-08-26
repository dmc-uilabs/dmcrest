package org.dmc.services.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/upload")
public class ServiceRunFileUploadController {
	
    @Autowired
    public ServiceRunFileUploadController() {
    	super();
    }

    @RequestMapping(method = POST, consumes = MULTIPART_FORM_DATA_VALUE)
    public void handleFormUpload(@RequestPart MultipartFile rawData) throws IOException {
        if (rawData.isEmpty())
            throw new IOException();

        final MediaType rawDataContentType = parseMediaType(rawData.getContentType());
        rawData.transferTo(new File("C:/tmp/test"));
    }

}