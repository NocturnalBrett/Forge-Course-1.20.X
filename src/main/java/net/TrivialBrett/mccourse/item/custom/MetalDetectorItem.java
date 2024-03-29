package net.TrivialBrett.mccourse.item.custom;

import net.TrivialBrett.mccourse.MCCourseMod;
import net.TrivialBrett.mccourse.block.ModBlocks;
import net.TrivialBrett.mccourse.item.ModItems;
import net.TrivialBrett.mccourse.sound.ModSounds;
import net.TrivialBrett.mccourse.util.InventoryUtil;
import net.TrivialBrett.mccourse.util.ModTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
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

                    // Checks if player has Data Tablet in inventory
                    if(InventoryUtil.hasPlayerStackInInventory(player, ModItems.DATA_TABLET.get())){

                        // Adds data to the Data Tablet
                        addDataToDataTablet(player, positionClicked.below(i), blockState.getBlock());
                    }

                    pContext.getLevel().playSeededSound(null, player.getX(), player.getY(), player.getZ(),
                            ModSounds.METAL_DETECTOR_FOUND_ORE.get(), SoundSource.BLOCKS, 1f, 1f, 0);

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

    private void addDataToDataTablet(Player player, BlockPos below, Block block) {
        // Gets the inventory slot that the Data Tablet is in
        ItemStack dataTablet = player.getInventory().getItem(InventoryUtil.getFirstInventoryIndex(player, ModItems.DATA_TABLET.get()));

        // Adds data to Data Tablet
        CompoundTag data = new CompoundTag();
        data.putString("mccourse.found_ore", "Valuable Found: " + I18n.get(block.getDescriptionId())
                + " at (" + below.getX() + ", " + below.getY() + ", " + below.getZ() + ")");

        dataTablet.setTag(data);


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

