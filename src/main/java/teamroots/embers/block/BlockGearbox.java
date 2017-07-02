package teamroots.embers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import teamroots.embers.tileentity.TileEntityAxle;
import teamroots.embers.tileentity.TileEntityGearbox;
import teamroots.embers.tileentity.TileEntityItemExtractor;
import teamroots.embers.tileentity.TileEntityRelay;
import teamroots.embers.util.Misc;

public class BlockGearbox extends BlockTEBase {
	public static final PropertyDirection facing = PropertyDirection.create("facing");

	public BlockGearbox(Material material, String name, boolean addToTab) {
		super(material, name, addToTab);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(facing).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(facing, EnumFacing.getFront(meta));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		if (Math.abs(placer.posX - (double) ((float) pos.getX() + 0.5F)) < 2.0D && Math.abs(placer.posZ - (double) ((float) pos.getZ() + 0.5F)) < 2.0D) {
			double d0 = placer.posY + (double) placer.getEyeHeight();

			if (d0 - (double) pos.getY() > 2.0D) {
				return getDefaultState().withProperty(facing, EnumFacing.UP);
			}

			if ((double) pos.getY() - d0 > 0.0D) {
				return getDefaultState().withProperty(facing, EnumFacing.DOWN);
			}
		}
		return getDefaultState().withProperty(facing, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side != state.getValue(facing);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityGearbox();
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
		TileEntityGearbox p = (TileEntityGearbox) world.getTileEntity(pos);
		p.updateNeighbors();
		p.markDirty();
	}
}
