package tokenbooking.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import tokenbooking.model.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public ClientSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public ClientSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Client> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Client>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new ClientSpecification(param));
        }

        Specification<Client> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}