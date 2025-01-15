package com.ohgiraffers.ukki.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InquiryState {
    CHECK("확인완료"),
    PROCESSING("처리중"),
    COMPLETE("처리완료");

    private String inquiryState;

    InquiryState(String inquiryState) {
        this.inquiryState = inquiryState;
    }

    public String getInquiryState() {
        return inquiryState;
    }

    @JsonValue
    public String toJson() {
        return this.inquiryState;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public static InquiryState fromValue(String value) {
        for (InquiryState state : InquiryState.values()) {
            if (state.getInquiryState().equals(value) || state.name().equals(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown enum type " + value);
    }
}
