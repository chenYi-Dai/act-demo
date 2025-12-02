//package com.lhj.workflow.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
///***
// * @description swagger配置
// * @author LHJ
// * @date 2020/2/27 18:09
// */
//@EnableSwagger2
//@Configuration
//public class SwaggerConfig {
//    @Value(value = "${swagger.enabled}")
//    Boolean swaggerEnabled;
//
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
//                // 是否开启
//                .enable(swaggerEnabled).select()
//                .apis(RequestHandlerSelectors.basePackage("com.lhj.workflow.controller"))
//                .paths(PathSelectors.any()).build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Activiti7流程")
//                .description("Activiti7流程演示")
//                // 作者信息
//                .contact(new Contact("lhj", "", "754491577@qq.com"))
//                .version("1.0.0")
//                .build();
//    }
//}
