package org.dmc.services.serviceRunTests;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/upload")
public class ServiceRunFileUploadController {
	
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