package fi.dy.masa.malilib.action;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import fi.dy.masa.malilib.config.option.BooleanConfig;
import fi.dy.masa.malilib.config.option.HotkeyedBooleanConfig;
import fi.dy.masa.malilib.input.ActionResult;
import fi.dy.masa.malilib.listener.EventListener;
import fi.dy.masa.malilib.overlay.message.MessageOutput;
import fi.dy.masa.malilib.registry.Registry;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.data.ModInfo;

public class ActionUtils
{

    public static ActionResult runVanillaCommand(ActionContext ctx, String arg)
    {
        if (ctx.getPlayer() != null)
        {
            ctx.getPlayer().sendChatMessage(arg);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    public static SimpleNamedAction createToggleActionWithToggleMessage(ModInfo mod, String name, BooleanConfig config)
    {
        return createToggleActionWithToggleMessage(mod, name, config, null);
    }

    public static SimpleNamedAction createToggleActionWithToggleMessage(ModInfo mod, String name, BooleanConfig config,
                                                                        @Nullable Function<BooleanConfig, String> messageFactory)
    {
        return SimpleNamedAction.of(mod, name, BooleanToggleAction.of(config, messageFactory));
    }

    public static SimpleNamedAction createToggleActionWithToggleMessage(ModInfo mod, String name, BooleanConfig config,
                                                                  @Nullable Function<BooleanConfig, String> messageFactory,
                                                                  @Nullable Supplier<MessageOutput> messageTypeSupplier)
    {
        return SimpleNamedAction.of(mod, name, BooleanToggleAction.of(config, messageFactory, messageTypeSupplier));
    }

    public static SimpleNamedAction register(ModInfo modInfo, String name, EventListener action)
    {
        SimpleNamedAction namedAction = SimpleNamedAction.of(modInfo, name, action);
        Registry.ACTION_REGISTRY.registerAction(namedAction);
        return namedAction;
    }

    public static SimpleNamedAction register(ModInfo modInfo, String name, Action action)
    {
        SimpleNamedAction namedAction = SimpleNamedAction.of(modInfo, name, action);
        Registry.ACTION_REGISTRY.registerAction(namedAction);
        return namedAction;
    }

    public static NamedParameterizableAction register(ModInfo modInfo, String name, ParameterizedAction action)
    {
        NamedParameterizableAction namedAction = NamedParameterizableAction.of(modInfo, name, action);
        Registry.ACTION_REGISTRY.registerAction(namedAction);
        return namedAction;
    }

    public static SimpleNamedAction registerToggle(ModInfo modInfo, String name, BooleanConfig config)
    {
        return registerToggle(modInfo, name, config, null, null);
    }

    public static SimpleNamedAction registerToggle(ModInfo modInfo, String name, BooleanConfig config,
                                                   @Nullable Function<BooleanConfig, String> messageFactory,
                                                   @Nullable Supplier<MessageOutput> messageTypeSupplier)
    {
        SimpleNamedAction namedAction = createToggleActionWithToggleMessage(modInfo, name, config,
                                                                            messageFactory, messageTypeSupplier);
        namedAction.setCommentTranslationKey(config.getCommentTranslationKey());
        Registry.ACTION_REGISTRY.registerAction(namedAction);
        return namedAction;
    }

    public static SimpleNamedAction registerToggleKey(ModInfo modInfo, String name, HotkeyedBooleanConfig config)
    {
        SimpleNamedAction namedAction = SimpleNamedAction.of(modInfo, name, config.getToggleAction());
        Registry.ACTION_REGISTRY.registerAction(namedAction);
        return namedAction;
    }

    /**
     * Constructs the default registry name for the given action,
     * in the format "modid:action_name".
     */
    public static String createRegistryNameFor(ModInfo modInfo, String name)
    {
        return modInfo.getModId() + ":" + name;
    }

    /**
     * Constructs the default translation key for the given action.
     * Tries, in order, the keys in the format "modid.action.name.action_name",
     * "modid.hotkey.name.action_name" and "modid.config.name.action_name"
     * to see which one has a translation.
     * If none of them do, then the name is returned as-is.
     */
    public static String createTranslationKeyFor(ModInfo modInfo, String name)
    {
        String modId = modInfo.getModId();
        String key = modId + ".action.name." + name.toLowerCase(Locale.ROOT);

        if (StringUtils.translate(key).equals(key) == false)
        {
            return key;
        }

        key = modId + ".hotkey.name." + name.toLowerCase(Locale.ROOT);

        if (StringUtils.translate(key).equals(key) == false)
        {
            return key;
        }

        key = modId + ".config.name." + name.toLowerCase(Locale.ROOT);

        if (StringUtils.translate(key).equals(key) == false)
        {
            return key;
        }

        return name;
    }
}
