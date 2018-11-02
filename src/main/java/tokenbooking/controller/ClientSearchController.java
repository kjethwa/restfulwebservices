package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.Client;
import tokenbooking.model.ClientSearchDetails;
import tokenbooking.service.ClientService;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController("/searchClients")
public class ClientSearchController {

    @Autowired
    ClientService clientService;

    @GetMapping
    public List<ClientSearchDetails> getClientSearchResult(@PathParam(value = "search") String search) {
        return new ArrayList<>(clientService.getClientSearchResult(search));
    }
}
