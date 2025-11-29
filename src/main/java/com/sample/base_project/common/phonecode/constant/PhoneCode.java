package com.sample.base_project.common.phonecode.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

@Component
public class PhoneCode {

    @Getter
    private static final LinkedHashMap<String, String> map = new LinkedHashMap<>();

    @Autowired
    public void init() {
        ClassPathResource resource = new ClassPathResource("phone/phone.csv");
        try (InputStream inputStream = resource.getInputStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split("\\|");
                    if (split.length == 3) {
                        map.put(line, split[2]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
