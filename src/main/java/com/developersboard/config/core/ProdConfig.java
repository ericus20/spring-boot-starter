package com.developersboard.config.core;

import com.developersboard.constant.ProfileTypeConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * This class provides every bean, and other configurations needed to be used in the production
 * phase.
 *
 * @author Matthew Puentes
 * @version 1.0
 * @since 1.0
 */
@Configuration
@Profile(ProfileTypeConstants.PROD)
public class ProdConfig {}
