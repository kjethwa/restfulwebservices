package tokenbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tokenbooking.model.ClientOperation;
import tokenbooking.repository.ClientOperationRepository;

@Service
public class ClientOperationService {

    @Autowired
    private ClientOperationRepository clientOperationRepository;

    public ClientOperation getClientOperation(Long operationId) {
        return clientOperationRepository.findOne(operationId);
    }
}
