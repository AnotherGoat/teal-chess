/*
 * Copyright (C) 2022  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package cl.vmardones.chess.io;

import cl.vmardones.chess.engine.piece.Piece;
import java.awt.image.BufferedImage;
import java.io.IOException;
import lombok.Generated;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PieceIconLoader {

  private static final Logger LOG = LoggerFactory.getLogger(PieceIconLoader.class);
  private static final String PIECE_ICON_PATH = "art/pieces";

  @Generated
  private PieceIconLoader() {
    throw new UnsupportedOperationException("You cannot instantiate me!");
  }

  public static @Nullable BufferedImage load(Piece piece, int width, int height) {

    var iconResource = ResourceImporter.get(getIconPath(piece));

    try {
      if (iconResource == null) {
        LOG.warn("Could not load the piece {}", getIconPath(piece));
        return null;
      }

      return SvgImporter.get(iconResource, width, height);
    } catch (IOException e) {
      LOG.warn("Could not load the piece {}", getIconPath(piece));
      return null;
    }
  }

  private static String getIconPath(Piece piece) {
    return "%s/%s%s.svg"
        .formatted(PIECE_ICON_PATH, piece.getAlliance(), piece.toSingleChar().toLowerCase());
  }
}
