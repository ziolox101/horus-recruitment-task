package pl.ziolkowski.dawid;

import java.util.List;

interface CompositeBlock extends Block {
    List<Block> getBlocks();
}