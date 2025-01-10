package com.ohgiraffers.ukki.user.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class DirectionsRequest {
    private Map<String, Double> origin;
    private Map<String, Double> destination;
    private List<Map<String, Double>> waypoints;

    // Getter 및 Setter
    public Map<String, Double> getOrigin() {
        return origin;
    }

    public void setOrigin(Map<String, Double> origin) {
        this.origin = origin;
    }

    public Map<String, Double> getDestination() {
        return destination;
    }

    public void setDestination(Map<String, Double> destination) {
        this.destination = destination;
    }

    public List<Map<String, Double>> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Map<String, Double>> waypoints) {
        this.waypoints = waypoints;
    }

    // Convert the object to JSON string
    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
