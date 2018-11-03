package tokenbooking.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties
public class ClientCategoryProperties {
    private List<String> clientCategory;

    public List<String> getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(List<String> clientCategory) {
        this.clientCategory = clientCategory;
    }
}
