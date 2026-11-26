package bluefaber.content;

import java.io.IOException;
import java.nio.file.*;

/** Command-line entry point for the same check/export steps a visual editor can call. */
public final class Main {
    public static void main(String[] args) {
        try {
            run(args);
        } catch (IOException | RuntimeException error) {
            System.err.println("BlueFaber: " + error.getMessage());
            System.exit(1);
        }
    }

    private static void run(String[] args) throws IOException {
        if (!((args.length == 3 && args[0].equals("check")) ||
                (args.length == 4 && args[0].equals("export")))) {
            throw new IllegalArgumentException("Usage: check <graph.json> <target> or export <graph.json> <target> <new directory>");
        }
        Path source = Path.of(args[1]).toAbsolutePath().normalize();
        var graph = ContentGraph.read(source);
        graph.validate(source.getParent(), args[2]);
        if (args[0].equals("export")) {
            if (!args[2].equals("fabric-1.21.1")) {
                throw new IllegalArgumentException("Export is currently available for fabric-1.21.1");
            }
            new Fabric1211Exporter(graph, source.getParent()).export(Path.of(args[3]).toAbsolutePath());
        }
        System.out.println(graph.name() + ": " + graph.nodes().size() + " nodes checked for " + args[2]);
    }
}
