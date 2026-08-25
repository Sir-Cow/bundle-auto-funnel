package sircow.bundleautofunnel.mixin;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sircow.bundleautofunnel.BundleHelper;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @Inject(method = "clicked", at = @At("HEAD"), cancellable = true)
    private void bundleautofunnel$onClicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo ci) {
        if (player.level().isClientSide()) return;
        if (containerInput != ContainerInput.QUICK_MOVE) return;

        AbstractContainerMenu menu = (AbstractContainerMenu) (Object) this;

        if (!menu.isValidSlotIndex(slotIndex)) return;

        Slot sourceSlot = menu.getSlot(slotIndex);
        ItemStack stack = sourceSlot.getItem();

        if (stack.isEmpty()) return;
        if (stack.is(ItemTags.BUNDLES)) return;

        int inserted;

        if (sourceSlot.container == player.getInventory()) {
            inserted = BundleHelper.tryInsertIntoBundle(stack, menu.slots, player.getInventory(), player);
        }
        else {
            inserted = BundleHelper.tryInsertIntoBundle(player, stack);
        }

        if (inserted > 0 && stack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
            sourceSlot.setChanged();
            ci.cancel();
        }
    }
}
