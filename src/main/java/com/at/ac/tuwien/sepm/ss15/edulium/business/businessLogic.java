package com.at.ac.tuwien.sepm.ss15.edulium.business;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generic BusinessLogic interface
 */

@org.springframework.stereotype.Service
@Transactional(propagation = Propagation.REQUIRED)
public interface BusinessLogic {
}