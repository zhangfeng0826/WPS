
package org.n52.wps.webAdmin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;

import org.n52.wps.commons.WPSConfig;
import org.n52.wps.server.WebProcessingService;
import org.n52.wps.webadmin.ChangeConfigurationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

@Controller
@RequestMapping(value = "/" + ConfigApi.ENDPOINT)
public class ConfigApi {

    public static final String ENDPOINT = "webAdmin";

    protected static Logger LOGGER = LoggerFactory.getLogger(WebProcessingService.class);

    public ConfigApi() {
        LOGGER.info("NEW {}", this);
    }

    @RequestMapping(value = "/file", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getConfig() throws MalformedURLException, IOException {
        String configPath = WPSConfig.getConfigPath();
        String configFileString = Resources.toString(Paths.get(configPath).toUri().toURL(), Charsets.UTF_8);
        ResponseEntity<String> entity = new ResponseEntity<String>(configFileString, HttpStatus.OK);
        return entity;
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public ResponseEntity<String> setConfig(@RequestParam("serializedWPSConfiguraton") String formData) {
        LOGGER.debug("Incoming config file: {}", formData);
        ChangeConfigurationBean configurationBean = new ChangeConfigurationBean();
        configurationBean.setSerializedWPSConfiguraton(formData);
        ResponseEntity<String> entity = new ResponseEntity<String>("Saved config.", HttpStatus.OK);
        return entity;
    }

}
