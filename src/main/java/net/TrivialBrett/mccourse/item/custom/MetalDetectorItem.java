package net.TrivialBrett.mccourse.item.custom;

import net.TrivialBrett.mccourse.MCCourseMod;
import net.TrivialBrett.mccourse.block.ModBlocks;
import net.TrivialBrett.mccourse.util.ModTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetalDetectorItem extends Item {
    public MetalDetectorItem(Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(!pContext.getLevel().isClientSide())
        {
            BlockPos positionClicked = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            boolean foundBlock = false;

            int bedrock = -64;


            for(int i = bedrock; i <= positionClicked.getY(); i++)
            {
                BlockState blockState = pContext.getLevel().getBlockState(positionClicked.below(i));

                MCCourseMod.LOGGER.info("Test: " + i);

                if(isValuableBlock(blockState))
                {
                    outputValuableCoordinates(positionClicked.below(i), player, blockState.getBlock());
                    foundBlock = true;

                    break;
                }
            }

            if(!foundBlock)
            {
                outputNoValuableFound(player);
            }
        }

        // Damages the player's Item when used
        pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(),
                player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
    {
        if(Screen.hasShiftDown())
        {
            pTooltipComponents.add(Component.translatable("tooltip.mccourse.metal_detector.tooltip.shift"));

        }else {
            pTooltipComponents.add(Component.translatable("tooltip.mccourse.metal_detector.tooltip"));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private void outputNoValuableFound(Player player)
    {
        player.sendSystemMessage(Component.translatable("item.mccourse.metal_detector.no_valuables"));
    }

    private void outputValuableCoordinates(BlockPos below, Player player, Block block)
    {
        player.sendSystemMessage(Component.literal("Valuable Found: " + I18n.get(block.getDescriptionId())
                + " at (" + below.getX() + ", " + below.getY() + ", " + below.getZ() + ")"));
    }

    private boolean isValuableBlock(BlockState blockState)
    {
        return blockState.is(ModTags.Blocks.METAL_DETECTOR_VALUABLES);
    }

}

