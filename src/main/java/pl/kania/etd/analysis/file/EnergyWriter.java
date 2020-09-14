package pl.kania.etd.analysis.file;

import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.io.Writable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EnergyWriter extends Writable {

    private final FileOutputProvider fop;
    private final StringBuilder textToWrite;

    public EnergyWriter(FileOutputProvider fop) {
        this.fop = fop;
        this.textToWrite = new StringBuilder();
    }

    @Override
    protected void writeToFile(FileWriter fw) throws IOException {
        fw.write(textToWrite.toString());
    }

    public void appendText(String text) {
        textToWrite.append(text);
    }

    @Override
    protected FileOutputProvider getFileOutputProvider() {
        return fop;
    }

    @Override
    protected String getFilename() {
        return "Energy writer";
    }

    @Override
    protected String getHeader() {
        return "period,word,energy,tweetsCount";
    }
}
