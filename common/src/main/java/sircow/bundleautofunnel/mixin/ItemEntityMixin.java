package sircow.bundleautofunnel.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.bundleautofunnel.BundleHelper;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"), cancellable = true)
    private void bundleautofunnel$tryFunnelOnPickup(Player player, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        ItemStack stack = self.getItem();

        if (stack.isEmpty()) return;

        int inserted = BundleHelper.tryInsertIntoBundle(player, stack);

        if (inserted <= 0) return;

        if (stack.isEmpty()) {
            self.discard();
            ci.cancel();
        }
        else {
            self.setItem(stack);
        }
    }
}
