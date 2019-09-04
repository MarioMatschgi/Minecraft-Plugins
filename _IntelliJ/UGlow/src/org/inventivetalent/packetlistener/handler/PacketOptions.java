package org.inventivetalent.packetlistener.handler;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketOptions
{
  boolean forcePlayer() default false;
  
  boolean forceServer() default false;
}


/* Location:              /Users/Mario/Downloads/uGlow-0.13.0.jar!/org/inventivetalent/packetlistener/handler/PacketOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */