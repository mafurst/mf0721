package com.mfurst.mf0721.config;

import com.mfurst.mf0721.model.dto.Holiday;
import com.mfurst.mf0721.model.dto.Tool;
import com.mfurst.mf0721.model.dto.ToolType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * App configuration class.
 *
 * The component scan annotation tells Spring to scan the
 * project and include any services/components.
 */
@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = "com.mfurst.mf0721")
public class AppConfig {
    /**
     * Load a bean that is a list of Tools from the application yaml property file
     * @return
     */
    @Bean
    @ConfigurationProperties(value = "tool-data.tools")
    public List<Tool> tools() {
        //This will be returned if there are no matches in the property file
        return new ArrayList<>();
    }

    @Bean
    @ConfigurationProperties(value = "tool-data.tool-type")
    public List<ToolType> toolTypes() {
        //This will be returned if there are no matches in the property file
        return new ArrayList<>();
    }

    @Bean
    @ConfigurationProperties(value = "holiday")
    public List<Holiday> holidays() {
        //This will be returned if there are no matches in the property file
        return new ArrayList<>();
    }
}
