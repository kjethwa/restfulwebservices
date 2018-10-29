package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tokenbooking.model.Client;
import tokenbooking.model.ClientSearchDetails;
import tokenbooking.service.ClientService;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ClientSearchController {

    @Autowired
    ClientService clientService;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/searchClients",method = RequestMethod.GET)
    public List<ClientSearchDetails> getClientSearchResult(@PathParam(value = "search") String search) {
        return new ArrayList<>(clientService.getClientSearchResult(search));
    }
}
