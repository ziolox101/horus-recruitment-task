package pl.ziolkowski.dawid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

class WallTest {

    private static final WoodBlock WOOD_BLOCK = new WoodBlock();
    private static final MetalBlock METAL_BLOCK = new MetalBlock();
    private static final CompositeBlock BRICK_COMPOSITE_BLOCK = new BrickCompositeBlock();

    @Test
    void shouldCountBlocks() {
        //given
        final WoodBlock secondWoodBlock = new WoodBlock();
        final Wall wall = new Wall(List.of(WOOD_BLOCK, METAL_BLOCK, secondWoodBlock));

        //when
        final int result = wall.count();

        //then
        assert result == 3;
    }

    @Test
    void shouldCountCompositeBlocks() {
        //given
        final CustomCompositeBlock customCompositeBlock = new CustomCompositeBlock("mixed",
                "mixed",
                List.of(WOOD_BLOCK, WOOD_BLOCK, WOOD_BLOCK)
        );
        final Wall wall = new Wall(List.of(BRICK_COMPOSITE_BLOCK, customCompositeBlock));

        //when
        final int result = wall.count();

        //then
        assert result == 2; //as not single 5
    }

    @Test
    void shouldCountAllTypesOfBlocks() {
        //given
        final Wall wall = new Wall(List.of(METAL_BLOCK, WOOD_BLOCK, BRICK_COMPOSITE_BLOCK));

        //when
        final int result = wall.count();

        //then
        assert result == 3; //as not single 4
    }

    @ParameterizedTest
    @ValueSource(strings = {"red", "red-silver"})
    void shouldFindByColour(final String colour) {
        //given
        final Wall wall = new Wall(List.of(METAL_BLOCK, WOOD_BLOCK, BRICK_COMPOSITE_BLOCK));

        //when
        final Optional<Block> optionalResult = wall.findBlockByColor(colour);

        //then
        assert optionalResult.isPresent();
        final Block result = optionalResult.get();
        assert result.getColor().equals(colour); //when not treated as single test for red-silver will fail
    }

    @Test
    void shouldFindFirstBlockByColourWhenThereAreDuplicates() {
        //given
        final String colour = WOOD_BLOCK.getColor();
        final Block customBlock = new CustomBlock("red", "concrete");
        final Wall wall = new Wall(List.of(customBlock, METAL_BLOCK, WOOD_BLOCK, BRICK_COMPOSITE_BLOCK));

        //when
        final Optional<Block> optionalResult = wall.findBlockByColor(colour);

        //then
        assert optionalResult.isPresent();
        final Block result = optionalResult.get();
        assert result.getColor().equals(customBlock.getColor());
        assert result.getMaterial().equals(customBlock.getMaterial());
    }

    @Test
    void shouldReturnOptionalEmptyIfColorIsNotPresent() {
        //given
        final String colour = "ugly";
        final Wall wall = new Wall(List.of(METAL_BLOCK, WOOD_BLOCK, BRICK_COMPOSITE_BLOCK));

        //when
        final Optional<Block> optionalResult = wall.findBlockByColor(colour);

        //then
        assert optionalResult.isEmpty();
    }

    @Test
    void shouldFindBlockByMaterial() {
        //given
        final String material = "sand";
        final CustomBlock customBlock = new CustomBlock("red", material);
        final CustomBlock customBlock2 = new CustomBlock("yellow", material);
        final Wall wall = new Wall(List.of(customBlock, customBlock2, BRICK_COMPOSITE_BLOCK));

        //when
        final List<Block> result = wall.findBlocksByMaterial(material);

        //then
        assert result.size() == 2;
        assert result.get(0).getMaterial().equals(material);
        assert result.get(1).getMaterial().equals(material);
    }

    @Test
    void shouldFindCompositeBlockByMaterial() {
        //given
        final String material = "sand";
        final CustomCompositeBlock customCompositeBlock = new CustomCompositeBlock("red", material, List.of(WOOD_BLOCK));
        final CustomCompositeBlock customCompositeBlock2 = new CustomCompositeBlock("yellow", material, List.of(WOOD_BLOCK, METAL_BLOCK));
        final Wall wall = new Wall(List.of(customCompositeBlock, customCompositeBlock2, BRICK_COMPOSITE_BLOCK));

        //when
        final List<Block> result = wall.findBlocksByMaterial(material);

        //then
        assert result.size() == 2;  //as not single test will fail because blocks in composite does not have sand material

        assert result.get(0).getMaterial().equals(material);
        assert result.get(0).getColor().equals("red");

        assert result.get(1).getMaterial().equals(material);
        assert result.get(1).getColor().equals("yellow");
    }

    public static class CustomBlock implements Block {
        private final String colour;
        private final String material;

        public CustomBlock(final String colour, final String material) {
            this.colour = colour;
            this.material = material;
        }

        @Override
        public String getMaterial() {
            return material;
        }

        @Override
        public String getColor() {
            return colour;
        }
    }

    public static class WoodBlock implements Block {

        @Override
        public String getColor() {
            return "red";
        }

        @Override
        public String getMaterial() {
            return "wood";
        }
    }

    public static class MetalBlock implements Block {
        @Override
        public String getMaterial() {
            return "metal";
        }

        @Override
        public String getColor() {
            return "silver";
        }
    }

    public static class BrickCompositeBlock implements CompositeBlock {

        @Override
        public List<Block> getBlocks() {
            return List.of(METAL_BLOCK, WOOD_BLOCK);
        }

        @Override
        public String getColor() {
            return "red-silver";
        }

        @Override
        public String getMaterial() {
            return "metal-wood";
        }
    }

    public static class CustomCompositeBlock implements CompositeBlock {
        private final String colour;
        private final String material;
        private final List<Block> blocks;

        public CustomCompositeBlock(final String colour, final String material, final List<Block> blocks) {
            this.colour = colour;
            this.material = material;
            this.blocks = blocks;
        }

        @Override
        public List<Block> getBlocks() {
            return blocks;
        }

        @Override
        public String getColor() {
            return colour;
        }

        @Override
        public String getMaterial() {
            return material;
        }
    }
}
