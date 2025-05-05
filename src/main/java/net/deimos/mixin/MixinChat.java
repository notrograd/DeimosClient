package net.deimos.mixin;

import net.deimos.api.cmd.CommandRegistry;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinChat {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        if (message.startsWith("@") && message.length() > 1) {
            String[] parts = message.substring(1).split(" ");
            String commandName = parts[0];

            String[] args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, args.length);

            if (CommandRegistry.executeCommand(commandName, args)) {
                ci.cancel();
            }
        }
    }
}