package tokenbooking.admin.controller;

import tokenbooking.admin.exception.AdminException;
import tokenbooking.admin.model.ResponseMessage;
import tokenbooking.admin.model.ResponseStatus;
import tokenbooking.admin.model.TokenInfo;
import tokenbooking.admin.service.AdminSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.AdminSummary;

import java.security.Principal;

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

    @GetMapping(value = "/admin/sessions")
    public ResponseMessage getAllSessionsOfClient(Principal principal) {
        try {
            if(principal == null){
                throw new AdminException("Inactive inactive");
            }
            AdminSummary adminSummary = adminSessionService.getAllSessionDetails(principal.getName());
            return new ResponseMessage(adminSummary, ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }
    @GetMapping(value = "/admin/sessions/today")
    public ResponseMessage getTodaySessionsOfClient(Principal principal) {
        try {
            if(principal == null){
                throw new AdminException("Inactive inactive");
            }
            AdminSummary adminSummary = adminSessionService.getAllSessionDetails(principal.getName());
            return new ResponseMessage(adminSummary, ResponseStatus.SUCCESS);
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
            if (tokenInfo == null) {
                tokenInfo = adminSessionService.getNextToken(sessionId);
            }
            return new ResponseMessage(tokenInfo, ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @GetMapping(value = "/admin/finishsession/{sessionId}")
    public ResponseMessage finishSession(@PathVariable Long sessionId) {
        try {
            adminSessionService.finishSession(sessionId);
            return new ResponseMessage("Session completed successfully.", ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @GetMapping(value = "/admin/cancelsession/{sessionId}")
    public ResponseMessage cancelSession(@PathVariable Long sessionId) {
        try {
            adminSessionService.cancelSession(sessionId);
            return new ResponseMessage("Session cancelled successfully.", ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }

    @GetMapping(value = "/admin/activesession")
    public ResponseMessage getActiveSession(Principal principal) {
        try {
            if(principal == null){
                throw new AdminException("Inactive inactive");
            }
            AdminSummary adminSummary = adminSessionService.getActiveSession(principal.getName());
            return new ResponseMessage(adminSummary, ResponseStatus.SUCCESS);
        } catch (AdminException e) {
            return new ResponseMessage(e.getMessage(), ResponseStatus.FAILURE);
        }
    }
}
