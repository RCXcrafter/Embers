package teamroots.embers.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import teamroots.embers.RegistryManager;
import teamroots.embers.item.ItemAshenCloak;
import teamroots.embers.item.ItemInflictorGem;

public class AshenCloakUnsocketRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		boolean has_cloak = false;
		boolean more_than_one_cloak = false;
		if (inv.getSizeInventory() > 4){
			for (int i = 0; i < inv.getSizeInventory(); i ++){
				if (inv.getStackInSlot(i) != null){
					if (inv.getStackInSlot(i).getItem() == RegistryManager.ashen_cloak_chest && inv.getStackInSlot(i).getTagCompound() != null){
						if (inv.getStackInSlot(i).getTagCompound().hasKey("gem1") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem2") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem3") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem4") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem5") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem6") ||
								inv.getStackInSlot(i).getTagCompound().hasKey("gem7")){
							if (!has_cloak && !more_than_one_cloak){
								has_cloak = true;
							}
							else if (has_cloak){
								has_cloak = false;
								more_than_one_cloak = true;
							}
						}
					}
					else {
						if (inv.getStackInSlot(i) != null){
							return false;
						}
					}
				}
			}
		}
		return has_cloak;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack capeStack = null;
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != null){
				if (inv.getStackInSlot(i).getItem() == RegistryManager.ashen_cloak_chest){
					capeStack = inv.getStackInSlot(i).copy();
				}
			}
		}
		if (capeStack != null){
			for (int i = 1; i < 8; i ++){
				if (capeStack.getTagCompound().hasKey("gem"+i)){
					capeStack.getTagCompound().removeTag("gem"+i);
				}
			}
		}
		return capeStack;
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(RegistryManager.ashen_cloak_chest,1);
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		ItemStack[] gems = new ItemStack[inv.getSizeInventory()];
		for (int i = 0; i < inv.getSizeInventory(); i ++){
			if (inv.getStackInSlot(i) != null){
				if (inv.getStackInSlot(i).getItem() == RegistryManager.ashen_cloak_chest){
					for (int j = 1; j < 8; j ++){
						if (inv.getStackInSlot(i).getTagCompound().hasKey("gem"+j)){
							gems[i] = new ItemStack(RegistryManager.inflictor_gem, 1, 0, inv.getStackInSlot(i).getTagCompound().getCompoundTag("gem"+j));
						}
					}
				}
			}
		}
		inv.clear();
		return gems;
	}

}
