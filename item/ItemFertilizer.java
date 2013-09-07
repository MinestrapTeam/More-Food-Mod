package clashsoft.mods.morefood.item;

import clashsoft.mods.morefood.block.BlockBush;
import clashsoft.mods.morefood.block.BlockPlantMoreFood;

import net.minecraft.block.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class ItemFertilizer extends ItemMoreFood
{
	public ItemFertilizer(int par1)
	{
		super(par1);
	}
	
	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
	{
		if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
		{
			return false;
		}
		else
		{
			int var11;
			int var12;
			int var13;
			var11 = par3World.getBlockId(par4, par5, par6);
			
			BonemealEvent event = new BonemealEvent(par2EntityPlayer, par3World, var11, par4, par5, par6);
			if (MinecraftForge.EVENT_BUS.post(event))
			{
				return false;
			}
			
			if (event.getResult() == Result.ALLOW)
			{
				if (!par3World.isRemote)
				{
					par1ItemStack.stackSize--;
				}
				return true;
			}
			
			if (Block.blocksList[var11] instanceof BlockPlantMoreFood)
			{
				if (!par3World.isRemote)
				{
					if (((BlockPlantMoreFood) Block.blocksList[var11]).fertilize(par3World, par4, par5, par6))
					{
						--par1ItemStack.stackSize;
					}
				}
				
				return true;
			}
			
			if (Block.blocksList[var11] instanceof BlockBush)
			{
				if (!par3World.isRemote)
				{
					if (((BlockBush) Block.blocksList[var11]).fertilize(par3World, par4, par5, par6))
					{
						--par1ItemStack.stackSize;
					}
				}
				
				return true;
			}
			
			if (Block.blocksList[var11] instanceof BlockSapling)
			{
				if (!par3World.isRemote)
				{
					((BlockSapling) Block.blocksList[var11]).growTree(par3World, par4, par5, par6, par3World.rand);
					--par1ItemStack.stackSize;
				}
				
				return true;
			}
			
			if (var11 == Block.mushroomBrown.blockID || var11 == Block.mushroomRed.blockID)
			{
				if (!par3World.isRemote && ((BlockMushroom) Block.blocksList[var11]).fertilizeMushroom(par3World, par4, par5, par6, par3World.rand))
				{
					--par1ItemStack.stackSize;
				}
				
				return true;
			}
			
			if (var11 == Block.melonStem.blockID || var11 == Block.pumpkinStem.blockID)
			{
				if (par3World.getBlockMetadata(par4, par5, par6) == 7)
				{
					return false;
				}
				
				if (!par3World.isRemote)
				{
					((BlockStem) Block.blocksList[var11]).fertilizeStem(par3World, par4, par5, par6);
					--par1ItemStack.stackSize;
				}
				
				return true;
			}
			
			if (var11 > 0 && Block.blocksList[var11] instanceof BlockCrops)
			{
				if (par3World.getBlockMetadata(par4, par5, par6) == 7)
				{
					return false;
				}
				
				if (!par3World.isRemote)
				{
					((BlockCrops) Block.blocksList[var11]).fertilize(par3World, par4, par5, par6);
					--par1ItemStack.stackSize;
				}
				
				return true;
			}
			
			if (var11 == Block.cocoaPlant.blockID)
			{
				if (!par3World.isRemote)
				{
					par3World.setBlockMetadataWithNotify(par4, par5, par6, 8 | BlockDirectional.getDirection(par3World.getBlockMetadata(par4, par5, par6)), 2);
					--par1ItemStack.stackSize;
				}
				
				return true;
			}
			
			if (var11 == Block.grass.blockID)
			{
				if (!par3World.isRemote)
				{
					--par1ItemStack.stackSize;
					label133:
					
					for (var12 = 0; var12 < 128; ++var12)
					{
						var13 = par4;
						int var14 = par5 + 1;
						int var15 = par6;
						
						for (int var16 = 0; var16 < var12 / 16; ++var16)
						{
							var13 += itemRand.nextInt(3) - 1;
							var14 += (itemRand.nextInt(3) - 1) * itemRand.nextInt(3) / 2;
							var15 += itemRand.nextInt(3) - 1;
							
							if (par3World.getBlockId(var13, var14 - 1, var15) != Block.grass.blockID || par3World.isBlockNormalCube(var13, var14, var15))
							{
								continue label133;
							}
						}
						
						if (par3World.getBlockId(var13, var14, var15) == 0)
						{
							if (itemRand.nextInt(10) != 0)
							{
								if (Block.tallGrass.canBlockStay(par3World, var13, var14, var15))
								{
									par3World.setBlock(var13, var14, var15, Block.tallGrass.blockID, 1, 2);
								}
							}
							else
							{
								ForgeHooks.plantGrass(par3World, var13, var14, var15);
							}
						}
					}
				}
				
				return true;
			}
			return false;
		}
	}
}
