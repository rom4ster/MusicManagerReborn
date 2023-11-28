package com.rom4ster.musicmanagerreborn.util

import io.kotest.common.ExperimentalKotest
import io.kotest.core.names.TestName
import io.kotest.core.spec.style.scopes.AbstractContainerScope
import io.kotest.core.spec.style.scopes.ContainerScope
import io.kotest.core.test.TestType



@JvmName("withDataMap")
suspend fun <T> ContainerScope.withTestTypeData(data: Map<String, T>, test: suspend ContainerScope.(T) -> Unit) {
    data.forEach { (name, t) ->
        registerTest(TestName(name), false, null, TestType.Test) { AbstractContainerScope(this).test(t) }
    }
}
