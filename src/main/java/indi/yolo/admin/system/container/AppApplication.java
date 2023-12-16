package indi.yolo.admin.system.container;

import cn.hutool.crypto.SecureUtil;
import com.mybatisflex.core.datasource.DataSourceDecipher;
import com.mybatisflex.core.datasource.DataSourceProperty;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.nio.charset.StandardCharsets;


@EnableCaching
@MapperScan("indi.yolo.admin.system.modules.*.mapper")
@ComponentScan(basePackages = "indi.yolo.admin.system")
@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    public DataSourceDecipher decipher() {
        final byte[] keys = "0123456789ABHAEQ".getBytes(StandardCharsets.UTF_8);
        return (property, value) -> {
            if (property == DataSourceProperty.USERNAME || property == DataSourceProperty.PASSWORD) {
                return SecureUtil.aes(keys).decryptStr(value);
            }
            return value;
        };
    }

}
