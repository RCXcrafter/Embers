package teamroots.embers.tileentity;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import teamroots.embers.EventManager;
import teamroots.embers.block.BlockAxle;
import teamroots.embers.network.PacketHandler;
import teamroots.embers.network.message.MessageTEUpdate;
import teamroots.embers.power.DefaultMechCapability;
import teamroots.embers.power.MechCapabilityProvider;
import teamroots.embers.util.Misc;

public class TileEntityCreativeMechSource extends TileEntity implements ITileEntityBase, ITickable {
	int ticksExisted = 0;
	BlockPos receivedFrom = null;
	public DefaultMechCapability capability = new DefaultMechCapability();
	
	public TileEntityCreativeMechSource(){
		super();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag){
		super.writeToNBT(tag);
		capability.writeToNBT(tag);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		super.readFromNBT(tag);
		capability.readFromNBT(tag);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		if (capability == MechCapabilityProvider.mechCapability){
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		if (capability == MechCapabilityProvider.mechCapability){
			return (T)this.capability;
		}
		return super.getCapability(capability, facing);
	}
	
	public boolean dirty = false;
	
	@Override
	public void markForUpdate(){
		EventManager.markTEForUpdate(getPos(), this);
	}
	
	@Override
	public void markDirty(){
		markForUpdate();
		super.markDirty();
	}

	@Override
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		capability.setPower(0f,null);
		updateNearby();
		world.setTileEntity(pos, null);
	}
	
	public void updateNearby(){
		for (EnumFacing f : EnumFacing.values()){
			TileEntity t = worldObj.getTileEntity(getPos().offset(f));
			if (t != null){
				if (t.hasCapability(MechCapabilityProvider.mechCapability, Misc.getOppositeFace(f))){
					t.getCapability(MechCapabilityProvider.mechCapability, Misc.getOppositeFace(f)).setPower(capability.getPower(Misc.getOppositeFace(f)),Misc.getOppositeFace(f));
					t.markDirty();
				}
			}
		}
	}

	@Override
	public void update() {
		if (capability.getPower(null) < 200f){
			capability.setPower(200f,null);
			markDirty();
		}
		updateNearby();
	}
}
