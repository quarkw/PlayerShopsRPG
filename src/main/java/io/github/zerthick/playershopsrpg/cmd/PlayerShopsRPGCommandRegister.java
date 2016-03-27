package io.github.zerthick.playershopsrpg.cmd;

import io.github.zerthick.playershopsrpg.cmd.cmdexecutors.shop.*;
import io.github.zerthick.playershopsrpg.cmd.cmdexecutors.shop.item.ShopCreateItemExecutor;
import io.github.zerthick.playershopsrpg.cmd.cmdexecutors.shop.item.ShopDestroyItemExecutor;
import io.github.zerthick.playershopsrpg.cmd.cmdexecutors.shop.item.ShopSetItemExecutor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

public class PlayerShopsRPGCommandRegister {

    private PluginContainer container;

    public PlayerShopsRPGCommandRegister(PluginContainer container) {
        this.container = container;
    }

    public void registerCmds() {

        // shop item set
        CommandSpec shopItemSetCommand = CommandSpec.builder()
                .description(Text.of("Set various attributes of a shop item (max amount / buy price / sell price"))
                .permission("playershopsrpg.command.item.destroy")
                .arguments(GenericArguments.choices(Text.of("SelectionType"), ShopSetItemExecutor.selectChoices()),
                        GenericArguments.integer(Text.of("ItemIndex")), GenericArguments.doubleNum(Text.of("DoubleArgument")))
                .executor(new ShopSetItemExecutor(container))
                .build();

        // shop item destroy
        CommandSpec shopItemDestroyCommand = CommandSpec.builder()
                .description(Text.of("Destroy an item in the shop you are currently standing in"))
                .permission("playershopsrpg.command.item.destroy")
                .arguments(GenericArguments.integer(Text.of("ItemIndex")))
                .executor(new ShopDestroyItemExecutor(container))
                .build();

        // shop item create
        CommandSpec shopItemCreateCommand = CommandSpec.builder()
                .description(Text.of("Create an item in the shop you are currently standing in"))
                .permission("playershopsrpg.command.item.create")
                .executor(new ShopCreateItemExecutor(container))
                .build();

        // shop item
        CommandSpec shopItemCommand = CommandSpec.builder()
                .permission("playershopsrpg.command.item")
                .child(shopItemCreateCommand, "create")
                .child(shopItemDestroyCommand, "destroy")
                .child(shopItemSetCommand, "set")
                .build();

        // shop set name <name>
        CommandSpec shopSetNameCommmand = CommandSpec.builder()
                .description(Text.of("Set the name of the shop you are currently standing in"))
                .permission("playershopsrpg.command.set.name")
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("NameArgument")))
                .executor(new ShopSetNameExecutor(container))
                .build();

        // shop set owner <user>
        CommandSpec shopSetOwnerCommand = CommandSpec.builder()
                .description(Text.of("Set the owner of the shop you are currently standing in"))
                .permission("playershopsrpg.command.set.owner")
                .arguments(GenericArguments.user(Text.of("UserArgument")))
                .executor(new ShopSetOwnerExecutor(container))
                .build();

        // shop set unlimited [stock | money] <bool>
        CommandSpec shopSetUnlimitedCommand = CommandSpec.builder()
                .description(Text.of("Set the shop you are currently standing in to have unlimited stock or money"))
                .permission("playershopsrpg.command.set.unlimited")
                .arguments(GenericArguments.choices(Text.of("SelectionType"), ShopSetUnlimitedExecutor.selectChoices()), GenericArguments.bool(Text.of("BooleanArgument")))
                .executor(new ShopSetUnlimitedExecutor(container))
                .build();

        // shop set
        CommandSpec shopSetCommand = CommandSpec.builder()
                .permission("playershopsrpg.command.set")
                .child(shopSetUnlimitedCommand, "unlimited")
                .child(shopSetOwnerCommand, "owner")
                .child(shopSetNameCommmand, "name")
                .build();

        // shop browse
        CommandSpec shopBrowseCommand = CommandSpec.builder()
                .description(Text.of("Browses the shop you are currently standing in"))
                .permission("playershopsrpg.command.browse")
                .executor(new ShopBrowseExecutor(container))
                .build();

        // shop destroy
        CommandSpec shopDestroyCommand = CommandSpec.builder()
                .description(Text.of("Destroys the shop you are currently standing in"))
                .permission("playershopsrpg.command.destroy")
                .executor(new ShopDestroyExecutor(container))
                .build();

        // shop create <Name>
        CommandSpec shopCreateCommand = CommandSpec.builder()
                .description(Text.of("Creates a shop in the region selected by shop select command"))
                .permission("playershopsrpg.command.create")
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("ShopName")))
                .executor(new ShopCreateExecutor(container))
                .build();

        // shop select
        CommandSpec shopSelectCommmand = CommandSpec.builder()
                .description(Text.of("Selects a region to create a shop"))
                .permission("playershopsrpg.command.select")
                .arguments(GenericArguments.optional(GenericArguments.choices(Text.of("SelectionType"), ShopSelectExecutor.selectChoices())))
                .executor(new ShopSelectExecutor(container))
                .build();

        // shop
        CommandSpec shopCommand = CommandSpec.builder()
                .permission("playershopsrpg.command.help")
                .executor(new ShopExecutor(container))
                .child(shopSelectCommmand, "select")
                .child(shopCreateCommand, "create")
                .child(shopDestroyCommand, "destroy")
                .child(shopBrowseCommand, "browse")
                .child(shopItemCommand, "item")
                .child(shopSetCommand, "set")
                .build();

        Sponge.getGame().getCommandManager().register(container.getInstance().get(), shopCommand, "shop");
    }
}
