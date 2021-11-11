package org.tretton63.wstest.appevents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

public class OnHejEvent extends ApplicationEvent {
    private static final Logger logger = LoggerFactory.getLogger(OnHejEvent.class);

    public OnHejEvent(Object source) {
        super(source);
        logger.info("OnHej was reached");
    }
}
