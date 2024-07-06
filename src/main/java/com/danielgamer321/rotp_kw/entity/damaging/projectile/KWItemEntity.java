package com.danielgamer321.rotp_kw.entity.damaging.projectile;

import com.danielgamer321.rotp_kw.init.InitEntities;
import com.github.standobyte.jojo.entity.itemprojectile.ItemProjectileEntity;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;

import net.minecraft.entity.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.properties.NoteBlockInstrument;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class KWItemEntity extends ItemProjectileEntity implements IRendersAsItem {
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.defineId(KWItemEntity.class, DataSerializers.ITEM_STACK);
    private boolean Remove = true;
    private boolean RemoveAfterHitting = false;
    private boolean Beneficial = false;

    public KWItemEntity(LivingEntity shooter, World world) {
        super(InitEntities.KW_ITEM.get(), shooter, world);
    }

    public KWItemEntity(EntityType<? extends KWItemEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ITEM, ItemStack.EMPTY);
    }
    
    @Override
    protected boolean hurtTarget(Entity target, Entity thrower) {
        Item item = entityData.get(ITEM).getItem();
        if (item == Items.TORCH ||
            item == Items.SOUL_TORCH ||
            item == Items.CAMPFIRE ||
            item == Items.SOUL_CAMPFIRE ||
            item == Items.MAGMA_BLOCK) {
            return DamageUtil.dealDamageAndSetOnFire(target, 
                    entity -> super.hurtTarget(target, thrower), 4, true);
        }
        boolean hurt = super.hurtTarget(target, thrower);
        
        if (hurt) {
            if (item == Items.NOTE_BLOCK) {
                target.playSound(NoteBlockInstrument.values()[random.nextInt(NoteBlockInstrument.values().length)].getSoundEvent(), 
                        5.0F, (float) Math.pow(2.0D, (double) (random.nextInt(24) - 12) / 12.0D));
            }
            
            else if (target instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) target;
                if (item == Items.PUFFERFISH) {
                    living.addEffect(new EffectInstance(Effects.POISON, 60, 0));
                }
                else if (item == Items.ICE) {
                    living.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(), 60, 0));
                }
                else if (item == Items.PACKED_ICE) {
                    living.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(), 60, 1));
                }
                else if (item == Items.BLUE_ICE) {
                    living.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(), 60, 2));
                }
                else if (item == Items.WITHER_ROSE) {
                    if (!living.isInvulnerableTo(DamageSource.WITHER)) {
                        living.addEffect(new EffectInstance(Effects.WITHER, 40, 0));
                    }
                }
            }
        }
        return hurt;
    }
    
    @Override
    protected void onHit(RayTraceResult rayTraceResult) {
        if (!level.isClientSide()) {
            Item item = entityData.get(ITEM).getItem();
            BlockPos blockpos = new BlockPos(0, 0, 0);
            switch (rayTraceResult.getType()) {
                case BLOCK:
                    BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)rayTraceResult;
                    blockpos = blockraytraceresult.getBlockPos();
                    break;
                case ENTITY:
                    EntityRayTraceResult entityraytraceresult = (EntityRayTraceResult)rayTraceResult;
                    blockpos = entityraytraceresult.getEntity().blockPosition();
                    break;
            }
            if (item == Items.TORCH || item == Items.SOUL_TORCH || item == Items.REDSTONE_TORCH) {
                dropItem(Items.STICK, 0, blockpos);
            }
            else if (item == Items.CAMPFIRE || item == Items.SOUL_CAMPFIRE) {
                dropItem(Items.CHARCOAL, 2, blockpos);
            }
            else if (item instanceof BucketItem || item == Items.MILK_BUCKET) {
                dropItem(Items.BUCKET, 0, blockpos);
                if (item instanceof BucketItem ) {
                    BucketItem bucket = (BucketItem) item;
                    switch (rayTraceResult.getType()) {
                        case BLOCK:
                            BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)rayTraceResult;
                            if (bucket.emptyBucket((PlayerEntity)null, this.level, blockpos, blockraytraceresult)) {
                                bucket.checkExtraContent(this.level, getItem(), blockpos);
                            }
                            break;
                        case ENTITY:
                            if (bucket.emptyBucket((PlayerEntity)null, this.level, blockpos, (BlockRayTraceResult)null)) {
                                bucket.checkExtraContent(this.level, getItem(), blockpos);
                            }
                            break;
                    }
                }
            }
            else if (item == Items.INFESTED_CHISELED_STONE_BRICKS ||
                item == Items.INFESTED_COBBLESTONE ||
                item == Items.INFESTED_CRACKED_STONE_BRICKS ||
                item == Items.INFESTED_MOSSY_STONE_BRICKS ||
                item == Items.INFESTED_STONE ||
                item == Items.INFESTED_STONE_BRICKS) {
                SilverfishEntity silverfish = EntityType.SILVERFISH.create(level);
                silverfish.moveTo(blockpos.getX(), blockpos.getY() + 0.5, blockpos.getZ(), 0.0F, 0.0F);
                level.addFreshEntity(silverfish);
                silverfish.spawnAnim();
            }
            else if (item == Items.END_CRYSTAL) {
                this.level.explode((Entity)null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), 6.0F, Explosion.Mode.DESTROY);
            }
            else if (item instanceof SpawnEggItem) {
                ItemStack stack = entityData.get(ITEM);
                SpawnEggItem spawn = (SpawnEggItem) item;
                EntityType<?> entitytype = ((SpawnEggItem)stack.getItem()).getType(stack.getTag());
                entitytype.spawn((ServerWorld) this.level, stack, (PlayerEntity)null, blockpos, SpawnReason.SPAWN_EGG, false, false);
            }
            if (RemoveAfterHitting) {
                remove();
            }
        }
        super.onHit(rayTraceResult);
    }

    private void dropItem(IItemProvider item, float p_70099_2_, BlockPos blockpos) {
        ItemEntity itementity = new ItemEntity(this.level, blockpos.getX(), blockpos.getY() + (double)p_70099_2_, blockpos.getZ(), new ItemStack(item));
        itementity.setDefaultPickUpDelay();
        this.level.addFreshEntity(itementity);
    }

    @Override
    protected boolean isRemovedOnEntityHit() {
        return Remove? true : false;
    }

    public boolean willBeRemovedOnEntityHit() {
        return Remove;
    }

    @Override
    public boolean isPickable() {
        return RemoveAfterHitting ? true : false;
    }

    public void setItem(ItemStack itemStack) {
        this.getEntityData().set(ITEM, Util.make(itemStack.copy(), (stack) -> {
            stack.setCount(1);
        }));
    }

    /*
     * careful - this method in particular only exists on client side (because it comes from IRendersAsItem), 
     * calling it on server side will crash dedicated servers
     */
    @Override
    public ItemStack getItem() {
        return entityData.get(ITEM);
    }

    @Override
    protected ItemStack getPickupItem() {
        return entityData.get(ITEM);
    }

    public void setRemoveItem(boolean remove) {
        Remove = remove;
    }

    public void removeAfterHitting(boolean remove) {
        RemoveAfterHitting = remove;
    }

    public void setBeneficial(boolean beneficial) {
        Beneficial = beneficial;
    }

    public boolean isBeneficial() {
        return Beneficial;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        ItemStack itemstack = this.entityData.get(ITEM);
        if (!itemstack.isEmpty()) {
            nbt.put("Item", itemstack.save(new CompoundNBT()));
        }
        nbt.putBoolean("Beneficial", Beneficial);
        nbt.putBoolean("Remove", Remove);
        nbt.putBoolean("RemoveAfterHitting", RemoveAfterHitting);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        ItemStack itemstack = ItemStack.of(nbt.getCompound("Item"));
        this.setItem(itemstack);
        Beneficial = nbt.getBoolean("Beneficial");
        Remove = nbt.getBoolean("Remove");
        RemoveAfterHitting = nbt.getBoolean("RemoveAfterHitting");
    }
}
