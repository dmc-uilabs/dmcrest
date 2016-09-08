package org.dmc.services.data.mappers;

import org.dmc.services.config.JpaTestConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by kskronek on 9/7/2016.
 */
@Configuration
@Import(JpaTestConfig.class)
@ComponentScan(basePackages = { "org.dmc.services" }, excludeFilters = {
		@ComponentScan.Filter(value = Configuration.class) })
public class MapperTestConfig {

}
