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
@CrossOrigin(origins = "http://localhost:4201", maxAge = 3600)
public class AdminSessionController {

    @Autowired
    AdminSessionService adminSessionService;

    @GetMapping(value = "/admin/startsession/{sessionId}")
    public ResponseMessage startSession(@PathVariable Long sessionId) {
        try {
            adminSessionService.startSession(sessionId);
            return new ResponseMessage("Session started successfully", ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @GetMapping(value = "/admin/sessions/{clientId}")
    public ResponseMessage getAllSessionsOfClient(@PathVariable Long clientId) {
        try {
            List<AdminSessionSummary> adminSessionSummaries = adminSessionService.getAllSessionDetails(clientId);
            return new ResponseMessage(adminSessionSummaries, ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @GetMapping(value = "/admin/nexttoken/{sessionId}")
    public ResponseMessage nextToken(@PathVariable Long sessionId) {
        try {
            TokenInfo tokenInfo = adminSessionService.getNextToken(sessionId);
            return new ResponseMessage(tokenInfo, ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @GetMapping(value = "/admin/lasttoken/{sessionId}")
    public ResponseMessage lastToken(@PathVariable Long sessionId) {
        try {
            TokenInfo tokenInfo = adminSessionService.getLastToken(sessionId);
            return new ResponseMessage(tokenInfo, ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @GetMapping(value = "/admin/completesession/{sessionId}")
    public ResponseMessage completeSession(@PathVariable Long sessionId) {
        try {
            adminSessionService.completeSession(sessionId);
            return new ResponseMessage("Session completed successfully.", ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @GetMapping(value = "/admin/activesession/{clientId}")
    public ResponseMessage getActiveSession(@PathVariable Long clientId) {
        try {
            AdminSessionSummary adminSessionSummary = adminSessionService.getActiveSession(clientId);
            return new ResponseMessage(adminSessionSummary, ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }
}
