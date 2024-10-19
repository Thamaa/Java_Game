package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    private final GameLluviaMenu game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Texture backgroundTexture;
    private Texture playButtonTexture;
    private Texture exitButtonTexture;
    private Rectangle playButtonBounds;
    private Rectangle exitButtonBounds;
    private Music menuMusic; // Música del menú

    public MainMenuScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Imagen de fondo para la pantalla de pausa
        backgroundTexture = new Texture(Gdx.files.internal("mainmenu2.png"));

        // Cargar texturas de botones
        playButtonTexture = new Texture(Gdx.files.internal("StartButton.png")); // Imagen de "Play"
        exitButtonTexture = new Texture(Gdx.files.internal("exitt.png"));  // Imagen de "Exit"

        // Crear límites de los botones para detección de toques
        playButtonBounds = new Rectangle(300, 110, 200, 80); // Posición y tamaño del botón de Play
        exitButtonBounds = new Rectangle(300, 15, 200, 80); // Posición y tamaño del botón de Exit

        // Cargar la música del menú
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Menu.mp3"));
        menuMusic.setLooping(true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 1.0f, 0.5f);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, 800, 480);
        batch.draw(playButtonTexture, playButtonBounds.x, playButtonBounds.y, playButtonBounds.width, playButtonBounds.height);
        batch.draw(exitButtonTexture, exitButtonBounds.x, exitButtonBounds.y, exitButtonBounds.width, exitButtonBounds.height);
        batch.end();

        // Verificar si se toca la pantalla para interactuar con los botones
        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();
            touchY = 480 - touchY; // Invertir coordenadas Y porque Gdx usa un origen de coordenadas distinto

            // Verificar si se presionó el botón de Play
            if (playButtonBounds.contains(touchX, touchY)) {
                menuMusic.pause(); // Pausar la música del menú cuando se inicia el juego
                game.setScreen(new GameScreen(game)); // Cargar la pantalla del juego cuando se presiona "Play"
                dispose();
            }

            // Verificar si se presionó el botón de Exit
            if (exitButtonBounds.contains(touchX, touchY)) {
                Gdx.app.exit(); // Salir del juego
            }
        }
    }

    @Override
    public void show() {
        menuMusic.play(); // Reproducir la música del menú cuando se muestra la pantalla
    }

    @Override
    public void resize(int width, int height) {
        // Si es necesario manejar el cambio de tamaño de la pantalla
    }

    @Override
    public void pause() {
        // No necesario en esta pantalla
    }

    @Override
    public void resume() {
        // No necesario en esta pantalla
    }

    @Override
    public void hide() {
        // Pausar la música si el menú se oculta
        menuMusic.pause();
    }

    @Override
    public void dispose() {
        // Libera los recursos de la textura de fondo y los botones
        backgroundTexture.dispose();
        playButtonTexture.dispose();
        exitButtonTexture.dispose();
        menuMusic.dispose(); // Liberar los recursos de la música del menú
    }
}
