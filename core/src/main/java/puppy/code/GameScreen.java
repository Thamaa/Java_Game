package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final GameLluviaMenu game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Tarro tarro;
    private Lluvia lluvia;
    private Texture backgroundTexture;
    private float backgroundX;
    private Music music;

    public GameScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        backgroundTexture = new Texture(Gdx.files.internal("fondo2.jpg"));
        backgroundX = 0;
        Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("playerhit.mp3"));
        tarro = new Tarro(new Texture(Gdx.files.internal("bucket.png")), hurtSound);
        Texture gota = new Texture(Gdx.files.internal("battery.png"));
        Texture gotaMala = new Texture(Gdx.files.internal("evilrobot.png"));
        Texture gotaEspecial = new Texture(Gdx.files.internal("dropSpecial.png"));
        Texture gotaCurativa = new Texture(Gdx.files.internal("vidaextra.png"));
        Texture gotaFatal = new Texture(Gdx.files.internal("jefeFinal.png"));
        Texture gotaInvencible = new Texture(Gdx.files.internal("invencible.png")); // Nueva textura para la gota invencible
        music = Gdx.audio.newMusic(Gdx.files.internal("Knockout.mp3"));

        Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        Poderes poderes = new Poderes(gotaEspecial, gotaInvencible); // Pasar la nueva textura al constructor de Poderes
        lluvia = new Lluvia(gota, gotaMala, poderes, gotaCurativa, gotaFatal, dropSound, music);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        tarro.crear();
        lluvia.crear();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundTexture, backgroundX, 0);
        batch.draw(backgroundTexture, backgroundX + backgroundTexture.getWidth(), 0);
        backgroundX -= 100 * delta;
        if (backgroundX <= -backgroundTexture.getWidth()) {
            backgroundX = 0;
        }
        font.draw(batch, "Partes totales: " + tarro.getPuntos(), 5, 475);
        font.draw(batch, "Vidas : " + tarro.getVidas(), 670, 475);
        font.draw(batch, "HighScore : " + game.getHigherScore(), camera.viewportWidth / 2 - 50, 475);
        if (!tarro.estaHerido()) {
            tarro.actualizarMovimiento();
            if (!lluvia.actualizarMovimiento(tarro)) {
                if (game.getHigherScore() < tarro.getPuntos()) {
                    game.setHigherScore(tarro.getPuntos());
                }
                lluvia.pausar();
                music.stop(); // Detener la música del juego
                Music gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("gameover.mp3"));
                gameOverMusic.play();
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GameOverScreen(game));
                    }
                });
            }
        }
        tarro.dibujar(batch);
        lluvia.actualizarDibujoLluvia(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Si es necesario manejar el cambio de tamaño de la pantalla
    }

    @Override
    public void show() {
        lluvia.continuar();
    }

    @Override
    public void hide() {
        // Si es necesario manejar cuando la pantalla se oculta
    }

    @Override
    public void pause() {
        lluvia.pausar();
        game.setScreen(new PausaScreen(game, this));
    }

    @Override
    public void resume() {
        lluvia.continuar();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        tarro.destruir();
        lluvia.destruir();
    }
}