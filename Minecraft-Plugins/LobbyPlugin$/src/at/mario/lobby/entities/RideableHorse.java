package at.mario.lobby.entities;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Horse;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;

public class RideableHorse extends EntityHorse {

    protected Field FIELD_JUMP = null;

    public RideableHorse(World world) {
        super(world);
   
        if (FIELD_JUMP == null) {
            try {
               FIELD_JUMP = EntityLiving.class.getDeclaredField("aY");
               FIELD_JUMP.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void g(float f, float f1) {
        if(this.passenger != null && this.passenger instanceof EntityHuman)
        {
            this.lastYaw = this.yaw = this.passenger.yaw;
            this.pitch = this.passenger.pitch * 0.5F;
            this.setYawPitch(this.yaw, this.pitch);
            this.aK = this.aI = this.yaw;
            f = ((EntityLiving)this.passenger).aZ * 0.5F;
            f1 = ((EntityLiving)this.passenger).ba;

            if(f1 <= 0.0F)
            {
                f1 *= 0.25F;
            }
              try {
                if (FIELD_JUMP.getBoolean(this.passenger) && this.onGround) {
                    this.motY += 1F;
                    this.ai = true;
                    if (f1 > 0.0F)
                    {
                        float f2 = MathHelper.sin(this.yaw * 3.141593F / 180.0F);
                        float f3 = MathHelper.cos(this.yaw * 3.141593F / 180.0F);

                        this.motX += -0.4F * f2 * ((EntityInsentient) this).br();
                        this.motZ += 0.4F * f3 * ((EntityInsentient) this).br();
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

            this.S = 1.0F; this.aM = this.bI() * 0.1F; if(!this.world.isClientSide)
            {
                this.k((float)this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
                super.g(f, f1);
            }

            this.aA = this.aB; double d0 = this.locX - this.lastX; double d1 = this.locZ - this.lastZ; float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
            if(f4 > 1.0F)
            {
                f4 = 1.0F;
            }

            this.aB += (f4 - this.aB) * 0.4F; this.aC += this.aB;
        } else {
            this.S = 0.5F; this.aM = 0.02F; super.g(f, f1);
        }
    }
    
    @Override
    public void setTame(boolean flag) {
    	super.setTame(true);
    }
    
    public static Horse spawn(Location location)
    {
        World mcWorld = (World) ((CraftWorld) location.getWorld()).getHandle();

        RideableHorse customEntity = new RideableHorse(mcWorld);
        customEntity.setLocation(location.getX(), location.getY(),location.getZ(), location.getYaw(), location.getPitch());

        ((CraftLivingEntity) customEntity.getBukkitEntity()).setRemoveWhenFarAway(false);

        mcWorld.addEntity(customEntity, SpawnReason.CUSTOM);

        customEntity.setCustomName("");
        customEntity.setCustomNameVisible(true);

        return (CraftHorse) customEntity.getBukkitEntity();
    }
}
