package sircow.bundleautofunnel;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.List;

public class BundleHelper {
    public static int tryInsertIntoBundle(Player player, ItemStack stack) {
        if (stack.isEmpty() || stack.is(ItemTags.BUNDLES)) return 0;
        if (!BundleContents.canItemBeInBundle(stack)) return 0;

        Inventory inventory = player.getInventory();
        int totalInserted = 0;

        for (int i = 0; i < inventory.getContainerSize() && !stack.isEmpty(); i++) {
            ItemStack bundle = inventory.getItem(i);

            if (!bundle.is(ItemTags.BUNDLES)) continue;

            int inserted = tryInsertIntoBundle(bundle, stack);

            if (inserted <= 0) continue;

            inventory.setItem(i, bundle);
            totalInserted += inserted;
        }

        if (totalInserted > 0) playInsertSound(player);

        return totalInserted;
    }

    public static int tryInsertIntoBundle(ItemStack stack, List<Slot> slots, Inventory playerInventory, Player player) {
        if (stack.isEmpty() || stack.is(ItemTags.BUNDLES)) return 0;
        if (!BundleContents.canItemBeInBundle(stack)) return 0;

        int totalInserted = 0;

        for (Slot slot : slots) {
            if (slot.container == playerInventory) continue;

            ItemStack bundle = slot.getItem();

            if (!bundle.is(ItemTags.BUNDLES)) continue;

            int inserted = tryInsertIntoBundle(bundle, stack);

            if (inserted <= 0) continue;

            slot.set(bundle);
            slot.setChanged();

            totalInserted += inserted;

            if (stack.isEmpty()) break;
        }

        if (totalInserted > 0) playInsertSound(player);

        return totalInserted;
    }

    private static int tryInsertIntoBundle(ItemStack bundle, ItemStack stack) {
        BundleContents contents = bundle.get(DataComponents.BUNDLE_CONTENTS);

        if (contents == null || contents.isEmpty()) return 0;

        boolean containsItem = false;

        for (ItemStackTemplate template : contents.items()) {
            if (template.typeHolder().value() == stack.getItem()) {
                containsItem = true;
                break;
            }
        }

        if (!containsItem) return 0;

        BundleContents.Mutable mutable = new BundleContents.Mutable(contents);
        int inserted = mutable.tryInsert(stack);

        if (inserted <= 0) return 0;

        bundle.set(DataComponents.BUNDLE_CONTENTS, mutable.toImmutable());

        return inserted;
    }

    private static void playInsertSound(Player player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.BUNDLE_INSERT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
