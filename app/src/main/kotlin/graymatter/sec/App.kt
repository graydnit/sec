package graymatter.sec

import graymatter.sec.command.*
import graymatter.sec.common.cli.CommandFactory
import graymatter.sec.common.cli.ToolVersionProvider
import graymatter.sec.common.crypto.BinaryEncoding
import graymatter.sec.common.exception.CommandFailedException
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.HelpCommand
import kotlin.system.exitProcess

@Command(
    name = "sec",
    description = [
        "SEC is a configuration companion to Palantir library for handling encrypting/decryption",
        "of various configuration file formats such as YAML, JSON and Java Properties."],
    versionProvider = ToolVersionProvider::class,
    mixinStandardHelpOptions = true,
    subcommands = [
        HelpCommand::class,
        GenerateKey::class,
        EncryptValue::class,
        DecryptValue::class,
        GenerateRandomBytes::class,
        EncryptConfig::class,
        DecryptConfig::class,
    ]
)
object App {

    @JvmStatic
    fun main(args: Array<String>) {
        exitProcess(createCommandLine().execute(* args))
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun createCommandLine(): CommandLine {
        return CommandLine(this, CommandFactory)
            .registerExceptionHandlers()
            .setExpandAtFiles(true)
            .setCaseInsensitiveEnumValuesAllowed(true)
            .setInterpolateVariables(true)
            .registerCommonConverters()
            .setUsageHelpWidth(150)
    }


    private fun CommandLine.registerCommonConverters(): CommandLine = apply {
        registerConverter(BinaryEncoding::class.java, BinaryEncoding.Companion::fromName)
    }

    private fun CommandLine.registerExceptionHandlers(): CommandLine {
        exitCodeExceptionMapper = CommandLine.IExitCodeExceptionMapper {
            (it as? CommandFailedException)?.exitCode ?: CommandLine.ExitCode.SOFTWARE
        }
        return this
    }

}
