package uk.tim740.skUtilities.convert;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import uk.tim740.skUtilities.skUtilities;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * Created by tim740 on 07/03/16
 */
public class ExprUnicode extends SimpleExpression<String> {
	private Expression<String> str;
    private int ucTy;

	@Override
	@Nullable
	protected String[] get(Event arg0) {
        String out;
        if (ucTy == 0) {
            char ch = str.getSingle(arg0).charAt(0);
            if (ch < 0x10){
                out = "\\u000" + Integer.toHexString(ch);
            }else if (ch < 0x100){
                out = "\\u00" + Integer.toHexString(ch);
            }else if (ch < 0x1000){
                out = "\\u0" + Integer.toHexString(ch);
            }else{
                out = "\\u" + Integer.toHexString(ch);
            }
        }else{
            Properties p = new Properties();
            try {
                p.load(new StringReader("key="+str.getSingle(arg0)));
            } catch (IOException e) {
                skUtilities.prErr(e.getMessage(), getClass().getSimpleName());
            }
            out = p.getProperty("key");
        }
        return new String[]{out};
	}

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] arg0, int arg1, Kleenean arg2, ParseResult arg3) {
        ucTy = arg3.mark;
        str = (Expression<String>) arg0[0];
        return true;
    }
    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
    @Override
    public boolean isSingle() {
        return true;
    }
    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return getClass().getName();
    }
}