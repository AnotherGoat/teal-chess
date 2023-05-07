/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.io.assets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.GdxRuntimeException;

final class SvgLoader {

    private static final String LOG_TAG = "Assets";
    private static final PNGTranscoder TRANSCODER = new PNGTranscoder();

    public static Pixmap load(String path, int sideSize) {
        return load(path, sideSize, sideSize);
    }

    public static Pixmap load(String path, int width, int height) {
        TRANSCODER.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float) width);
        TRANSCODER.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float) height);

        var file = Gdx.files.internal(path);

        try (var inputStream = file.read();
                var outputStream = new ByteArrayOutputStream()) {
            var input = new TranscoderInput(inputStream);
            var output = new TranscoderOutput(outputStream);

            TRANSCODER.transcode(input, output);

            var pngBytes = outputStream.toByteArray();
            return new Pixmap(pngBytes, 0, pngBytes.length);
        } catch (GdxRuntimeException | IOException | TranscoderException e) {
            Gdx.app.error(LOG_TAG, "Could not load SVG file " + path);
            return new Pixmap(width, height, Pixmap.Format.Alpha);
        }
    }

    private SvgLoader() {}
}
