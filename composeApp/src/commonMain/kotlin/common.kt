import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject

expect fun getPlatformName(): String

inline fun <reified T> inject() = org.koin.java.KoinJavaComponent.inject<T>(T::class.java)

