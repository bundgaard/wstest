package org.tretton63.wstest.domain;

import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;


@Aggregate
public class Program {

    @AggregateIdentifier
    private UUID programId;

    Program() {}




}
