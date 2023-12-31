package com.dsd.ct.commands;

import com.dsd.ct.managers.ConfigManager;
import com.dsd.ct.util.CustomLogger;
import com.dsd.ct.util.EnumTypes;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.List;
import java.util.function.Supplier;

public class CreatorTrialsCommand {
    private static final List<String> mainOptions = ConfigManager.getInstance().getSurvivalTrialsConfigContainer().getSurvivalTrialsConfig().getCommandList();
    private static final List<String> giantsOptions = ConfigManager.getInstance().getGiantConfigContainer().getGiantConfig().getCommandList();
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(CreatorTrialsCommand.build());
        CustomLogger.getInstance().debug("Registered Command");
    }
        public static int toggleCommand(CommandContext context){
            int didSucceed = 1; //0 = failed, 1 = success
            Style style = Style.EMPTY.withColor(TextColor.parseColor("Green"));
            StringBuilder sb = new StringBuilder();
            String option = (String) context.getArgument("option", String.class);
            CommandSourceStack cs = (CommandSourceStack) context.getSource();
            EnumTypes.ModConfigOption configOption = EnumTypes.ModConfigOption.fromOptionName(option);
            sb.append(ConfigManager.getInstance().toggleMainConfigOption(configOption));

            CustomLogger.getInstance().debug(sb.toString());
            Supplier<Component> componentSupplier = () -> Component.literal(sb.toString()).setStyle(style);
            cs.sendSuccess(componentSupplier,true);
            return didSucceed;
        }

        public static int giantCommand(CommandContext context, String action, double value){
            int didSucceed = 1; //0 = failed, 1 = success
            StringBuilder sb = new StringBuilder();
            Style style = Style.EMPTY.withColor(TextColor.parseColor("Green"));
            CommandSourceStack cs = (CommandSourceStack) context.getSource();

            sb.append("Giant Command Ran - ").append(" Action: ").append(action);
            sb.append(" Value: ").append(value).append(" set");

            CustomLogger.getInstance().debug(sb.toString());
            Supplier<Component> componentSupplier = () -> Component.literal(sb.toString()).setStyle(style);
            cs.sendSuccess(componentSupplier,true);
            return didSucceed;
        }

    /************************LITERAL BUILDER FOR COMMANDS*****************************************/
    public static LiteralArgumentBuilder<CommandSourceStack> build() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("creatortrials")
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("toggle")
                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("option", StringArgumentType.string())
                                .suggests((context, builder) ->{
                                    for(String option : mainOptions){
                                        builder.suggest(option);
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    return toggleCommand(context);
                                    // Handle toggle command logic
                                })))
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("giants")
                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("action", StringArgumentType.string())
                                .suggests((context, builder) ->{
                                    for(String option : giantsOptions){
                                        builder.suggest(option);
                                    }
                                    return builder.buildFuture();
                                })
                                .then(RequiredArgumentBuilder.<CommandSourceStack, Double>argument("value", DoubleArgumentType.doubleArg())
                                        .executes(context -> {
                                            String action = context.getArgument("action", String.class);
                                            double value = context.getArgument("value", double.class);
                                            return giantCommand(context, action, value);
                                            // Handle giants command logic
                                        }))));
    }



}
