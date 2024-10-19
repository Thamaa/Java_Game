package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class Lluvia {
    private Array<Rectangle> rainDropsPos;
    private Array<Integer> rainDropsType;
    private long lastDropTime;
    private long lastSpecialDropTime;
    private Texture gotaBuena;
    private Texture gotaMala;
    private Texture gotaCurativa;
    private Sound dropSound;
    private Music rainMusic;
    private Texture gotaFatal;
    private Poderes poderes;

    public Lluvia(Texture gotaBuena, Texture gotaMala, Poderes poderes, Texture gotaCurativa, Texture gotaFatal, Sound ss, Music mm) {
        rainMusic = mm;
        dropSound = ss;
        this.gotaBuena = gotaBuena;
        this.gotaMala = gotaMala;
        this.poderes = poderes;
        this.gotaCurativa = gotaCurativa;
        this.gotaFatal = gotaFatal;
        lastSpecialDropTime = TimeUtils.nanoTime();
    }

    public void crear() {
        rainDropsPos = new Array<Rectangle>();
        rainDropsType = new Array<Integer>();
        crearGotaDeLluvia();
        rainMusic.setLooping(true);
        rainMusic.play();
    }

    private void crearGotaDeLluvia() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        int tipoGota;
        if (TimeUtils.nanoTime() - lastSpecialDropTime > 4000000000L) {
            if (MathUtils.random(1, 20) == 1) {
                tipoGota = 4;
                raindrop.width = 50;
                raindrop.height = 50;
            } else {
                tipoGota = 3;
                raindrop.width = 64;
                raindrop.height = 64;
            }
            lastSpecialDropTime = TimeUtils.nanoTime();
        } else if (MathUtils.random(1, 500) == 1) {
            tipoGota = 5;
            raindrop.width = 166;
            raindrop.height = 192;
        } else if (MathUtils.random(1, 500) == 1) { // Probabilidad para la gota invencible
            tipoGota = 6;
            raindrop.width = 64;
            raindrop.height = 64;
        } else {
            tipoGota = MathUtils.random(1, 10) < 4 ? 1 : 2;
            raindrop.width = 64;
            raindrop.height = 64;
        }
        rainDropsPos.add(raindrop);
        rainDropsType.add(tipoGota);
        lastDropTime = TimeUtils.nanoTime();
    }

    public void actualizarDibujoLluvia(SpriteBatch batch) {
        for (int i = 0; i < rainDropsPos.size; i++) {
            Rectangle raindrop = rainDropsPos.get(i);
            if (rainDropsType.get(i) == 1) // gota dañina
                batch.draw(gotaMala, raindrop.x, raindrop.y, raindrop.width, raindrop.height);
            else if (rainDropsType.get(i) == 2) // gota buena
                batch.draw(gotaBuena, raindrop.x, raindrop.y, raindrop.width, raindrop.height);
            else if (rainDropsType.get(i) == 3) // gota especial
                batch.draw(poderes.getGotaEspecial(), raindrop.x, raindrop.y, raindrop.width, raindrop.height);
            else if (rainDropsType.get(i) == 4) // gota curativa
                batch.draw(gotaCurativa, raindrop.x, raindrop.y, raindrop.width, raindrop.height);
            else if (rainDropsType.get(i) == 5) // gota fatal
                batch.draw(gotaFatal, raindrop.x, raindrop.y, 128, 128); // Dibuja la gota fatal en 128x128
            else if (rainDropsType.get(i) == 6) // gota invencible
                batch.draw(poderes.getGotaInvencible(), raindrop.x, raindrop.y, raindrop.width, raindrop.height);
        }
    }

    public boolean actualizarMovimiento(Tarro tarro) {
        if (TimeUtils.nanoTime() - lastDropTime > 100000000) crearGotaDeLluvia();
        for (int i = 0; i < rainDropsPos.size; i++) {
            Rectangle raindrop = rainDropsPos.get(i);
            raindrop.y -= 300 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) {
                rainDropsPos.removeIndex(i);
                rainDropsType.removeIndex(i);
            }
            if (raindrop.overlaps(tarro.getArea())) {
                if (rainDropsType.get(i) == 1) { // gota dañina
                    if (!tarro.isInvencible()) {
                        tarro.dañar();
                        if (tarro.getVidas() <= 0)
                            return false; // Game over
                    }
                } else if (rainDropsType.get(i) == 2) { // gota buena
                    tarro.sumarPuntos(10);
                    dropSound.play();
                } else if (rainDropsType.get(i) == 3) { // gota especial
                    tarro.sumarPuntos(20);
                    tarro.aumentarVelocidad(50);
                } else if (rainDropsType.get(i) == 4) { // gota curativa
                    tarro.aumentarVida(1);
                    dropSound.play();
                } else if (rainDropsType.get(i) == 5) { // gota fatal
                    tarro.setVidas(0);
                    return false;
                } else if (rainDropsType.get(i) == 6) { // gota invencible
                    tarro.activarInvencibilidad(5000); // 5 segundos de invencibilidad
                }
                rainDropsPos.removeIndex(i);
                rainDropsType.removeIndex(i);
            }
        }
        return true;
    }

    public void destruir() {/*...*/}
    public void pausar() {/*...*/}
    public void continuar() {/*...*/}
}