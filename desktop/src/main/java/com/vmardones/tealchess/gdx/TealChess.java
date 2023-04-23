/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.io.PieceLoader;
import com.vmardones.tealchess.io.PieceTheme;

final class TealChess extends ApplicationAdapter {

    private static final Color LIGHT_COLOR = Color.valueOf("#FFCE9E");
    private static final Color DARK_COLOR = Color.valueOf("#D18B47");
    private static final List<String> pieceCodes =
            List.of("wP", "wN", "wB", "wR", "wQ", "wK", "bP", "bN", "bB", "bR", "bQ", "bK");

    private Game game;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Map<String, Texture> textures;

    @Override
    public void create() {
        game = new Game();

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        batch = new SpriteBatch();

        textures = new HashMap<>();

        textures = pieceCodes.stream()
                .collect(toMap(code -> code, code -> new Texture(PieceLoader.load(PieceTheme.CBURNETT, code))));
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.TEAL);

        var board = game.board();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (var square : board.squares()) {

            var drawColor = square.color().isWhite() ? LIGHT_COLOR : DARK_COLOR;
            var x = square.coordinate().fileIndex() * 80;
            var y = (square.coordinate().rank() - 1) * 80;

            shapeRenderer.setColor(drawColor);
            shapeRenderer.rect(x, y, 80, 80);
        }

        shapeRenderer.end();

        batch.begin();

        for (var square : board.squares()) {

            var piece = square.piece();

            if (piece != null) {
                var x = square.coordinate().fileIndex() * 80 + 4;
                var y = (square.coordinate().rank() - 1) * 80 + 4;
                var texture = textures.get(piece.color() + piece.firstChar());

                batch.setColor(0, 0, 0, 0.1f);
                batch.setShader(null);
                batch.draw(texture, x + 4, y, 76, 76);

                batch.setColor(Color.WHITE);
                batch.setShader(null);
                batch.draw(texture, x, y);
            }
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        // textures.values().forEach(Texture::dispose);
    }
}
