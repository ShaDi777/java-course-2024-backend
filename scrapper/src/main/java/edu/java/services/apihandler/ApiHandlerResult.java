package edu.java.services.apihandler;

import lombok.With;

@With
public record ApiHandlerResult(
    boolean hasUpdate,
    String description
) {
    public static ApiHandlerResult getDefault() {
        return new ApiHandlerResult(false, null);
    }
}
