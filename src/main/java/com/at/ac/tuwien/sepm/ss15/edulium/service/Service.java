package com.at.ac.tuwien.sepm.ss15.edulium.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic Service interface
 */
@org.springframework.stereotype.Service
@PreAuthorize("isAuthenticated()")
@Transactional(propagation = Propagation.REQUIRED)
public interface Service {
}
