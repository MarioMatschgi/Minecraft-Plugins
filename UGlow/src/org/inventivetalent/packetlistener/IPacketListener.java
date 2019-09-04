package org.inventivetalent.packetlistener;

import org.bukkit.event.Cancellable;

public abstract interface IPacketListener
{
  public abstract Object onPacketSend(Object paramObject1, Object paramObject2, Cancellable paramCancellable);
  
  public abstract Object onPacketReceive(Object paramObject1, Object paramObject2, Cancellable paramCancellable);
}


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/IPacketListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */