package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.Client;
import tokenbooking.service.ClientService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    ClientService clientService;

    @GetMapping
    public List<Client> getAllClients(){
        return clientService.getAllClients();
    }

    @GetMapping("{clientId}")
    public Client getClientById(@PathVariable Long clientId){
        return clientService.getClientById(clientId);
    }

    @PostMapping
    public void addClient(@RequestBody Client client){
        clientService.addClient(client);
    }

    @PutMapping
    public void updateClient(@RequestBody Client client){
        clientService.updateClient(client);
    }

}
