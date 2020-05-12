package com.capitalone.dashboard.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("PMD.AvoidStringBufferField")
public class KeyValueLog {
    
    private static final char SEPERATOR = ' ';
    private static final char EQUALS = '=';
    private static final char QUOTE = '"';
    
    private StringBuilder builder = new StringBuilder();
    
    private Map<String, Object> attributes = new LinkedHashMap<>();
    
    public KeyValueLog with(String key, Object value) {
        attributes.put(key, value);
        return this;
    }
    
    @Override
    public String toString() {
        
        Set<String> keySet = attributes.keySet();
        builder.append(keySet.stream().map(key -> key + EQUALS + QUOTE + attributes.get(key) + QUOTE + SEPERATOR).collect(Collectors.joining()));
        
        return builder.toString().trim();
    }

}
