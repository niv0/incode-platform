package org.incode.module.unittestsupport.dom.with;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;

import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;


public abstract class WithFieldUniqueContractTestAllAbstract<T> {

    private final Class<T> interfaceType;
    private final String fieldName;
    private final String prefix;

    public WithFieldUniqueContractTestAllAbstract(Class<T> interfaceType, String fieldName, final String prefix) {
        this.interfaceType = interfaceType;
        this.fieldName = fieldName;
        this.prefix = prefix;
    }
    
    @Test
    public void searchAndTest() {

        final Reflections reflections = new Reflections(prefix);
        final StringBuilder buf = new StringBuilder();

        Set<Class<? extends T>> domainObjectClasses = reflections.getSubTypesOf(interfaceType);
        checkClass:
        for (final Class<? extends T> subtype : domainObjectClasses) {
            if(subtype.isInterface() || subtype.isAnonymousClass() || subtype.isLocalClass() || subtype.isMemberClass()) {
                // skip (probably a testing class)
                continue;
            }
            
            try {
                subtype.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // assume inherited field, so skip.
                continue;
            }
            
            final Annotation[] annotations = subtype.getAnnotations();
            
            // @Unique(...)
            for (Annotation annotation : annotations) {
                if(annotation instanceof Unique) {
                    final Unique unique = (Unique) annotation;
                    if(declaresUniquenesForField(subtype, unique, fieldName)) {
                        continue checkClass;
                    }
                }
            }

            // @Uniques({ @Unique(...), @Unique(...), ... })
            for (Annotation annotation : annotations) {
                if(annotation instanceof Uniques) {
                    Uniques uniques = (Uniques) annotation;
                    for (Unique unique : uniques.value()) {
                        if(declaresUniquenesForField(subtype, unique, fieldName)) {
                            continue checkClass;
                        }
                    }
                }
            }

            buf.append("\n" + subtype.getName() + " is not annotated with @Unique(members={\"" + fieldName + "\"})");
        }
        if(buf.length() > 0) {
            Assert.fail(buf.toString());
        }
    }

    private static <T> boolean declaresUniquenesForField(final Class<? extends T> subtype, final Unique unique, String fieldName) {
        final String[] members = unique.members();
        return members != null && members.length == 1 && members[0].equals(fieldName);
    }

}
