package com.artemchep.keyguard.feature.home.settings.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FontDownload
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.artemchep.keyguard.common.io.launchIn
import com.artemchep.keyguard.common.model.AppFont
import com.artemchep.keyguard.common.usecase.GetFont
import com.artemchep.keyguard.common.usecase.GetFontVariants
import com.artemchep.keyguard.common.usecase.PutFont
import com.artemchep.keyguard.common.usecase.WindowCoroutineScope
import com.artemchep.keyguard.feature.localization.textResource
import com.artemchep.keyguard.platform.LeContext
import com.artemchep.keyguard.res.Res
import com.artemchep.keyguard.ui.FlatDropdown
import com.artemchep.keyguard.ui.FlatItemAction
import com.artemchep.keyguard.ui.FlatItemTextContent
import com.artemchep.keyguard.ui.icons.icon
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.combine
import org.kodein.di.DirectDI
import org.kodein.di.instance

fun settingFontProvider(
    directDI: DirectDI,
) = settingFontProvider(
    getFont = directDI.instance(),
    getFontVariants = directDI.instance(),
    putFont = directDI.instance(),
    windowCoroutineScope = directDI.instance(),
    context = directDI.instance(),
)

fun settingFontProvider(
    getFont: GetFont,
    getFontVariants: GetFontVariants,
    putFont: PutFont,
    windowCoroutineScope: WindowCoroutineScope,
    context: LeContext,
): SettingComponent = combine(
    getFont(),
    getFontVariants(),
) { font, variants ->
    val text = getAppFontTitle(font, context)
    val dropdown = variants
        .map { fontVariant ->
            val actionTitle = getAppFontTitle(fontVariant, context)
            val actionText = getAppFontText(fontVariant, context)
            FlatItemAction(
                title = actionTitle,
                text = actionText,
                onClick = {
                    putFont(fontVariant)
                        .launchIn(windowCoroutineScope)
                },
            )
        }

    SettingIi(
        search = SettingIi.Search(
            group = "font",
            tokens = listOf(
                "font",
                "family",
            ),
        ),
    ) {
        SettingFont(
            text = text,
            dropdown = dropdown,
        )
    }
}

private fun getAppFontTitle(appFont: AppFont?, context: LeContext) = when (appFont) {
    null -> textResource(Res.strings.follow_system_settings, context)
    AppFont.ROBOTO -> "Roboto"
    AppFont.NOTO -> "Noto"
    AppFont.ATKINSON_HYPERLEGIBLE -> "Atkinson Hyperlegible"
}

private fun getAppFontText(appFont: AppFont?, context: LeContext) = when (appFont) {
    null -> null
    AppFont.ROBOTO -> null
    AppFont.NOTO -> null
    AppFont.ATKINSON_HYPERLEGIBLE -> textResource(
        Res.strings.font_atkinson_hyperlegible_text,
        context,
    )
}

@Composable
private fun SettingFont(
    text: String,
    dropdown: List<FlatItemAction>,
) {
    FlatDropdown(
        leading = icon<RowScope>(Icons.Outlined.FontDownload),
        content = {
            FlatItemTextContent(
                title = {
                    Text(
                        text = stringResource(Res.strings.pref_item_font_title),
                    )
                },
                text = {
                    Text(text)
                },
            )
        },
        dropdown = dropdown,
    )
}
