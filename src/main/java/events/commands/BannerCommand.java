package events.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.utils.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.Constant;

import java.awt.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BannerCommand extends Command{
    private static final Logger log = LoggerFactory.getLogger(BannerCommand.class);

    @Override
    public void execute(MessageReceivedEvent event) {

        final String command = removePrefix(event.getMessage().getContentRaw());
        MessageChannel channel = event.getChannel();

        if (command.isEmpty()) {
            event.getJDA().retrieveUserById(event.getAuthor().getIdLong()).queue(member -> {

                member.retrieveProfile().queue(profile -> {

                    if (profile.getBanner() != null) {

                        if (profile.getBannerUrl().contains(".gif")) {
                            profile.getBanner().downloadToFile(new File("/Bot/Files/"+member.getName()+".gif"),600).whenComplete((file, throwable) -> {

                                try (FileUpload upload = FileUpload.fromData(file,file.getName())){

                                    MessageEmbed embed = new EmbedBuilder()
                                            .setColor(Color.decode(Constant.default_color))
                                            .setAuthor(member.getEffectiveName()+" profile banner",null,member.getEffectiveAvatarUrl())
                                            .setImage("attachment://"+file.getName())
                                            .build();
                                    channel.sendFiles(upload).setEmbeds(embed).queue();

                                }catch (Exception e) {
                                    log.error(e.getMessage());
                                }

                                if (file.delete()) {
                                    System.out.println("file deleted!");
                                }else System.out.println("failed to delete the file");


                            });

                        }else {
                            profile.getBanner().downloadToFile(new File("/Bot/Files/"+member.getName()+".gif"),600).whenComplete((file, throwable) -> {

                                try (FileUpload upload = FileUpload.fromData(file,file.getName())){

                                    MessageEmbed embed = new EmbedBuilder()
                                            .setColor(Color.decode(Constant.default_color))
                                            .setAuthor(member.getEffectiveName()+" profile banner",null,member.getEffectiveAvatarUrl())
                                            .setImage("attachment://"+file.getName())
                                            .build();
                                    channel.sendFiles(upload).setEmbeds(embed).queue();

                                }catch (Exception e) {
                                    log.error(e.getMessage());
                                }

                                if (file.delete()) {
                                    System.out.println("file deleted!");
                                }else System.out.println("failed to delete the file");


                            });
                        }
                    }else channel.sendMessage("You have no banner.").queue();


                });


            });
        }

        if (command.startsWith("<@")) {
            Pattern pattern = Pattern.compile("<@(\\d+)>");
            Matcher matcher = pattern.matcher(event.getMessage().getContentRaw());

            if (matcher.find()) {
                long id = Long.parseLong(matcher.group(1));
                event.getJDA().retrieveUserById(id).queue(member -> {

                    member.retrieveProfile().queue(profile -> {

                        if (profile.getBanner() != null) {

                            if (profile.getBannerUrl().contains(".gif")) {
                                profile.getBanner().downloadToFile(new File("/Bot/Files/"+member.getName()+".gif"),600).whenComplete((file, throwable) -> {

                                    try (FileUpload upload = FileUpload.fromData(file,file.getName())){

                                        MessageEmbed embed = new EmbedBuilder()
                                                .setColor(Color.decode(Constant.default_color))
                                                .setAuthor(member.getEffectiveName()+" profile banner",null,member.getEffectiveAvatarUrl())
                                                .setImage("attachment://"+file.getName())
                                                .build();
                                        channel.sendFiles(upload).setEmbeds(embed).queue();

                                    }catch (Exception e) {
                                        log.error(e.getMessage());
                                    }

                                    if (file.delete()) {
                                        System.out.println("file deleted!");
                                    }else System.out.println("failed to delete the file");


                                });

                            }else {
                                profile.getBanner().downloadToFile(new File("/Bot/Files/"+member.getName()+".gif"),600).whenComplete((file, throwable) -> {

                                    try (FileUpload upload = FileUpload.fromData(file,file.getName())){

                                        MessageEmbed embed = new EmbedBuilder()
                                                .setColor(Color.decode(Constant.default_color))
                                                .setAuthor(member.getEffectiveName()+" profile banner",null,member.getEffectiveAvatarUrl())
                                                .setImage("attachment://"+file.getName())
                                                .build();
                                        channel.sendFiles(upload).setEmbeds(embed).queue();

                                    }catch (Exception e) {
                                        log.error(e.getMessage());
                                    }

                                    if (file.delete()) {
                                        System.out.println("file deleted!");
                                    }else System.out.println("failed to delete the file");


                                });
                            }
                        }else channel.sendMessage(member.getName()+" has no banner").queue();


                    });


                },new ErrorHandler().handle(ErrorResponse.UNKNOWN_USER, e -> channel.sendMessage("User not Found!").queue()));


            }
        }else {
            event.getJDA().retrieveUserById(command).queue(member -> {

                member.retrieveProfile().queue(profile -> {

                    if (profile.getBanner() != null) {

                        if (profile.getBannerUrl().contains(".gif")){
                            profile.getBanner().downloadToFile(new File("/Bot/Files/"+member.getName()+".gif"),600).whenComplete((file, throwable) -> {

                                try (FileUpload upload = FileUpload.fromData(file,file.getName())){

                                    MessageEmbed embed = new EmbedBuilder()
                                            .setColor(Color.decode(Constant.default_color))
                                            .setAuthor(member.getEffectiveName()+" profile banner",null,member.getEffectiveAvatarUrl())
                                            .setImage("attachment://"+file.getName())
                                            .build();
                                    channel.sendFiles(upload).setEmbeds(embed).queue();

                                }catch (Exception e) {
                                    log.error(e.getMessage());
                                }

                                if (file.delete()) {
                                    System.out.println("file deleted!");
                                }else System.out.println("failed to delete the file");


                            });

                        }else {
                            profile.getBanner().downloadToFile(new File("/Bot/Files/"+member.getName()+".png"),600).whenComplete((file, throwable) -> {

                                try (FileUpload upload = FileUpload.fromData(file,file.getName())){

                                    MessageEmbed embed = new EmbedBuilder()
                                            .setColor(Color.decode(Constant.default_color))
                                            .setAuthor(member.getEffectiveName()+" profile banner",null,member.getEffectiveAvatarUrl())
                                            .setImage("attachment://"+file.getName())
                                            .build();
                                    channel.sendFiles(upload).setEmbeds(embed).queue();

                                }catch (Exception e) {
                                    log.error(e.getMessage());
                                }

                                if (file.delete()) {
                                    System.out.println("file deleted!");
                                }else System.out.println("failed to delete the file");


                            });
                        }
                    }else channel.sendMessage(member.getName()+" has no banner").queue();


                });

            },new ErrorHandler().handle(ErrorResponse.UNKNOWN_USER,e -> channel.sendMessage("User not Found!").queue()));
        }


    }

    @Override
    public String getName() {
        return "banner";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"bg","background"};
    }

    private String removePrefix(String message) {

        return message.substring(Constant.prefix.length() + getName().length()).strip();

    }
}
