package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class Tarro {
    private boolean invencible;
    private long tiempoInvencible;
    private int vidas;
    private boolean herido;
    private long tiempoHerido;
    private long tiempoHeridoMax;
    private Sound sonidoHerido;
    private Texture bucketImage;
    private Rectangle bucket;
    private float velx;
    private int puntos; 

    public Tarro(Texture bucketImage, Sound sonidoHerido) {
        this.bucketImage = bucketImage;
        this.sonidoHerido = sonidoHerido;
        this.vidas = 3;
        this.herido = false;
        this.tiempoHeridoMax = 50;
		puntos = 0;
        this.velx = 400;
        this.bucket = new Rectangle();
        this.bucket.x = 800 / 2 - 64 / 2;
        this.bucket.y = 20;
        this.bucket.width = 64;
        this.bucket.height = 64;
        this.invencible = false;
        this.tiempoInvencible = 0;
    }

	public void crear() {
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 32;
		bucket.height = 64;
 }

    public void aumentarVelocidad(float incremento) {
        velx += incremento;
    }

    public void dañar() {
        if (!isInvencible()) {
            vidas--;
            herido = true;
            tiempoHerido = tiempoHeridoMax;
            sonidoHerido.play();
        }
    }

    public void dibujar(SpriteBatch batch) {
        if (!herido)
            batch.draw(bucketImage, bucket.x, bucket.y);
        else {
            batch.draw(bucketImage, bucket.x, bucket.y + MathUtils.random(-5, 5));
            tiempoHerido--;
            if (tiempoHerido <= 0) herido = false;
        }
    }

    public void actualizarMovimiento() {
        // movimiento desde mouse/touch
        /*if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }*/

        // movimiento desde teclado
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= velx * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += velx * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.A)) bucket.x -= velx * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.D)) bucket.x += velx * Gdx.graphics.getDeltaTime();
        // que no se salga de los bordes izq y der
        if (bucket.x < 0) bucket.x = 0;
        if (bucket.x > 800 - 64) bucket.x = 800 - 64;
    }

    public void destruir() {
        bucketImage.dispose();
    }

    public boolean estaHerido() {
        return herido;
    }

    public Rectangle getArea() {
        return bucket;
    }

	public int getPuntos() {
		return puntos;
	}

    public int getVidas() {
        return vidas;
    }

    public void sumarPuntos(int pp) {
			puntos+=pp;
		}

    public void aumentarVida(int i) {
           vidas+=i;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public void activarInvencibilidad(long duracion) {
        invencible = true;
        tiempoInvencible = TimeUtils.nanoTime() + duracion * 1000000; // Convertir milisegundos a nanosegundos
    }

    public boolean isInvencible() {
        if (invencible && TimeUtils.nanoTime() > tiempoInvencible) {
            invencible = false;
        }
        return invencible;
    }
}
