package becode.sec.common.exception

import picocli.CommandLine.ExitCode

sealed class CommonToolException(override val message: String) : RuntimeException(message)

class CommandFailedException(
    userMessage: String,
    val exitCode: Int
) : CommonToolException(userMessage)


fun failCommand(exitCode: Int, message: String): Nothing = throw CommandFailedException(message, exitCode)
fun failCommand(message: String): Nothing = failCommand(ExitCode.SOFTWARE, message)

fun failCommandOn(condition: Boolean, exitCode: Int, message: String) {
    if (condition) {
        failCommand(exitCode, message)
    }
}
