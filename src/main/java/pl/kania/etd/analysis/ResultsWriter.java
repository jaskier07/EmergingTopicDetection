package pl.kania.etd.analysis;

import pl.kania.etd.io.FileOutputProvider;
import pl.kania.etd.io.Writable;

import java.io.FileWriter;
import java.io.IOException;

public class ResultsWriter extends Writable {

    private final FileOutputProvider fop;
    private final StringBuilder sb;

    public ResultsWriter(FileOutputProvider fop) {
        this.fop = fop;
        sb = new StringBuilder();
    }

    public void append(String text) {
        sb.append(text);
    }

    @Override
    protected FileOutputProvider getFileOutputProvider() {
        return fop;
    }

    @Override
    protected String getFilename() {
        return "Results";
    }

    @Override
    protected String getHeader() {
        return "from,to,index,value,word1,word2,word3,word4,word5,word6,word7,word8,word9,word10\n";
    }

    @Override
    protected void writeToFile(FileWriter fw) throws IOException {
        fw.write(sb.toString());
    }
}
