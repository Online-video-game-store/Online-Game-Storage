package mr.demonid.service.catalog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.BinderCustomizer;
import org.springframework.cloud.stream.binder.rabbit.RabbitMessageChannelBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.AbstractMessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@Configuration
public class StreamConfig {

    private final JwtChannelInterceptor jwtInterceptor;


    public StreamConfig(JwtChannelInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Bean
    public MessageChannel inputIn0Channel(JwtChannelInterceptor interceptor) {
        DirectChannel channel = new DirectChannel();
        channel.addInterceptor(interceptor);
        return channel;
    }

//    @Bean
//    public ChannelInterceptor jwtValidationInterceptor(JwtChannelInterceptor interceptor) {
//        return interceptor;
//    }


    /**
     * Очистка потоков в асинхронном режиме.
     */
    @Bean
    public ExecutorChannelInterceptor securityContextCleaner() {
        return new ExecutorChannelInterceptor() {
            @Override
            @Nullable
            public void afterMessageHandled(@NonNull Message<?> message, @NonNull MessageChannel channel, @NonNull MessageHandler handler, Exception ex) {
                SecurityContextHolder.clearContext();
            }
        };
    }

}