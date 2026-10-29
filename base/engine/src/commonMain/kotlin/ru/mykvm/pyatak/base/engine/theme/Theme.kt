package ru.mykvm.pyatak.base.engine.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import ru.mykvm.pyatak.base.engine.ComposeResources.Lato_Black
import ru.mykvm.pyatak.base.engine.ComposeResources.Lato_BlackItalic
import ru.mykvm.pyatak.base.engine.ComposeResources.Lato_Bold
import ru.mykvm.pyatak.base.engine.ComposeResources.Lato_BoldItalic
import ru.mykvm.pyatak.base.engine.ComposeResources.Lato_Italic
import ru.mykvm.pyatak.base.engine.ComposeResources.Lato_Light
import ru.mykvm.pyatak.base.engine.ComposeResources.Lato_LightItalic
import ru.mykvm.pyatak.base.engine.ComposeResources.Lato_Regular
import ru.mykvm.pyatak.base.engine.ComposeResources.Res

class AppColors {
    val selectedNavigationColor = Color(0xFF145E5E)
    val unselectedNavigationColor = Color(0xFFCDD2DF)
    val greenLight = Color(14, 171, 150)
    val unfocused = Color(189, 189, 189)
    val mainDark = Color(23, 23, 37)
    val mainError = Color(193, 15, 15)
    val simplyWhite = Color(0xFFFFFFF0)
    val unselectedBorderChipColor = Color(0xFFD4CFCF)
    val inputBorder = Color(0xB9B3AAAA)
    val simplyBlack = Color(0xFF000000)
    val turquoise = Color(83, 170, 172)
    val mainYellowLight = Color(0xFFFFFFD700)
    val costYellow = Color(0xFFDC6C0A)
    val mainOrangeLight = Color(0xFFEF6C00)
    val mainMenuBackgroundLight = Color(red = 241, green = 154, blue = 37)
    val mainGreenLight = Color(0xFF16A085)
    val mainBlueGreen = Color(0xFF0EC1A9)
    val mainGreyLight = Color(0xFF696969)
    val mainBlackLight = Color(0xFF1E1E1E)
    val mainBackgroundLight = Color(0xFF99E6B3)

    val mainSplashDark = Color(0xFF012B5C)
    val secondSplashDark = Color(0xFF036480)

    // shimmers colors
    val shimmerColor1 = Color(0xFF747272)
    val shimmerColor2 = Color(0xFF6C6C6C)
    val shimmerColor3 = Color(0xFF797878)
    val shimmerColor4 = Color(0xFF929090)
    val shimmerColor5 = Color(0xFFA8A4A4)
    val shimmerColor6 = Color(0xFFD4D1D1)
    val shimmerColor7 = Color(0xFFEAE9E9)

    // colors with transparent
    val mainOrangeLightWithTransparent = Color(0x7CEF6C00)
    val simplyWhiteWithTransparent = Color(0x23FFFFF0)
    val headerMainColorWithTransparent = Color(0xCC0E0230)
    val disabledButtonBackground = Color(112, 112, 112, 63)
    val bottomSheetBackground = Color(14, 2, 48, 85)
    val achievementBackground = Color(red = 14, green = 193, blue = 169, alpha = 26)
    val mainSubNavigationBackground = Color(red = 255, green = 255, blue = 255, alpha = 12)
    val mainBlueGreenTransparent = Color(0xB30EC1A9)
    val shimmerColor1WithTransparent = Color(0x4A747272)
    val shimmerColor2WithTransparent = Color(0x466C6C6C)
    val shimmerColor3WithTransparent = Color(0x4D797878)
    val shimmerColor4WithTransparent = Color(0x7A929090)
    val shimmerColor5WithTransparent = Color(0xF3A8A4A4)
    val shimmerColor6WithTransparent = Color(0x7ED4D1D1)
    val shimmerColor7WithTransparent = Color(0x59EAE9E9)
}

class AppTypography(fontFamily: FontFamily, colors: AppColors) {

