package com.bit.lake.inject

import org.junit.jupiter.api.Test

class FancyRepositoryExtension {
    fun hello() {
        println("Even more Hello, world!")
    }
}

class MyRepository(
    private val fancyRepositoryExtension: FancyRepositoryExtension,
) {
    fun hello() {
        println("Hello, world!")
        fancyRepositoryExtension.hello()
    }
}

class MyService(private val repository: MyRepository) {
    fun hello() {
        repository.hello()
    }
}

object ApplicationRunnerTest {
    @Test
    fun `perform a simple single module dependency injection`() {
        val module = module {
            singleOf<FancyRepositoryExtension>() {
                FancyRepositoryExtension()
            }
            singleOf<MyRepository>() {
                MyRepository(get())
            }
            singleOf<MyService>() {
                MyService(get())
            }
        }
        start(module).get<MyService>().hello()
    }
}
