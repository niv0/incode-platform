package org.isisaddons.module.publishmq.dom.statusclient;

public class StringValue {
    public final String value;
    public StringValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
