package Gdx_Risk

import com.github.czyzby.lml.parser.LmlParser
import com.github.czyzby.lml.util.Lml
import com.github.czyzby.lml.vis.util.VisLml
import com.kotcrab.vis.ui.VisUI

class _UI {
    init {
        VisUI.load()
        val test = VisLml.parser().build()!!
//        val view = test.createView()
    }
}