package net.deimos.api.i;

import net.deimos.api.mods.Category;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
    String name();
    String description();
    Category category();
}
