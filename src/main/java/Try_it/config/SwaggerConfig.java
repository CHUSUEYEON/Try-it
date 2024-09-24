package Try_it.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
@OpenAPIDefinition(
    info = @Info(
        title = "Try-it API Docs",
        description = """
                [츄라잇] 스웨거 페이지입니다. 설명란에 예시 json 을 이용하여 편리하게 테스트 하실 수 있습니다.\s
                \s
                테스트 방법\s
                1. 로그인 하기(Auth에 있습니다.)\s
                2. 로그인하여 받은 token 값 복사하여 원하는 API 우측 상단의 자물쇠 모양 클릭하여 복사한 토큰값 넣어서 로그인 상태로 만들기\s
                3. 예시문 참고하여 직접 작성 혹은 예시문 그대로 넣어 결과값 확인하며 API 작동 여부 확인하기
            """,
        version = "v1"
    )
)
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components()
            .addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));
        return new OpenAPI()
            .addSecurityItem(securityRequirement)
            .components(components);
    }

}
