package com.vidlus.qin_sunset.core;

import java.io.IOException;
import java.io.Writer;

public class IssuedWriter extends Writer {
    private final Issued issued;
    private final Destiny destiny;

    public IssuedWriter(Issued issued, Destiny destiny) {
        super();
        this.issued = issued;
        this.destiny = destiny;
    }

    public static enum Destiny {
        OUT, ERR
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        var line = new String(cbuf, off, len);
        switch (this.destiny) {
            case OUT:
                this.issued.addOutLine(line);
                break;
            case ERR:
                this.issued.addErrLine(line);
                break;
        }
    }

    @Override
    public void flush() throws IOException {}

    @Override
    public void close() throws IOException {}

}
