
package com.retail.retail_order_management.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Retail Order Management API",
        version = "1.0",
        description = "REST APIs for managing customers and orders"
    )
)
public class OpenAPIConfig {
}
