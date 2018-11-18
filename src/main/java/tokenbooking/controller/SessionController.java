package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.ClientAndSessionDetails;
import tokenbooking.model.SessionDetails;
import tokenbooking.service.SessionService;

@RestController
public class SessionController {

    @Autowired
    SessionService sessionService;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/clients/{clientId}/sessions", method = RequestMethod.GET)
    public ClientAndSessionDetails getAllActiveSessionsOfClient(@PathVariable Long clientId) {
        try {
            return sessionService.getSessionDetailsOfClientWithClientNameAndAddressSummary(clientId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/sessions/{sessionId}/nextAvailableToken", method = RequestMethod.GET)
    public Integer getNextAvailableToken(@PathVariable Long sessionId){
        try {
            return sessionService.getNextAvailableToken(sessionId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

   /* @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/sessions", method = RequestMethod.POST)
    public SessionDetails saveSessionDetails(@RequestBody SessionDetails sessionDetails) {
        return sessionService.save(sessionDetails);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/sessions/{sessionId}", method = RequestMethod.GET)
    public SessionDetails getSession(@PathVariable Long sessionId) {
        return sessionService.getSessionDetails(sessionId);
    }*/
}
