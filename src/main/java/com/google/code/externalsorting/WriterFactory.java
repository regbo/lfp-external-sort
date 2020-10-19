package com.google.code.externalsorting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public interface WriterFactory {

	public static final WriterFactory DEFAULT = create(null);

	public static WriterFactory create(Runnable newLineCallback) {
		return (file, charset, append) -> {
			return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset)) {

				@Override
				public void newLine() throws IOException {
					super.newLine();
					if (newLineCallback != null)
						newLineCallback.run();
				}
			};
		};
	}

	BufferedWriter apply(File file, Charset charset, boolean append) throws IOException;
}
