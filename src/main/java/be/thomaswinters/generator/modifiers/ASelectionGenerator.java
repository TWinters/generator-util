package be.thomaswinters.generator.modifiers;

import be.thomaswinters.generator.generators.IGenerator;
import be.thomaswinters.generator.selection.ISelector;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class ASelectionGenerator<E, F extends Supplier<Optional<E>>> extends AGeneratorDecorator<E, F> implements IGenerator<E> {
    private final ISelector<E> selector;
    private final int amountOfGenerations;

    public ASelectionGenerator(F innerGenerator, int amountOfGenerations, ISelector<E> selector) {
        super(innerGenerator);
        this.amountOfGenerations = amountOfGenerations;
        this.selector = selector;
    }

    @Override
    public Optional<E> generate() {
        return select(getInnerGenerator());
    }

    protected Optional<E> select(Supplier<Optional<E>> generator) {
        return selector.select(Stream.generate(generator)
                .limit(amountOfGenerations)
                .filter(Optional::isPresent)
                .map(Optional::get));
    }

}
