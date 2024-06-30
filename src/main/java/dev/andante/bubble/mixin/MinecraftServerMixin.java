package dev.andante.bubble.mixin;

import dev.andante.bubble.world.IBubbleWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow @Final public Map<RegistryKey<World>, ServerWorld> worlds;

    /**
     * Removes bubble worlds before saving.
     */
    @Redirect(
            method = "save",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;getWorlds()Ljava/lang/Iterable;",
                    ordinal = 0
            )
    )
    private Iterable<ServerWorld> onSaveWorlds(MinecraftServer server) {
        return this.worlds
                .values()
                .stream()
                .filter(world -> !(world instanceof IBubbleWorld))
                .toList();
    }
}
