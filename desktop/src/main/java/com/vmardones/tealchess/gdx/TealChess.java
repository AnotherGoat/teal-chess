/*
 * Copyright (C) 2023  Víctor Mardones
 * The full notice can be found at README.md in the root directory.
 */

package com.vmardones.tealchess.gdx;

import static com.badlogic.gdx.graphics.Color.TEAL;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.vmardones.tealchess.game.Game;
import com.vmardones.tealchess.io.PieceLoader;
import com.vmardones.tealchess.io.PieceTheme;
import com.vmardones.tealchess.player.Color;

final class TealChess extends ApplicationAdapter {

    private static final com.badlogic.gdx.graphics.Color LIGHT_COLOR =
            com.badlogic.gdx.graphics.Color.valueOf("#FFCE9E");
    private static final com.badlogic.gdx.graphics.Color DARK_COLOR =
            com.badlogic.gdx.graphics.Color.valueOf("#D18B47");
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
        ScreenUtils.clear(TEAL);

        var files = "abcdefgh";

        var board = game.board();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (var square : board.squares()) {

            var drawColor = square.color() == Color.WHITE ? LIGHT_COLOR : DARK_COLOR;
            var file = files.indexOf(square.coordinate().file()) * 80;
            var rank = (square.coordinate().rank() - 1) * 80;

            shapeRenderer.setColor(drawColor);
            shapeRenderer.rect(file, rank, 80, 80);
        }

        shapeRenderer.end();

        batch.begin();

        for (var piece : board.pieces(Color.WHITE)) {
            batch.draw(
                    textures.get("" + piece.color() + piece.firstChar()),
                    files.indexOf(piece.coordinate().file()) * 80,
                    (piece.coordinate().rank() - 1) * 80);
        }

        for (var piece : board.pieces(Color.BLACK)) {
            batch.draw(
                    textures.get("" + piece.color() + piece.firstChar()),
                    files.indexOf(piece.coordinate().file()) * 80,
                    (piece.coordinate().rank() - 1) * 80);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        // textures.forEach(Texture::dispose);
    }
}
