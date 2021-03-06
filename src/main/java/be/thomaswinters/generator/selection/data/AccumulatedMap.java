package be.thomaswinters.generator.selection.data;

import be.thomaswinters.generator.selection.Weighted;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;

import java.util.NavigableMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccumulatedMap<E> {
    private final double totalSize;
    private final NavigableMap<Double, E> accumulatedMap;

    public AccumulatedMap(Stream<? extends Weighted<? extends E>> stream) {
        ImmutableSortedMap.Builder<Double, E> b =
                new ImmutableSortedMap.Builder<>(Ordering.natural());
        double currentTotal = 0d;
        for (Weighted<? extends E> entry : stream.collect(Collectors.toList())) {
            if (entry.getWeight() > 0) {
                currentTotal += entry.getWeight();
                b.put(currentTotal, entry.getElement());
            }
        }

        // Initialise
        this.accumulatedMap = b.build();
        this.totalSize = currentTotal;
    }

    public E get(Double index) {
        return accumulatedMap.ceilingEntry(index).getValue();
    }

    public double getTotalSize() {
        return totalSize;
    }

    @Override
    public String toString() {
        return "AccumulatedMap{" +
                "totalSize=" + totalSize +
                ", accumulatedMap=" + accumulatedMap +
                '}';
    }
}
