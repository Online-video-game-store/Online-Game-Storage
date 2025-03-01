package mr.demonid.gateway.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "application")
public class AppConfig {
    private String loginUri;
    private String logoutUri;

    public String getLoginUri() {
        return loginUri;
    }

    public void setLoginUri(String loginUri) {
        this.loginUri = loginUri;
    }

    public String getLogoutUri() {
        return logoutUri;
    }

    public void setLogoutUri(String logoutUri) {
        this.logoutUri = logoutUri;
    }


    @Override
    public String toString() {
        return "AppConfig{" +
                "loginUri='" + loginUri + '\'' +
                ", logoutUri='" + logoutUri + '\'' +
                '}';
    }
}
