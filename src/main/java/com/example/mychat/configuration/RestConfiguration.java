package com.example.mychat.configuration;

import com.example.mychat.entity.ChatRoom;
import com.example.mychat.entity.Media;
import com.example.mychat.entity.Message;
import com.example.mychat.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {


    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Media.class);
        config.exposeIdsFor(Message.class);
        config.exposeIdsFor(ChatRoom.class);
        config.exposeIdsFor(User.class);

    }
}
