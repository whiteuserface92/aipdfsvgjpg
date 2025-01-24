import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        System.out.println("multipartConfigElement");
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(100));  // 파일 크기 제한 (10MB)
        factory.setMaxRequestSize(DataSize.ofMegabytes(100));  // 전체 요청 크기 제한 (10MB)
        return factory.createMultipartConfig();
    }
}