package com.jonatas.apitabelatarifaria;

import com.jonatas.apitabelatarifaria.config.ContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import(ContainersConfig.class)
@SpringBootTest
class ApiTabelaTarifariaApplicationIT {

    @Test
    void contextLoads() {
    }

}
