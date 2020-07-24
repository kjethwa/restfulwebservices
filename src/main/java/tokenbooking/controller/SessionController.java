package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.ClientAndSessionDetails;
import tokenbooking.model.SessionDetails;
import tokenbooking.repository.ClientNameAndId;
import tokenbooking.service.ClientService;
import tokenbooking.service.SessionService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController()
public class SessionController {

    @Autowired
    SessionService sessionService;

    @Autowired
    ClientService clientService;

    @CrossOrigin(origins = "http://localhost:4201")
    @RequestMapping(value = "/enduserapi/clientname", method = RequestMethod.GET)
    public List<ClientNameAndId> getListOfAllActiveClient() {
        return clientService.getListOfAllActiveClients();
    }

    @CrossOrigin(origins = "http://localhost:4201")
    @RequestMapping(value = "/enduserapi/clients/{clientId}/sessions", method = RequestMethod.GET)
    public ClientAndSessionDetails getAllActiveSessionsOfClient(@PathVariable UUID clientId, Principal principal) {
        try {
            if (StringUtils.isEmpty(principal)) {
                throw new Exception("Invalid request");
            }
                return sessionService.getSessionDetailsOfClientWithClientNameAndAddressSummary(clientId, principal.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/enduserapi/sessions/{sessionId}/nextAvailableToken", method = RequestMethod.GET)
    public Integer getNextAvailableToken(@PathVariable UUID sessionId) {
        try {
            return sessionService.getNextAvailableToken(sessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/sessions", method = RequestMethod.POST)
    public SessionDetails saveSessionDetails(@RequestBody SessionDetails sessionDetails) {
        return sessionService.save(sessionDetails);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/sessions/{sessionId}", method = RequestMethod.GET)
    public SessionDetails getSession(@PathVariable UUID sessionId) {
        return sessionService.getSessionDetails(sessionId);
    }
}
