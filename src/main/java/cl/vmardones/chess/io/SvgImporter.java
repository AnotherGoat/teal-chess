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
import lombok.Generated;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SvgImporter {

  private static final Logger LOG = LoggerFactory.getLogger(SvgImporter.class);

  @Generated
  private SvgImporter() {
    throw new UnsupportedOperationException("You cannot instantiate me!");
  }

  static @Nullable BufferedImage get(InputStream inputStream, int width, int height)
      throws IOException {

    var transcoder = new PNGTranscoder();

    transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float) width);
    transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float) height);

    try (inputStream;
        var outputStream = new ByteArrayOutputStream()) {

      var input = new TranscoderInput(inputStream);
      var output = new TranscoderOutput(outputStream);

      transcoder.transcode(input, output);

      outputStream.flush();

      var imageData = outputStream.toByteArray();
      return ImageIO.read(new ByteArrayInputStream(imageData));

    } catch (TranscoderException e) {
      LOG.error("Failed to transcode the SVG image!", e);
      return null;
    }
  }
}
