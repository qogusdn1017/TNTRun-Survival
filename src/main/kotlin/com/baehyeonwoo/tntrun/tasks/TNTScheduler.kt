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

package com.baehyeonwoo.tntrun.tasks
import com.baehyeonwoo.tntrun.TNTMain
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.Plugin
import org.bukkit.util.NumberConversions

/***
 * @author BaeHyeonWoo
 */

class TNTScheduler : Runnable {

    private fun getInstance(): Plugin {
        return TNTMain.instance
    }

    private val config = getInstance().config
    
    private val blockEdgeDetection = config.getBoolean("block-edge-detection")

    override fun run() {
        Bukkit.getOnlinePlayers().forEach {
            val pos1 = it.location.subtract(0.0, 0.01, 0.0)
            val pos2 = pos1.clone().subtract(0.0, 1.0, 0.0)
            val pos = pos1.takeIf { pos -> !pos.block.type.isAir } ?: pos2
            if (blockEdgeDetection) {
                val y = pos.blockY
                val minX = NumberConversions.floor(it.boundingBox.minX)
                val maxX = NumberConversions.floor(it.boundingBox.maxX)
                val minZ = NumberConversions.floor(it.boundingBox.minZ)
                val maxZ = NumberConversions.floor(it.boundingBox.maxZ)
                getInstance().server.scheduler.runTaskLater(getInstance(), Runnable {
                    for (x in minX..maxX) {
                        for (z in minZ..maxZ) {
                            val edgeBlock = it.world.getBlockAt(x, y, z)
                            if (!edgeBlock.type.isAir) {
                                edgeBlock.type = Material.AIR
                            }
                        }
                    }
                }, 15)
            }
            getInstance().server.scheduler.runTaskLater(getInstance(), Runnable {
                pos.block.type = Material.AIR
            }, 15)
        }
    }
}