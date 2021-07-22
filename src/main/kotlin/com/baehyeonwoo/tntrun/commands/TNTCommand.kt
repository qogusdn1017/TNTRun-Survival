package com.baehyeonwoo.tntrun.commands

import com.baehyeonwoo.tntrun.TNTMain
import com.baehyeonwoo.tntrun.event.TNTListener
import com.baehyeonwoo.tntrun.tasks.StopWatchScheduler
import com.baehyeonwoo.tntrun.tasks.TNTScheduler
import io.github.monun.kommand.kommand
import net.kyori.adventure.text.Component.text
import org.bukkit.*
import org.bukkit.plugin.Plugin

object TNTCommand {
    private fun getInstance(): Plugin {
        return TNTMain.instance
    }

    var count = 0
    private val config = getInstance().config
    private val stopwatch = config.getBoolean("stopwatch")

    fun tntCommand() {
        getInstance().kommand {
            register("tntrun") {
                requires { playerOrNull != null && player.isOp }
                executes {
                    when (count) {
                        0 -> {
                            getInstance().server.scheduler.scheduleSyncRepeatingTask(getInstance(), TNTScheduler(), 0, 0)
                            getInstance().server.pluginManager.registerEvents(TNTListener(), getInstance())
                            if (stopwatch) {
                                getInstance().server.scheduler.scheduleSyncRepeatingTask(getInstance(), StopWatchScheduler(), 0, 0)
                            }
                            Bukkit.getOnlinePlayers().forEach {
                                it.sendMessage(text().content("TNT Run 야생 시작!").build())
                                it.sendTitle("${ChatColor.RED}RUN! ${ChatColor.GOLD}RUN! ${ChatColor.YELLOW}RUN!", "Run for your lives!!!", 10, 100, 10)
                                count = 1
                                it.gameMode = GameMode.SURVIVAL
                                it.playSound(it.location, Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER, 1000F, 1F)
                            }
                        }
                        1 -> {
                            Bukkit.getOnlinePlayers().forEach {
                                it.sendMessage(text().content("TNT Run 야생 중단!").build())
                                if (stopwatch) {
                                    it.sendTitle(" ", "${ChatColor.BOLD}총 플레이 타임: ${config.getString("stopwatch-value")}", 0, 150, 0)
                                }
                            }
                            count = 0
                            getInstance().server.scheduler.cancelTasks(getInstance())
                        }
                    }
                }
            }
        }
    }
}