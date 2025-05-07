package net.deimos.api.anticheat;

import net.deimos.api.event.Event;
import net.minecraft.network.packet.Packet;
/**
 * Bypasses catered specifically to the NoCheatPlus Anticheat.
 */
public class NoCheatPlus {
    public static class StrictRotation extends Event {
        public int pitch,yaw;
        private Packet<?> p;
        public StrictRotation(int pitch, int yaw, Packet<?> p)
        {
            this.pitch = pitch;
            this.yaw = yaw;
            this.p = p;
        }

        public Packet<?> getPacket() {
            return p;
        }
    }

}
