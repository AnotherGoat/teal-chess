/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess;

import java.lang.annotation.*;

/**
 * Custom annotation used for marking test classes to exclude from NullAway linting.
 * Every test class should have this annotation.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcludeFromNullAway {}
