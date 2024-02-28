package net.TrivialBrett.mccourse.datagen;

import net.TrivialBrett.mccourse.MCCourseMod;
import net.TrivialBrett.mccourse.item.ModItems;
import net.TrivialBrett.mccourse.loot.AddItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import org.checkerframework.checker.units.qual.A;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output ) {
        super(output, MCCourseMod.MOD_ID);
    }

    @Override
    protected void start() {
        // Adds Kohlrabi seeds to grass and fern loot tables
        add("kohlrabi_seeds_from_grass", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
                LootItemRandomChanceCondition.randomChance(0.35f).build()},
                ModItems.KOHLRABI_SEEDS.get()));
        add("kohlrabi_seeds_from_fern", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.FERN).build(),
                LootItemRandomChanceCondition.randomChance(0.35f).build()},
                ModItems.KOHLRABI_SEEDS.get()));

        // Adds the metal detector to the jungle temple loot chest
        add("metal_detector_from_jungle_temple", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("chests/jungle_temple")).build()},
                ModItems.METAL_DETECTOR.get()));
    }
}
