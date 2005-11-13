/*
 *    
 *   Copyright 2004 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.taglibs.rdc.scxml;

/**
 * A class representing an event. Specific event types have been
 * defined in reference to SCXML.
 * 
 * @author Jaroslav Gergic
 * @author Rahul Akolkar
 */
public class TriggerEvent {
    
    /**
     * Constructor
     * 
     * @param name The event name
     * @param type The event type
     * @param payload The event payload
     */
    public TriggerEvent(String name, int type, Object payload) {
        super();
        this.name = name;
        this.type = type;
        this.payload = payload;
    }

    /**
     * Constructor
     * 
     * @param name The event name
     * @param type The event type
     */
    public TriggerEvent(String name, int type) {
        this(name, type, null);
    }

    /**
     * <code>CALL_EVENT</code>
     */
    public static final int CALL_EVENT = 1;

    /**
     * <code>CHANGE_EVENT</code>
     * 
     */
    public static final int CHANGE_EVENT = 2;

    /**
     * <code>SIGNAL_EVENT</code>
     * 
     */
    public static final int SIGNAL_EVENT = 3;

    /**
     * <code>TIME_EVENT</code>
     * 
     */
    public static final int TIME_EVENT = 4;

    /**
     * The event name
     * 
     */
    private String name;

    /**
     * The event type
     * 
     */
    private int type;

    /**
     * The event payload
     * 
     */
    private Object payload;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the payload.
     */
    public Object getPayload() {
        return payload;
    }

    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }

    /**
     * Define an equals operator for TriggerEvent
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof TriggerEvent) {
            TriggerEvent te2 = (TriggerEvent) obj;
            if (type == te2.type && name.equals(te2.name)
                && ((payload == null && te2.payload == null) 
                     || (payload != null && payload.equals(te2.payload)))) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
}