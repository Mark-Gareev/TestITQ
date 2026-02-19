package ru.gareev.utilservice.entity;

import lombok.Getter;

/**
 * immutable class to aggregate one thread result
 */
@Getter
public class ConcurrentResult {
    private final long success;
    private final long failure;
    private final long notFound;

    public ConcurrentResult(
            long success,
            long failure,
            long notFound) {
        this.success = success;
        this.failure = failure;
        this.notFound = notFound;
    }

    public ConcurrentResult merge(ConcurrentResult adding) {
        return new ConcurrentResult(
                adding.getSuccess(),
                adding.getFailure(),
                adding.getNotFound()
        );
    }
}
