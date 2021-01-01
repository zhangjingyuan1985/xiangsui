package com.sutpc.transpaas.algoserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  /**
   * 创建API.
   *
   * @return Docket
   */
  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.sutpc.transpaas.algoserver"))
        .paths(PathSelectors.any())
        .build();
  }

  /**
   * apiInfo.
   *
   * @return ApiInfo
   */
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("超级模型-算法服务")
        .version("1.0")
        .build();
  }
}
