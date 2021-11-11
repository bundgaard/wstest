package org.tretton63.wstest.interfaces.rest;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Role;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tretton63.wstest.appevents.OnHejEvent;

import javax.annotation.security.RolesAllowed;

@RestController
public class ProgramController {

    private static final Logger logger = LoggerFactory.getLogger(ProgramController.class);
    private CommandGateway commandGateway;
    private QueryGateway queryGateway;
    private ApplicationEventPublisher eventPublisher;

    public ProgramController(CommandGateway commandGateway,
                             QueryGateway queryGateway,
                             ApplicationEventPublisher eventPublisher) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/program/")
    public void start() {

    }

    @GetMapping("/program/hej")
    @RolesAllowed("USER")
    public void hej(Authentication authentication) {
        logger.info("Principal {}",  authentication.getPrincipal());
        logger.info("Details {}", authentication.getDetails());
        eventPublisher.publishEvent(new OnHejEvent("Hej hej"));
    }
}
