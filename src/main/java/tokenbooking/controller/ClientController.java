package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.Client;
import tokenbooking.repository.ClientNameAndId;
import tokenbooking.service.ClientService;

import java.util.List;

@RestController
public class ClientController {

    @Autowired
    ClientService clientService;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/clients/{clientId}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Client getClientById(@PathVariable Long clientId) {
        return clientService.getClientById(clientId);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/clients", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addClient(@RequestBody Client client) {
        clientService.addClient(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/clients", method = RequestMethod.PUT)
    public void updateClient(@RequestBody Client client) {
        clientService.updateClient(client);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/clientname", method = RequestMethod.GET)
    public List<ClientNameAndId> getListOfAllActiveClient() {
        return clientService.getListOfAllActiveClients();
    }

}
