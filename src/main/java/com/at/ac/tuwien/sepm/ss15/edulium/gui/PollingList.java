package com.at.ac.tuwien.sepm.ss15.edulium.gui;

import javafx.collections.ObservableListBase;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Supplier;

/**
 * A observable list which periodically updates its elements when a element supplier is set
 */
public class PollingList<E> extends ObservableListBase<E> {
    private final List<E> elements = new ArrayList<>();

    private static TaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledFuture = null;
    private Supplier<List<E>> supplier = null;
    private long interval = 0;

    /**
     * Task which gets periodically called by the task scheduler to update the elements.
     */
    private class UpdateTask implements Runnable {
        @Override
        public void run() {
            assert supplier != null;

            Set<E> suppliedElements = new HashSet<>(supplier.get());
            if (suppliedElements == null) {
                return;
            }

            beginChange();

            // remove elements
            for (int index = elements.size() - 1; index >= 0; index--) {
                E element = elements.get(index);

                if (!suppliedElements.contains(element)) {
                    nextRemove(index, element);
                    elements.remove(index);
                }
            }

            // add new elements
            suppliedElements.removeAll(elements); // remove all unchanged elements -> items to add
            nextAdd(elements.size(), elements.size() + suppliedElements.size());  // [start index, end index[
            elements.addAll(suppliedElements);

            endChange();
        }
    }

    public PollingList(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    /**
     * Sets the data supplier for the polling list. The polling list will periodically invoke supplier.get (when polling
     * is enabled) to update its elements.
     * The polling task will be restarted if polling is already running.
     * @param supplier An Supplier object which returns a list of elements (polling can only be started if supplier is not null)
     */
    public void setSupplier(Supplier<List<E>> supplier) {
        this.supplier = supplier;

        if (isPolling()) {
            restartPolling();
        }
    }

    /**
     * Sets the update interval for the polling list.
     * The polling task will be restarted if polling is already running.
     * @param interval Polling interval in milliseconds (polling can only be started if interval is bigger than 0)
     */
    public void setInterval(long interval) {
        this.interval = interval;

        if (isPolling()) {
            restartPolling();
        }
    }

    /**
     * Starts the polling of elements if a valid supplier is set and the update interval is bigger than 0.
     */
    public void startPolling() {
        if (supplier != null && interval > 0) {
            scheduledFuture = taskScheduler.scheduleAtFixedRate(new DelegatingSecurityContextRunnable(new UpdateTask()), interval);
        }
    }

    /**
     * Stops the polling
     */
    public void stopPolling() {
        scheduledFuture.cancel(true);
        scheduledFuture = null;
    }

    /**
     * @return True if the polling task is running, false otherwise
     */
    public boolean isPolling() {
        return scheduledFuture != null;
    }

    private void restartPolling() {
        stopPolling();
        startPolling();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public E get(int index) {
        return elements.get(index);
    }
}
