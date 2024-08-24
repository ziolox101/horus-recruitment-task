package pl.ziolkowski.dawid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Waladwawl implements Structure {
    private final List<Block> blocks;

    public Wall(final List<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public Optional<Block> findBlockByColor(final String color) {
        return getAllBlocks()
                .filter(block -> block.getColor().equals(color))
                .findFirst();
    }

    @Override
    public List<Block> findBlocksByMaterial(final String material) {
        return getAllBlocks()
                .filter(block -> block.getMaterial().equals(material))
                .toList();
    }

    @Override
    public int count() {
        return (int) getAllBlocks().count();
    }

    private Stream<Block> getAllBlocks() {
        return blocks.stream().flatMap(this::flattenBlocks);
    }

    private Stream<Block> flattenBlocks(final Block block) {
        return Stream.of(block); // CompositeBlock treated as single entity, the mix of blocks in composite creates one block
    }

//    private Stream<Block> flattenBlocks(final Block block) { //case when CompositeBlocks are not treated as single entity
//        if (block instanceof CompositeBlock compositeBlock) {
//            return compositeBlock.getBlocks().stream().flatMap(this::flattenBlocks);
//        }
//        return Stream.of(block);
//    }
}
