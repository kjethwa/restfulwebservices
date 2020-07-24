package tokenbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tokenbooking.model.ClientOperation;
import tokenbooking.repository.ClientOperationRepository;

import java.util.UUID;

@Service
public class ClientOperationService {

    @Autowired
    private ClientOperationRepository clientOperationRepository;

    public ClientOperation getClientOperation(UUID operationId) {
        return clientOperationRepository.findById(operationId).get();
    }
}
