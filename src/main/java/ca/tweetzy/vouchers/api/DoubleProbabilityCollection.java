package ca.tweetzy.vouchers.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public final class DoubleProbabilityCollection<E> {

    private final List<DoubleProbabilitySetElement<E>> collection;
    private final Random random = new Random();

    private double totalProbability;

    public DoubleProbabilityCollection() {
        this.collection = new ArrayList<>();
        this.totalProbability = 0.0;
    }

    public int size() {
        return this.collection.size();
    }

    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    public boolean contains(E object) {
        if (object == null) {
            throw new IllegalArgumentException("Cannot check if null object is contained in this collection");
        }

        return this.collection.stream().anyMatch(entry -> entry.getObject().equals(object));
    }

    public Iterator<DoubleProbabilitySetElement<E>> iterator() {
        return this.collection.iterator();
    }

    public void add(E object, double probability) {
        if (object == null) {
            throw new IllegalArgumentException("Cannot add null object");
        }

        if (probability <= 0) {
            throw new IllegalArgumentException("Probability must be greater than 0");
        }

        DoubleProbabilitySetElement<E> entry = new DoubleProbabilitySetElement<>(object, probability);
        this.collection.add(entry);
        this.totalProbability += probability;
    }

    public boolean remove(E object) {
        if (object == null) {
            throw new IllegalArgumentException("Cannot remove null object");
        }

        Iterator<DoubleProbabilitySetElement<E>> it = this.iterator();
        boolean removed = false;

        while (it.hasNext()) {
            DoubleProbabilitySetElement<E> entry = it.next();
            if (entry.getObject().equals(object)) {
                this.totalProbability -= entry.getProbability();
                it.remove();
                removed = true;
            }
        }

        return removed;
    }

    public void clear() {
        this.collection.clear();
        this.totalProbability = 0.0;
    }

    public E get() {
        if (this.isEmpty()) {
            throw new IllegalStateException("Cannot get an object out of an empty collection");
        }

        double randomValue = this.random.nextDouble() * this.totalProbability;

        double cumulativeProbability = 0.0;
        for (DoubleProbabilitySetElement<E> entry : this.collection) {
            cumulativeProbability += entry.getProbability();
            if (randomValue <= cumulativeProbability) {
                return entry.getObject();
            }
        }

        // This should never happen due to the nature of the loop
        throw new RuntimeException("Failed to select an object");
    }

    public double getTotalProbability() {
        return this.totalProbability;
    }

    public static class DoubleProbabilitySetElement<T> {
        private final T object;
        private final double probability;

        public DoubleProbabilitySetElement(T object, double probability) {
            this.object = object;
            this.probability = probability;
        }

        public T getObject() {
            return this.object;
        }

        public double getProbability() {
            return this.probability;
        }
    }
}
