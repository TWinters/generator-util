package be.thomaswinters.generator.relatedgenerator;

import java.util.Optional;

public abstract class RelatedGeneratorDecorator<E,F> implements IRelatedGenerator<E,F> {
    private IRelatedGenerator<E,F> innerGenerator;

    public RelatedGeneratorDecorator(IRelatedGenerator<E,F> innerGenerator) {
        this.innerGenerator = innerGenerator;
    }

    protected IRelatedGenerator<E,F> getInnerGenerator() {
        return innerGenerator;
    }

    @Override
    public Optional<E> generateRelated(F input) {
        return innerGenerator.generateRelated(input);
    }

    @Override
    public Optional<E> generate() {
        return innerGenerator.generate();
    }
}
