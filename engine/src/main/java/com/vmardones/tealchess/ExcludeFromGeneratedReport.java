/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess;

import java.lang.annotation.*;

/**
 * Custom annotation used for marking private constructors in utility classes.
 * Marking these makes JaCoCo skip including them in code coverage reports.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface ExcludeFromGeneratedReport {}
