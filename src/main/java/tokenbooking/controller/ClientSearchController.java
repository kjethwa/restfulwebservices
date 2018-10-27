package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tokenbooking.model.Client;
import tokenbooking.service.ClientService;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ClientSearchController {

    @Autowired
    ClientService clientService;

    @RequestMapping(value = "/searchClients",method = RequestMethod.GET)
    public List<Client> getClientSearchResult(@PathParam(value = "search") String search) {
        return new ArrayList<>(clientService.getClientSearchResult(search));
    }
}
