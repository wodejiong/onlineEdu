package com.xuecheng.manage_course;

import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRibbon {

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testRibbon() {
        String serviceId = "XC-SERVICE-MANAGE-CMS";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://"+serviceId+"/cms/get/5a92141cb00ffc5a448ff1a0", Map.class);
        Map body = forEntity.getBody();
        System.out.println(body);
    }
}
