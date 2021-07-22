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
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

/***
 * @author BaeHyeonWoo
 */


class StopWatchScheduler : Runnable {

    private fun getInstance(): Plugin {
        return TNTMain.instance
    }

    private val hours = HashMap<String, Int>()
    private val minutes = HashMap<String, Int>()
    private val seconds = HashMap<String, Int>()
    private val milliseconds = HashMap<String, Int>()
    private val keystring = "keystring"
    private val systemTime = System.currentTimeMillis().toInt() / 10

    private fun getStopWatch(): String {
        val nowhr = hours[keystring]
        val nowm = minutes[keystring]
        val nows = seconds[keystring]
        val nowms = milliseconds[keystring]
        return("$nowhr:$nowm:$nows.$nowms")
    }

    private fun saveStopWatch() {
        val config = getInstance().config
        config.set("stopwatch-value", getStopWatch())
    }
    
    override fun run() {
        val currentTime = System.currentTimeMillis().toInt() / 10
        val reachTime = currentTime - systemTime
        val ms = reachTime % 100
        val mm = reachTime / 100
        val sec = mm % 60
        val min = mm / 60 % 60
        val hr = mm / 3600

        hours[keystring] = hr
        minutes[keystring] = min
        seconds[keystring] = sec
        milliseconds[keystring] = ms

        // https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=rain483&logNo=220661508094

        Bukkit.getOnlinePlayers().forEach {
            it.sendActionBar(Component.text().content("현재 플레이 타임: ${getStopWatch()}").build())
        }
        saveStopWatch()
    }
}