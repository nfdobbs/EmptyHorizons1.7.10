package com.nfdobbs.emptyhorizons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;

public class Helpers {

    public static boolean IsServerSide(EntityEvent event) {
        return !event.entity.worldObj.isRemote;
    }

    public static boolean IsPlayerEvent(EntityEvent event) {
        return event.entity instanceof EntityPlayer;
    }

}
