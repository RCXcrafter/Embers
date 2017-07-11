package teamroots.embers.compat.crafttweaker;

import minetweaker.MineTweakerImplementationAPI;
import minetweaker.util.IEventHandler;
import net.minecraft.item.ItemStack;
import teamroots.embers.recipe.RecipeRegistry;

public class Melter implements IEventHandler<MineTweakerImplementationAPI.ReloadEvent> {

	public Melter() {
        MineTweakerImplementationAPI.onPostReload(this);
    }
	
	@Override
	public void handle(MineTweakerImplementationAPI.ReloadEvent event) {
		RecipeRegistry.allMeltingOreRecipes.clear();
		RecipeRegistry.allMeltingRecipes.clear();
		
		for (String recipeString : RecipeRegistry.meltingOreRecipes.keySet()){
			RecipeRegistry.allMeltingOreRecipes.add(RecipeRegistry.meltingOreRecipes.get(recipeString));
		}
		
		for (ItemStack recipeStack : RecipeRegistry.meltingRecipes.keySet()){
			RecipeRegistry.allMeltingRecipes.add(RecipeRegistry.meltingRecipes.get(recipeStack));
		}
	}
}
