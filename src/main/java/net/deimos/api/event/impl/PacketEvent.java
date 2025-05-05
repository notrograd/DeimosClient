package net.deimos.api.event.impl;

import net.deimos.api.event.Event;
import net.minecraft.network.packet.Packet;

public class PacketEvent {
    public static class Receive extends Event {
        Packet<?> p;
        public Receive(Packet<?> p){
            this.p = p;
        }

        public boolean isCancelled=false;

        public void cancel()
        {
            if (!this.isCancelled) this.isCancelled = true;
        }

        public Packet<?> getPacket() {
            return p;
        }
    }
}
