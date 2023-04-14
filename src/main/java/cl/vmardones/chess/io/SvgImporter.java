/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cl.vmardones.chess.ExcludeFromGeneratedReport;
import org.eclipse.jdt.annotation.Nullable;

final class SvgImporter {

    private static final Logger LOG = LogManager.getLogger(SvgImporter.class);
    private static final PNGTranscoder TRANSCODER = new PNGTranscoder();

    static @Nullable BufferedImage get(InputStream inputStream, int width, int height) throws IOException {

        TRANSCODER.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float) width);
        TRANSCODER.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float) height);

        try (inputStream;
                var outputStream = new ByteArrayOutputStream()) {

            var input = new TranscoderInput(inputStream);
            var output = new TranscoderOutput(outputStream);

            TRANSCODER.transcode(input, output);
            outputStream.flush();

            var imageData = outputStream.toByteArray();
            return ImageIO.read(new ByteArrayInputStream(imageData));

        } catch (TranscoderException e) {
            LOG.warn("Failed to transcode the SVG image!", e);
            return null;
        }
    }

    @ExcludeFromGeneratedReport
    private SvgImporter() {
        throw new UnsupportedOperationException("This is an utility class, it cannot be instantiated!");
    }
}
