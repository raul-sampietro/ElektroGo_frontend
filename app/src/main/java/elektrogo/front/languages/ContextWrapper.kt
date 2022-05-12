package elektrogo.front.languages

import android.os.Build
import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import java.util.*

//Code extracted from https://github.com/kmvignesh. It's a class of type ContextWrapper.class (from Android)
//that updates the current configuration in order to change the language that the user picks for our app. This has to be used because
//the call we were using first, basecontext.resources.updateconfiguration(), is deprecated so we can't use it to change the language used in ElektroGo
class MyContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {

        @Suppress("DEPRECATION")
        fun wrap(ctx: Context, language: String): ContextWrapper {
            var context = ctx
            val config = context.resources.configuration
            val sysLocale: Locale?
            sysLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getSystemLocale(config)
            } else {
                getSystemLocaleLegacy(config)
            }
            if (language != "" && sysLocale.language != language) {
                val locale = Locale(language)
                Locale.setDefault(locale)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    setSystemLocale(config, locale)
                } else {
                    setSystemLocaleLegacy(config, locale)
                }

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context = context.createConfigurationContext(config)
            } else {
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
            }
            return MyContextWrapper(context)
        }

        @Suppress("DEPRECATION")
        private fun getSystemLocaleLegacy(config: Configuration): Locale {
            return config.locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun getSystemLocale(config: Configuration): Locale {
            return config.locales.get(0)
        }

        @Suppress("DEPRECATION")
        private fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
            config.locale = locale
        }

        @TargetApi(Build.VERSION_CODES.N)
        fun setSystemLocale(config: Configuration, locale: Locale) {
            config.setLocale(locale)
        }
    }
}