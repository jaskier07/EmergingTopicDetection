package pl.kania.etd.analysis.file;

import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.io.Writable;

import java.io.FileWriter;
import java.io.IOException;

public class PeriodWriter extends Writable {

    private final FileOutputProvider fop;
    private final StringBuilder textToWrite;

    public PeriodWriter(FileOutputProvider fop) {
        this.fop = fop;
        this.textToWrite = new StringBuilder();
    }

    @Override
    protected FileOutputProvider getFileOutputProvider() {
        return fop;
    }

    public void appendText(String text) {
        textToWrite.append(text);
    }

    @Override
    protected String getFilename() {
        return "Periods";
    }

    @Override
    protected String getHeader() {
        return "index,start,end,tweetsCount";
    }

    @Override
    protected void writeToFile(FileWriter fw) throws IOException {
        fw.write(textToWrite.toString());
    }
}
