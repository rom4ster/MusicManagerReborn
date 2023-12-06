import org.koin.core.context.startKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.inject

expect fun getPlatformName(): String
expect fun getFileDirectory(): String
public inline fun <reified T> inject(qualifier: Qualifier? = null, noinline parametersDefinition: ParametersDefinition = {ParametersHolder(
    listOf<Any?>().toMutableList()
)}) = org.koin.java.KoinJavaComponent.inject<T>(T::class.java, qualifier, parametersDefinition.takeUnless { it().values.isEmpty() })

