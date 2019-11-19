package br.com.ottimizza.application.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.domain.responses.GenericResponse;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import br.com.ottimizza.application.services.InvitationService;

@RestController // @formatter:off
@RequestMapping("/api/v1/invites")
public class InviteController {

    @Inject
    InvitationService invitationService;

    @GetMapping
    public ResponseEntity<?> fetch(@RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
                                   @RequestParam(name = "page_size", defaultValue = "10") int pageSize, 
                                   Principal principal) throws Exception {
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<?> invite(@RequestBody UserOrganizationInvite inviteDetails, Principal principal) throws Exception {
        return ResponseEntity.ok(new GenericResponse<UserOrganizationInvite>(
            invitationService.invite(inviteDetails, principal)
        ));
    }

}