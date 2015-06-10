package com.at.ac.tuwien.sepm.ss15.edulium.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic Service interface
 */
@org.springframework.stereotype.Service
@Transactional(propagation = Propagation.REQUIRED)
public interface Service {
}
