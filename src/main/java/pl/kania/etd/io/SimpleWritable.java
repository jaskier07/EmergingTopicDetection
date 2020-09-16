package pl.kania.etd.io;

import java.io.FileWriter;
import java.io.IOException;

public abstract class SimpleWritable extends Writable {

    private final StringBuilder sb;
    private final FileOutputProvider fop;

    @Override
    protected FileOutputProvider getFileOutputProvider() {
        return fop;
    }

    public SimpleWritable(FileOutputProvider fop) {
        this.sb = new StringBuilder();
        this.fop = fop;
    }

    protected void append(String text) {
        sb.append(text);
    }

    @Override
    protected void writeToFile(FileWriter fw) throws IOException {
        fw.append(sb);
    }
}
