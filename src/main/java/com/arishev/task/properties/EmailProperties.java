package com.arishev.task.properties;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("email")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailProperties {


     private String noReplay;
}
