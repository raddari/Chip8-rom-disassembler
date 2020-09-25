package me.raddari.chip8;

import me.raddari.chip8.disassembler.Disassembler;
import me.raddari.chip8.disassembler.Opcode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public final class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
            return;
        }

        var romFile = new File(args[0]);
        if (romFile.isFile()) {
            Disassembler.create()
                    .disassemble(romFile)
                    .stream()
                    .map(Opcode::toAssemblyString)
                    .forEach(System.out::println);
        } else {
            LOGGER.fatal("Argument {} must point to a rom file", romFile.getAbsolutePath());
            printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("Usage: Chip8-rom-disassembler <rom-path>");
    }

}
