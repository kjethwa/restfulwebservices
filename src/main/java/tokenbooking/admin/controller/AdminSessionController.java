package tokenbooking.admin.controller;

import tokenbooking.admin.model.TokenInfo;
import tokenbooking.admin.service.AdminSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminSessionController {

    @Autowired
    AdminSessionService adminSessionService;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/startsession/{sessionId}", method = RequestMethod.GET)
    public TokenInfo startSession(@PathVariable Long sessionId) throws Exception {
        adminSessionService.startSession(sessionId);
        return nextToken(sessionId);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/nexttoken/{sessionId}", method = RequestMethod.GET)
    public TokenInfo nextToken(@PathVariable Long sessionId) throws Exception {
        return adminSessionService.getNextToken(sessionId);
    }
}
