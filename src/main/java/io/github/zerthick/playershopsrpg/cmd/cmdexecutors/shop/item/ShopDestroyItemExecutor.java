/*
 * Copyright (C) 2016  Zerthick
 *
 * This file is part of PlayerShopsRPG.
 *
 * PlayerShopsRPG is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * PlayerShopsRPG is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PlayerShopsRPG.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zerthick.playershopsrpg.cmd.cmdexecutors.shop.item;

import com.google.common.collect.ImmutableMap;
import io.github.zerthick.playershopsrpg.PlayerShopsRPG;
import io.github.zerthick.playershopsrpg.cmd.cmdexecutors.AbstractShopTransactionCmdExecutor;
import io.github.zerthick.playershopsrpg.cmd.cmdexecutors.CommandArgs;
import io.github.zerthick.playershopsrpg.shop.ShopItem;
import io.github.zerthick.playershopsrpg.shop.ShopTransactionResult;
import io.github.zerthick.playershopsrpg.utils.config.sql.SQLDataUtil;
import io.github.zerthick.playershopsrpg.utils.messages.Messages;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class ShopDestroyItemExecutor extends AbstractShopTransactionCmdExecutor {

    public ShopDestroyItemExecutor(PlayerShopsRPG plugin) {
        super(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        return super.executeTransaction(src, args, (player, arg, shop) -> {

            Optional<Integer> itemIndexArgumentOptional = arg.getOne(CommandArgs.ITEM_INDEX);

            if (itemIndexArgumentOptional.isPresent()) {
                Optional<ShopItem> shopItem = shop.getShopItem(itemIndexArgumentOptional.get());
                ShopTransactionResult transactionResult = shop.destroyItem(player, itemIndexArgumentOptional.get());

                if (transactionResult == ShopTransactionResult.SUCCESS) {
                    shopItem.ifPresent(item -> SQLDataUtil.deleteShopItem(item.getShopItemUUID(), plugin.getLogger()));
                    player.sendMessage(ChatTypes.CHAT, Text.of(TextColors.BLUE, Messages.processDropins(Messages.DESTROY_ITEM_SUCCESS,
                            ImmutableMap.of(Messages.DROPIN_SHOP_NAME, shop.getName(), Messages.DROPIN_ITEM_INDEX,
                                    String.valueOf(itemIndexArgumentOptional.get())))));
                }

                return transactionResult;
            }
            return ShopTransactionResult.EMPTY;
        }, Messages.DESTROY_ITEM_CONSOLE_REJECT);
    }
}
