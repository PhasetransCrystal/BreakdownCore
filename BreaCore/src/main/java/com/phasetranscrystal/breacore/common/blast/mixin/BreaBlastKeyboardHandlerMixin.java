package com.phasetranscrystal.breacore.common.blast.mixin;

import com.phasetranscrystal.breacore.common.blast.player.KeyInput;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO
// @Mixin(KeyboardHandler.class)
public abstract class BreaBlastKeyboardHandlerMixin {

    // @Inject(method = "keyPress", at = @At(value = "INVOKE_ASSIGN", target =
    // "Lcom/mojang/blaze3d/platform/Window;getWindow()J", ordinal = 0), cancellable = true)
    public void onKeyPress_BreaBlast(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        // if (NeoForge.EVENT_BUS.post(new KeyInputEvent.Client(key, scanCode, action, modifiers)).isCanceled()) {
        // ci.cancel();
        // }

        KeyInput.clientClickCheck(key, modifiers, action, ci::cancel);
    }
}
