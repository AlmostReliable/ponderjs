package com.almostreliable.ponderjs.mixin;

import com.almostreliable.ponderjs.PonderLang;
import net.createmod.catnip.data.Couple;
import net.createmod.ponder.foundation.PonderIndex;
import net.createmod.ponder.foundation.registration.PonderLocalization;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.throwables.MixinException;

import java.util.Map;

@Mixin(value = PonderLocalization.class, remap = false)
public abstract class PonderLocalizationMixin {

    @Shadow @Final public Map<ResourceLocation, Map<String, String>> specific;
    @Shadow @Final public Map<ResourceLocation, Couple<String>> tag;

    @Shadow
    protected static String langKeyForSpecific(ResourceLocation sceneId, String k) {
        throw new MixinException("This is a shadow method");
    }

    @Shadow
    protected static String langKeyForTagDescription(ResourceLocation k) {
        throw new MixinException("This is a shadow method");
    }

    @Shadow
    protected static String langKeyForTag(ResourceLocation k) {
        throw new MixinException("This is a shadow method");
    }

    @Inject(method = "getTagName", at = @At("RETURN"), cancellable = true)
    public void ponderjs$getTagName(ResourceLocation tagId, CallbackInfoReturnable<String> cir) {
        if (PonderIndex.editingModeActive()) return;
        if (!PonderLang.TAGS.contains(tagId)) return;
        if (I18n.exists(langKeyForTag(tagId))) return;

        try {
            var result = tag.get(tagId).getFirst();
            cir.setReturnValue(result);
        } catch (Exception e) {
            cir.setReturnValue("PonderJS: LANG ERROR FOR TAG");
        }
    }

    @Inject(method = "getTagDescription", at = @At("RETURN"), cancellable = true)
    public void ponderjs$getTagDescription(ResourceLocation tagId, CallbackInfoReturnable<String> cir) {
        if (PonderIndex.editingModeActive()) return;
        if (!PonderLang.TAGS.contains(tagId)) return;
        if (I18n.exists(langKeyForTagDescription(tagId))) return;

        try {
            var result = tag.get(tagId).getSecond();
            cir.setReturnValue(result);
        } catch (Exception e) {
            cir.setReturnValue("PonderJS: LANG ERROR FOR TAG");
        }
    }

    @Inject(method = "getSpecific(Lnet/minecraft/resources/ResourceLocation;Ljava/lang/String;)Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    public void ponderjs$getSpecific(ResourceLocation sceneId, String k, CallbackInfoReturnable<String> cir) {
        if (PonderIndex.editingModeActive()) return;
        if (!PonderLang.SCENES.contains(sceneId)) return;
        if (I18n.exists(langKeyForSpecific(sceneId, k))) return;

        try {
            var result = specific.get(sceneId).get(k);
            cir.setReturnValue(result);
        } catch (Exception e) {
            cir.setReturnValue("PonderJS: LANG ERROR FOR SCENE");
        }
    }

    @Inject(method = "getSpecific(Lnet/minecraft/resources/ResourceLocation;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
    public void ponderjs$getSpecific(ResourceLocation sceneId, String k, Object[] params, CallbackInfoReturnable<String> cir) {
        if (PonderIndex.editingModeActive()) return;
        if (!PonderLang.SCENES.contains(sceneId)) return;
        if (I18n.exists(langKeyForSpecific(sceneId, k))) return;

        try {
            var result = String.format(specific.get(sceneId).get(k), params);
            cir.setReturnValue(result);
        } catch (Exception e) {
            cir.setReturnValue("PonderJS: LANG ERROR FOR SCENE");
        }
    }
}
