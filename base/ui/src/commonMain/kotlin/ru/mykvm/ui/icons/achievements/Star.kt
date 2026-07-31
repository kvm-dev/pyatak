package ru.mykvm.ui.icons.achievements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

public val Star: ImageVector
    get() {
        if (_star != null) {
            return _star!!
        }
        _star = Builder(name = "Star", defaultWidth = 10.0.dp, defaultHeight = 10.0.dp,
                viewportWidth = 10.0f, viewportHeight = 10.0f).apply {
            group {
                path(fill = SolidColor(Color(0xFFffffff)), stroke = SolidColor(Color(0xFFffffff)),
                        strokeLineWidth = 1.0f, strokeLineCap = Round, strokeLineJoin =
                        StrokeJoin.Round, strokeLineMiter = 4.0f, pathFillType =
                        NonZero) {
                    moveTo(4.9229f, 0.8205f)
                    lineTo(6.1906f, 3.3887f)
                    lineTo(9.0254f, 3.8031f)
                    lineTo(6.9742f, 5.801f)
                    lineTo(7.4583f, 8.6236f)
                    lineTo(4.9229f, 7.2903f)
                    lineTo(2.3875f, 8.6236f)
                    lineTo(2.8716f, 5.801f)
                    lineTo(0.8203f, 3.8031f)
                    lineTo(3.6552f, 3.3887f)
                    lineTo(4.9229f, 0.8205f)
                    close()
                }
            }
        }
        .build()
        return _star!!
    }

private var _star: ImageVector? = null

@Preview
@Composable
private fun Preview(): Unit {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Star, contentDescription = "")
    }
}

