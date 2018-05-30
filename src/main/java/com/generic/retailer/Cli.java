package com.generic.retailer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public final class Cli implements AutoCloseable {

    public static Cli create(String prompt, BufferedReader reader, BufferedWriter writer, LocalDate date) {
        requireNonNull(prompt);
        requireNonNull(reader);
        requireNonNull(writer);
        return new Cli(prompt, reader, writer, date);
    }

    public static Cli create(BufferedReader reader, BufferedWriter writer) {
        return new Cli(">", reader, writer, LocalDate.now());
    }

    private static final Predicate<String> WHITESPACE = Pattern.compile("^\\s{0,}$").asPredicate();

    private final String prompt;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final LocalDate date;

    private Cli(String prompt, BufferedReader reader, BufferedWriter writer, LocalDate date) {
        this.prompt = prompt;
        this.reader = reader;
        this.writer = writer;
        this.date = date;
    }

    private void prompt() throws IOException {
        writeLine(prompt);
    }

    private Optional<String> readLine() throws IOException {
        String line = reader.readLine();
        return line == null || WHITESPACE.test(line) ? Optional.empty() : Optional.of(line);
    }

    private void writeLine(String line) throws IOException {
        writer.write(line);
        writer.newLine();
        writer.flush();
    }

    public void run() throws IOException {
        writeLine("What would you like to buy?");
        prompt();
        Optional<String> line = readLine();
        ITrolley trolley = null;
        while (line.isPresent()) {
            writeLine("Would you like anything else?");
            prompt();
            String item = line.orElse("");
            trolley = new Trolley();
            switch (products.valueOf(item.toUpperCase())) {
                case BOOK:
                    trolley.addItem(new Book());
                    break;
                case CD:
                    trolley.addItem(new CD());
                    break;
                case DVD:
                    trolley.addItem(new DVD());
                    break;
                default:
                    break;
            }
            line = readLine();
        }
        trolley.generateReceipt(writer, date);
    }


    @Override
    public void close() throws Exception {
        reader.close();
        writer.close();
    }

    private enum products {
        BOOK, CD, DVD
    }
}
