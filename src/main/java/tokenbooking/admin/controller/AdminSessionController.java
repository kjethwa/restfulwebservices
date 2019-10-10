package tokenbooking.admin.controller;

import tokenbooking.admin.exception.AdminException;
import tokenbooking.admin.model.ResponseMessage;
import tokenbooking.admin.model.ResponseStatus;
import tokenbooking.admin.model.TokenInfo;
import tokenbooking.admin.service.AdminSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.AdminSessionSummary;

import java.util.List;

@RestController
public class AdminSessionController {

    @Autowired
    AdminSessionService adminSessionService;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/admin/startsession/{sessionId}", method = RequestMethod.GET)
    public ResponseMessage startSession(@PathVariable Long sessionId) {
        try {
            TokenInfo tokenInfo = adminSessionService.startSession(sessionId);
            return new ResponseMessage(tokenInfo, ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/admin/sessions/{clientId}", method = RequestMethod.GET)
    public ResponseMessage getAllSessionsOfClient(@PathVariable Long clientId) {
        try {
            List<AdminSessionSummary> adminSessionSummaries = adminSessionService.getAllSessionDetails(clientId);
            return new ResponseMessage(adminSessionSummaries, ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/admin/nexttoken/{sessionId}", method = RequestMethod.GET)
    public ResponseMessage nextToken(@PathVariable Long sessionId) {
        try {
            TokenInfo tokenInfo = adminSessionService.getNextToken(sessionId);
            return new ResponseMessage(tokenInfo, ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }
}
