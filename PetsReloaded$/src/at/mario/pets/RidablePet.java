package at.mario.pets;

import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.World;

public class RidablePet extends Entity{

	public RidablePet(World world) {
		super(world);
	}

	  public void e(float sideMot, float forMot)
	  {
	    if ((this.passenger == null) || (!(this.passenger instanceof EntityHuman)))
	    {
	      super.e(sideMot, forMot);
	      this.W = 0.5F;
	      
	      return;
	    }
	    EntityHuman human = (EntityHuman)this.passenger;
	    if (!RideThaMob.control.contains(human.getBukkitEntity().getName()))
	    {
	      super.e(sideMot, forMot);
	      this.W = 0.5F;
	      return;
	    }
	    this.lastYaw = (this.yaw = this.passenger.yaw);
	    this.pitch = (this.passenger.pitch * 0.5F);
	    
	    b(this.yaw, this.pitch);
	    this.aO = (this.aM = this.yaw);
	    
	    this.W = 1.0F;
	    
	    sideMot = ((EntityLiving)this.passenger).bd * 0.5F;
	    forMot = ((EntityLiving)this.passenger).be;
	    if (forMot <= 0.0F) {
	      forMot *= 0.25F;
	    }
	    sideMot *= 0.75F;
	    
	    float speed = 0.35F;
	    
	    i(speed);
	    super.e(sideMot, forMot);
	    try
	    {
	      Field jump = null;
	      jump = EntityLiving.class.getDeclaredField("bc");
	      jump.setAccessible(true);
	      if ((jump != null) && (this.onGround)) {
	        if (jump.getBoolean(this.passenger))
	        {
	          double jumpHeight = 0.5D;
	          this.motY = jumpHeight;
	        }
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	  }

	@Override
	protected void a(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void b(NBTTagCompound arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void h() {
		// TODO Auto-generated method stub
		
	}
}
