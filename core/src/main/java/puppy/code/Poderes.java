package puppy.code;

import com.badlogic.gdx.graphics.Texture;

public class Poderes {
    private Texture gotaEspecial;
    private Texture gotaInvencible;

    public Poderes(Texture gotaEspecial, Texture gotaInvencible) {
        this.gotaEspecial = gotaEspecial;
        this.gotaInvencible = gotaInvencible;
    }

    public Texture getGotaEspecial() {
        return gotaEspecial;
    }

    public Texture getGotaInvencible() {
        return gotaInvencible;
    }
}