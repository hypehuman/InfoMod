package de.outinetworks.InfoMod.events;


import de.outinetworks.InfoMod.config.InfoModConfig;
import de.outinetworks.InfoMod.mods.AnimalInfo;
import de.outinetworks.InfoMod.mods.ChunkOverlay;
import de.outinetworks.InfoMod.mods.DurabilityViewer;
import de.outinetworks.InfoMod.mods.SpawnOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class GUIHandlers
{
	@SubscribeEvent
	public void tickEvent(RenderWorldLastEvent event)
	{
		Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
		if(entity != null)
		{
			GL11.glPushMatrix();
			// Map GL Output to Player Position
			WCoordMatch(entity, event.getPartialTicks());

			ChunkOverlay.renderBounds(entity);
			SpawnOverlay.renderLighting(entity);
			GL11.glPopMatrix();
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onRenderGui(RenderGameOverlayEvent.Post event)
	{
		if(event.isCancelable() || event.getType() != ElementType.ALL)     
			return;
	
		new DurabilityViewer(Minecraft.getMinecraft());

		if(InfoModConfig.showAnimalInfo)AnimalInfo.renderInfoBox();
	}
	
	private static void WCoordMatch(Entity entity, float frame)
	{       
		double X = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
		double Y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
		double Z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;
		
		GL11.glTranslated(-X, -Y, -Z);
	}
}
