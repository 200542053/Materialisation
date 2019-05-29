package me.shedaniel.materialisation.items;

import com.google.common.collect.Sets;
import me.shedaniel.materialisation.MaterialisationUtils;
import me.shedaniel.materialisation.ModReference;
import me.shedaniel.materialisation.mixin.MiningToolItemAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class MaterialisedAxeItem extends MiningToolItem implements MaterialisedMiningTool {
    
    private static final Set<Block> EFFECTIVE_BLOCKS = Sets.newHashSet(new Block[]{Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.BOOKSHELF, Blocks.OAK_WOOD, Blocks.SPRUCE_WOOD, Blocks.BIRCH_WOOD, Blocks.JUNGLE_WOOD, Blocks.ACACIA_WOOD, Blocks.DARK_OAK_WOOD, Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG, Blocks.CHEST, Blocks.PUMPKIN, Blocks.CARVED_PUMPKIN, Blocks.JACK_O_LANTERN, Blocks.MELON, Blocks.LADDER, Blocks.SCAFFOLDING, Blocks.OAK_BUTTON, Blocks.SPRUCE_BUTTON, Blocks.BIRCH_BUTTON, Blocks.JUNGLE_BUTTON, Blocks.DARK_OAK_BUTTON, Blocks.ACACIA_BUTTON, Blocks.OAK_PRESSURE_PLATE, Blocks.SPRUCE_PRESSURE_PLATE, Blocks.BIRCH_PRESSURE_PLATE, Blocks.JUNGLE_PRESSURE_PLATE, Blocks.DARK_OAK_PRESSURE_PLATE, Blocks.ACACIA_PRESSURE_PLATE});
    
    public MaterialisedAxeItem(Settings settings) {
        super(0, -3.1F, MaterialisationUtils.DUMMY_MATERIAL, EFFECTIVE_BLOCKS, settings.durability(0));
        addProperty(new Identifier(ModReference.MOD_ID, "handle_isbright"), (itemStack, world, livingEntity) -> {
            return itemStack.getOrCreateTag().containsKey("mt_handle_bright") ? 1f : 0f;
        });
        addProperty(new Identifier(ModReference.MOD_ID, "handle_isnotbright"), (itemStack, world, livingEntity) -> {
            return !itemStack.getOrCreateTag().containsKey("mt_handle_bright") ? 1f : 0f;
        });
        addProperty(new Identifier(ModReference.MOD_ID, "axe_head_isbright"), (itemStack, world, livingEntity) -> {
            return itemStack.getOrCreateTag().containsKey("mt_axe_head_bright") ? 1f : 0f;
        });
        addProperty(new Identifier(ModReference.MOD_ID, "axe_head_isnotbright"), (itemStack, world, livingEntity) -> {
            return !itemStack.getOrCreateTag().containsKey("mt_axe_head_bright") ? 1f : 0f;
        });
    }
    
    @Override
    public float getToolBlockBreakingSpeed(ItemStack stack, BlockState state) {
        Material material_1 = state.getMaterial();
        return material_1 != Material.WOOD && material_1 != Material.PLANT && material_1 != Material.REPLACEABLE_PLANT && material_1 != Material.BAMBOO ? (((MiningToolItemAccessor) stack.getItem()).getEffectiveBlocks().contains(state.getBlock()) ? MaterialisationUtils.getToolBreakingSpeed(stack) : 1.0F) : MaterialisationUtils.getToolBreakingSpeed(stack);
    }
    
    @Override
    public boolean canEffectivelyBreak(ItemStack itemStack, BlockState state) {
        return EFFECTIVE_BLOCKS.contains(state.getBlock());
    }
    
    @Override
    public boolean canRepair(ItemStack itemStack_1, ItemStack itemStack_2) {
        return false;
    }
    
    @Override
    public boolean onEntityDamaged(ItemStack stack, LivingEntity livingEntity_1, LivingEntity livingEntity_2) {
        if (!livingEntity_1.world.isClient && (!(livingEntity_1 instanceof PlayerEntity) || !((PlayerEntity) livingEntity_1).abilities.creativeMode))
            if (MaterialisationUtils.getToolDurability(stack) > 0)
                if (MaterialisationUtils.applyDamage(stack, 2, livingEntity_1.getRand())) {
                    livingEntity_1.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                    Item item_1 = stack.getItem();
                    stack.subtractAmount(1);
                    if (livingEntity_1 instanceof PlayerEntity) {
                        ((PlayerEntity) livingEntity_1).incrementStat(Stats.BROKEN.getOrCreateStat(item_1));
                    }
                    MaterialisationUtils.setToolDurability(stack, 0);
                }
        return true;
    }
    
    @Override
    public boolean onBlockBroken(ItemStack stack, World world_1, BlockState blockState_1, BlockPos blockPos_1, LivingEntity livingEntity_1) {
        if (!world_1.isClient && blockState_1.getHardness(world_1, blockPos_1) != 0.0F)
            if (!livingEntity_1.world.isClient && (!(livingEntity_1 instanceof PlayerEntity) || !((PlayerEntity) livingEntity_1).abilities.creativeMode))
                if (MaterialisationUtils.getToolDurability(stack) > 0)
                    if (MaterialisationUtils.applyDamage(stack, 1, livingEntity_1.getRand())) {
                        livingEntity_1.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                        Item item_1 = stack.getItem();
                        stack.subtractAmount(1);
                        if (livingEntity_1 instanceof PlayerEntity) {
                            ((PlayerEntity) livingEntity_1).incrementStat(Stats.BROKEN.getOrCreateStat(item_1));
                        }
                        MaterialisationUtils.setToolDurability(stack, 0);
                    }
        return true;
    }
    
    @Override
    public void buildTooltip(ItemStack stack, World world_1, List<Component> list_1, TooltipContext tooltipContext_1) {
        int toolDurability = MaterialisationUtils.getToolDurability(stack);
        int maxDurability = MaterialisationUtils.getToolMaxDurability(stack);
        list_1.add(new TranslatableComponent("text.materialisation.max_durability", maxDurability));
        if (toolDurability > 0)
            list_1.add(new TranslatableComponent("text.materialisation.durability", toolDurability, MaterialisationUtils.TWO_DECIMAL_FORMATTER.format(toolDurability / (float) maxDurability * 100)));
        else
            list_1.add(new TranslatableComponent("text.materialisation.broken"));
        list_1.add(new TranslatableComponent("text.materialisation.breaking_speed", MaterialisationUtils.TWO_DECIMAL_FORMATTER.format(MaterialisationUtils.getToolBreakingSpeed(stack))));
        list_1.add(new TranslatableComponent("text.materialisation.mining_level", MaterialisationUtils.getToolMiningLevel(stack)));
    }
    
}