    val bottomSheetTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontStyle = Italic,
        fontSize = 14.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val bottomSheetDescription = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = colors.greenLight,
        textAlign = TextAlign.Center
    )

    val textFieldPlaceholder = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = colors.unfocused,
        textAlign = TextAlign.Start
    )

    val textFieldError = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 16.sp,
        color = colors.mainError,
        textAlign = TextAlign.Center
    )

    val mainScreenEventsNotFound = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colors.simplyBlack,
        textAlign = TextAlign.Center
    )
    val otpChar = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        color = colors.mainDark,
        textAlign = TextAlign.Center
    )

    val startTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Start
    )

    val transparentButton = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val questionFromList = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = colors.simplyBlack
    )

    val answerFromList = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colors.simplyBlack
    )

    val serviceTag = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val disabledServiceTag = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = colors.inputBorder,
        textAlign = TextAlign.Center
    )

    val serviceText = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Start
    )

    val serviceTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val screenTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val screenSubTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val testQuestion = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Start
    )

    val chips = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 14.sp,
        color = colors.simplyBlack,
        fontStyle = Italic,
        textAlign = TextAlign.Center
    )

    val bottomSheetButton = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val bottomSheetButtonBlack = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = colors.simplyBlack,
        textAlign = TextAlign.Center
    )

    val notificationTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val boldItalicLabel = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontStyle = Italic,
        fontSize = 14.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val textArea = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Start
    )

    val advText = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = colors.simplyWhite,
        fontStyle = Italic,
        textAlign = TextAlign.Center
    )

    val placeholder = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colors.inputBorder,
        textAlign = TextAlign.Start
    )

    val bookTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Black,
        fontStyle = Italic,
        fontSize = 14.sp,
        color = colors.simplyBlack,
        textAlign = TextAlign.Start
    )

    val bookPriceOld = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Black,
        fontStyle = Italic,
        fontSize = 18.sp,
        color = colors.costYellow,
        textAlign = TextAlign.Start
    )

    val bookPriceNew = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Black,
        fontStyle = Italic,
        fontSize = 18.sp,
        color = colors.mainGreenLight,
        textAlign = TextAlign.End
    )

    val bookInnerTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Black,
        fontStyle = Italic,
        fontSize = 14.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val professionTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val selectorLabelText = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        color = colors.mainOrangeLight,
        textAlign = TextAlign.Start
    )

    val selectorOptionText = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        color = colors.mainYellowLight,
        textAlign = TextAlign.Start
    )

    val clickableTextUnderlined = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )

    val clickableTextNormal = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Center
    )

    val splashTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 32.sp,
        brush = Brush.linearGradient(
            colors = listOf(colors.mainOrangeLight, colors.mainGreenLight)
        ),
        textAlign = TextAlign.Center
    )

    val splashDescription = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = colors.unselectedNavigationColor,
        textAlign = TextAlign.Center
    )

    val appName = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 32.sp,
        brush = Brush.linearGradient(
            colors = listOf(colors.mainOrangeLight, colors.mainGreenLight)
        ),
        textAlign = TextAlign.Center
    )

    val previewTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = colors.simplyWhite,
        fontStyle = Italic,
        textAlign = TextAlign.Center
    )

    val previewDescription = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = colors.greenLight,
        textAlign = TextAlign.Center
    )

    val authorizationTitle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = colors.simplyWhite,
        fontStyle = Italic,
        textAlign = TextAlign.Center
    )

    val authorizationDescription = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = colors.greenLight,
        textAlign = TextAlign.Center
    )

    val userName = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = colors.simplyWhite,
        textAlign = TextAlign.Start
    )
}

private val LocalAppColors = staticCompositionLocalOf { AppColors() }

private val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("AppTypography are not initialized! All application must be inside AppTheme { ... }")
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colors = remember { AppColors() }
    val fontFamily = getFoolStackFontFamily()

    val typography = remember(fontFamily, colors) {
        AppTypography(fontFamily = fontFamily, colors = colors)
    }

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        content = content
    )
}

object AppTheme {
    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current

    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}

@Composable
fun getFoolStackFontFamily(): FontFamily {
    return FontFamily(
        Font(resource = Res.font.Lato_Black, FontWeight.Black),
        Font(resource = Res.font.Lato_BlackItalic, FontWeight.Black, style = Italic),
        Font(resource = Res.font.Lato_Bold, FontWeight.Bold),
        Font(resource = Res.font.Lato_BoldItalic, FontWeight.Bold, style = Italic),
        Font(resource = Res.font.Lato_Italic, FontWeight.Normal, style = Italic),
        Font(resource = Res.font.Lato_Light, FontWeight.Light),
        Font(resource = Res.font.Lato_LightItalic, FontWeight.Light, style = Italic),
        Font(resource = Res.font.Lato_Regular, FontWeight.ExtraLight)
    )
}