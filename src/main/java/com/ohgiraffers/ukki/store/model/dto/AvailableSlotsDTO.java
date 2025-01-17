package com.ohgiraffers.ukki.store.model.dto;

public class AvailableSlotsDTO {
    private int availableSlots;

    public AvailableSlotsDTO(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    @Override
    public String toString() {
        return "AvailableSlotsDTO{" +
                "availableSlots=" + availableSlots +
                '}';
    }
}
