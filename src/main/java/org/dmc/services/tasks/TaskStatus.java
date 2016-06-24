package org.dmc.services.tasks;

public class TaskStatus {

    public static enum StatusValues {
        UNKNOWN(0, "Unknown"),
        OPEN (1, "Open"),
        INPROGRESS (2, "InProgress"),
        CLOSED (3, "Closed");
        private final int value;
        private final String name;
        StatusValues(int value, String name) {
            this.value = value;
            this.name = name;
        }
        public int getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
    };

}
