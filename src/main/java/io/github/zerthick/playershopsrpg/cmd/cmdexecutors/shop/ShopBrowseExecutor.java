package io.github.zerthick.playershopsrpg.cmd.cmdexecutors.shop;

import io.github.zerthick.playershopsrpg.cmd.cmdexecutors.AbstractCmdExecutor;
import io.github.zerthick.playershopsrpg.shop.Shop;
import io.github.zerthick.playershopsrpg.shop.ShopContainer;
import io.github.zerthick.playershopsrpg.shop.ShopTransactionResult;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShopBrowseExecutor extends AbstractCmdExecutor {

    public ShopBrowseExecutor(PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    public static Map<String, String> selectChoices() {
        Map<String, String> selectChoices = new HashMap<>();
        selectChoices.put("manager", "manager");
        selectChoices.put("owner", "owner");
        return selectChoices;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            Player player = (Player) src;

            String selectType = "default";

            Optional<String> selectTypeOptional = args.getOne(Text.of("SelectionType"));

            if (selectTypeOptional.isPresent()) {
                selectType = selectTypeOptional.get();
            }

            Optional<ShopContainer> shopContainerOptional = shopManager.getShop(player);
            if (shopContainerOptional.isPresent()) {
                ShopContainer shopContainer = shopContainerOptional.get();
                Shop shop = shopContainer.getShop();
                ShopTransactionResult transactionResult = ShopTransactionResult.EMPTY;
                switch (selectType) {
                    case "default":
                        transactionResult = shop.showBuyView(player);
                        break;
                    case "manager":
                        transactionResult = shop.showManagerView(player);
                        break;
                    case "owner":
                        transactionResult = shop.showOwnerView(player);
                }
                if (transactionResult != ShopTransactionResult.SUCCESS) {
                    player.sendMessage(ChatTypes.CHAT, Text.of(TextColors.RED, transactionResult.getMessage()));
                }
            } else {
                player.sendMessage(ChatTypes.CHAT, Text.of(TextColors.RED, "You are not in a shop!"));
            }
            return CommandResult.success();
        }

        src.sendMessage(Text.of("You cannot browse shops from the console!"));
        return CommandResult.success();
    }
}
