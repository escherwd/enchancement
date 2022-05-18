/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.enchantment;

import moriyashiine.enchancement.client.packet.ResetFrozenTicksPacket;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class FrostbiteEnchantment extends EmptyEnchantment {
	public FrostbiteEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, type, slotTypes);
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return super.canAccept(other) && other != Enchantments.FIRE_ASPECT;
	}

	@Override
	public void onTargetDamaged(LivingEntity user, Entity target, int level) {
		if (user instanceof MobEntity) {
			applyEffect(target, 1);
		}
	}

	public static void applyEffect(Entity target, float cooldown) {
		if (cooldown >= Enchancement.getConfig().weaponEnchantmentCooldownRequirement && !target.world.isClient && target instanceof LivingEntity living) {
			if (!living.isDead()) {
				if (target.getFrozenTicks() < 300) {
					target.setFrozenTicks(300);
				}
			} else if (target instanceof ServerPlayerEntity player) {
				ResetFrozenTicksPacket.send(player);
			}
		}
	}
}
