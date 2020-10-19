package com.google.code.externalsorting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

public interface ReaderFactory {

	public static final ReaderFactory DEFAULT = create(null, false);

	public static final ReaderFactory DEFAULT_GZIP = create(null, true);

	public static ReaderFactory create(Function<String, String> lineModifier) {
		return create(lineModifier, false);
	}

	public static ReaderFactory create(Function<String, String> lineModifier, boolean useGzip) {
		return (file, charset) -> {
			InputStream is = new FileInputStream(file);
			if (useGzip)
				is = new GZIPInputStream(is, 8192);
			return new BufferedReader(new InputStreamReader(is, charset)) {

				@Override
				public String readLine() throws IOException {
					while (true) {
						String line = super.readLine();
						if (line == null || lineModifier == null)
							return line;
						String newLine = lineModifier.apply(line);
						if (newLine != null)
							return newLine;
					}
				}
			};
		};
	}

	BufferedReader apply(File file, Charset charset) throws IOException;
}
