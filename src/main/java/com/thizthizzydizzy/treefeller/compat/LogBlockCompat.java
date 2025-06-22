package com.thizthizzydizzy.treefeller.compat;
import com.thizthizzydizzy.treefeller.Modifier;
import com.thizthizzydizzy.treefeller.Tool;
import com.thizthizzydizzy.treefeller.Tree;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
public class LogBlockCompat extends InternalCompatibility{
    @Override
    public String getFriendlyName(){
        return "LogBlock (1.19)";
    }
    @Override
    public String getPluginName(){
        return "LogBlock";
    }
    @Override
    public void addBlock(Player player, Block block, BlockState was){
        de.diddiz.LogBlock.util.LoggingUtil.smartLogBlockPlace(de.diddiz.LogBlock.LogBlock.getInstance().getConsumer(), de.diddiz.LogBlock.Actor.actorFromEntity(player), was, block.getState());
    }
    @Override
    public void removeBlock(Player player, Block block){
        de.diddiz.LogBlock.util.LoggingUtil.smartLogBlockBreak(de.diddiz.LogBlock.LogBlock.getInstance().getConsumer(), de.diddiz.LogBlock.Actor.actorFromEntity(player), block);
    }
    @Override
    public void breakBlock(Tree tree, Tool tool, Player player, ItemStack axe, Block block, List<Modifier> modifiers){
        de.diddiz.LogBlock.util.LoggingUtil.smartLogBlockBreak(de.diddiz.LogBlock.LogBlock.getInstance().getConsumer(), de.diddiz.LogBlock.Actor.actorFromEntity(player), block);
    }
}