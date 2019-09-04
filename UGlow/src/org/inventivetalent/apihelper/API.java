package org.inventivetalent.apihelper;

import org.bukkit.plugin.Plugin;

public abstract interface API
{
  public abstract void load();
  
  public abstract void init(Plugin paramPlugin);
  
  public abstract void disable(Plugin paramPlugin);
}


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/apihelper/API.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */