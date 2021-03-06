package be.thomaswinters.generator.streamgenerator;

import be.thomaswinters.generator.generators.IGenerator;
import be.thomaswinters.generator.selection.ISelector;
import be.thomaswinters.generator.selection.RandomUniqueSelector;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@FunctionalInterface
public interface IStreamGenerator<E> {
    Stream<E> generateStream();

    // TO ISTREAMGENERATOR
    default <F> IStreamGenerator<F> mapStream(Function<Stream<E>, Stream<F>> mapper) {
        return new StreamMappingGenerator<>(this, mapper);
    }

    default <F> IStreamGenerator<F> map(Function<E, F> operator) {
        return mapStream(stream -> stream.map(operator));
    }

    default <F> IStreamGenerator<F> flatMap(Function<E, Stream<F>> operator) {
        return mapStream(stream -> stream.flatMap(operator));
    }

    default IStreamGenerator<E> filter(Predicate<E> operator) {
        return mapStream(stream -> stream.filter(operator));
    }

    default IStreamGenerator<E> distinct() {
        return mapStream(Stream::distinct);
    }

    default IStreamGenerator<E> peek(Consumer<E> consumer) {
        return mapStream(stream -> stream.peek(consumer));
    }

    default IStreamGenerator<E> sort(Comparator<E> comparator) {
        return mapStream(stream -> stream.sorted(comparator));
    }

    default IStreamGenerator<E> limit(int numberOfGenerations) {
        return () -> generateStream()
                .limit(numberOfGenerations);
    }

    // TO IGENERATOR
    default IGenerator<E> reduceToGenerator(Function<Stream<E>, Optional<E>> reducer) {
        return new StreamToGeneratorAdaptor<>(this, reducer);
    }

    default IGenerator<E> reduceToGenerator() {
        return reduceToGenerator(new RandomUniqueSelector<>());
    }

    default IGenerator<E> reduceToGenerator(ISelector<E> selector) {
        return () -> selector.select(this.generateStream());
    }


    default IGenerator<E> findFirst() {
        return reduceToGenerator(Stream::findFirst);
    }

    default IGenerator<E> max(Comparator<E> comparator) {
        return reduceToGenerator(stream -> stream.max(comparator));
    }

    default IGenerator<E> min(Comparator<E> comparator) {
        return reduceToGenerator(stream -> stream.min(comparator));
    }


    default IStreamGenerator<E> makeInfinite() {
        return () -> Stream.generate(this::generateStream).flatMap(e -> e);
    }
}
