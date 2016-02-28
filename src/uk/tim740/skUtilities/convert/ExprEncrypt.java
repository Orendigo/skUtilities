package uk.tim740.skUtilities.convert;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import uk.tim740.skUtilities.Main;

import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by tim740 on 23/02/2016
 */
public class ExprEncrypt extends SimpleExpression<String> {
    private Expression<String> string, key, cipher;
    private int type;

    @Override
    @Nullable
    protected String[] get(Event arg0) {
        byte[] iKey = this.key.getSingle(arg0).getBytes();
        String iCipher = this.cipher.getSingle(arg0).toUpperCase();
        String iString = this.string.getSingle(arg0);
        Key Ekey = new SecretKeySpec(iKey, iCipher);
        Cipher c = null;
        try{
            c = Cipher.getInstance(iCipher);
        }catch (Exception e){
            Main.prErr(e.getMessage() + " '"+ cipher +"'", getClass().getSimpleName());
        }
        if (type == 0){
            byte[] encry = new byte[0];
            try{
                if (c != null) {
                    c.init(Cipher.ENCRYPT_MODE, Ekey);
                    encry = c.doFinal(iString.getBytes());
                }
            }catch (Exception e){
                Main.prErr(e.getMessage(), getClass().getSimpleName());
            }
            return new String[]{new BASE64Encoder().encode(encry)};
        }else{
            byte[] decry;
            byte[] cout = new byte[0];
            String out = "";
            try{
                decry = new BASE64Decoder().decodeBuffer(iString);
                if (c != null) {
                    c.init(Cipher.DECRYPT_MODE, Ekey);
                    cout = c.doFinal(decry);
                }
            }catch (Exception e) {
                Main.prErr(e.getMessage(), getClass().getSimpleName());
            }for (int i=0; i<cout.length; i++){
                out = (out + Character.toString((char) new Byte(cout[i]).intValue()));
            }
            return new String[]{out};
        }
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
    @Override
    public boolean isSingle() {
        return true;
    }
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] arg0, int arg1, Kleenean arg2, SkriptParser.ParseResult arg3) {
        type = arg3.mark;
        this.string = (Expression<String>) arg0[0];
        this.cipher = (Expression<String>) arg0[1];
        this.key = (Expression<String>) arg0[2];
        return true;
    }
    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return getClass().getName();
    }
}
