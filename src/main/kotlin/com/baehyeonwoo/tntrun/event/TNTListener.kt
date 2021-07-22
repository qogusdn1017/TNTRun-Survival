/*
 * Copyright (c) 2021 BaeHyeonWoo
 *
 *  Licensed under the General Public License, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/gpl-3.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baehyeonwoo.tntrun.event

import com.baehyeonwoo.tntrun.TNTMain
import com.baehyeonwoo.tntrun.commands.TNTCommand
import io.github.monun.tap.effect.playFirework
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityPortalEnterEvent
import org.bukkit.plugin.Plugin

/***
 * @author BaeHyeonWoo
 */

class TNTListener : Listener {

    private fun getInstance(): Plugin {
        return TNTMain.instance
    }

    private val config = getInstance().config

    private val stopwatch = config.getBoolean("stopwatch")

    private val ending = config.getBoolean("ending")

    private val blockEdgeDetection = config.getBoolean("block-edge-detection")

    private val instantPortal = config.getBoolean("instant-portal")

    @EventHandler
    fun onEntityDeathEvent(e: EntityDeathEvent) {
        if (ending) {
            val entity = e.entity
            val firework = FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.AQUA).build()

            fun dragonEnd() {
                val loc = entity.killer?.location?.add(0.0, 0.9, 0.0)
                loc?.world?.playFirework(loc, firework)
                loc?.world?.playFirework(loc, firework)
                loc?.world?.playFirework(loc, firework)
            }

            fun endTitle() {
                Bukkit.getOnlinePlayers().forEach {
                    it.sendTitle("${ChatColor.YELLOW}${ChatColor.BOLD}TNT Run 야생 종료!", "", 0, 150, 0)
                    it.playSound(it.location, Sound.ITEM_TOTEM_USE, SoundCategory.MASTER, 1000F, 1F)
                }
            }

            fun printPlayTime() {
                getInstance().server.scheduler.runTaskLater(getInstance(), Runnable {
                    Bukkit.getOnlinePlayers().forEach {
                        it.sendTitle(" ", "총 플레이 타임: ${config.getString("stopwatch-value")}", 0, 150, 0)
                    }
                }, 120)
            }

            if (entity.type == EntityType.ENDER_DRAGON) {
                if (entity.killer is Player) {
                    dragonEnd()
                    endTitle()
                    getInstance().server.scheduler.cancelTasks(getInstance())
                    getInstance().server.scheduler.runTaskLater(getInstance(), Runnable {
                        Bukkit.getOnlinePlayers().forEach {
                            it.sendTitle(" ", "드래곤 잡으신 분: ${entity.killer?.name}", 0, 150, 0)
                        }
                    }, 60)
                    if (stopwatch) {
                        printPlayTime()
                    }
                    TNTCommand.count = 0
                    HandlerList.unregisterAll(getInstance())
                } else {
                    endTitle()
                    getInstance().server.scheduler.cancelTasks(getInstance())
                    if (stopwatch) {
                        printPlayTime()
                    }
                    TNTCommand.count = 0
                    HandlerList.unregisterAll(getInstance())
                }
            }
        }
    }

    @EventHandler
    fun onEntityPortalEnterEvent(e: EntityPortalEnterEvent) {
        if (instantPortal) {
            if (blockEdgeDetection) {
                if (e.entity is Player) {
                    val p = e.entity as Player
                    p.gameMode = GameMode.CREATIVE
                    getInstance().server.scheduler.runTaskLater(getInstance(), Runnable {
                        p.gameMode = GameMode.SURVIVAL
                    }, 3)
                }
            }
        }
    }
}