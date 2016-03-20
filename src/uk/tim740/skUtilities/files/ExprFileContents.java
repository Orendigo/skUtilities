package uk.tim740.skUtilities.files;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import uk.tim740.skUtilities.skUtilities;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by tim740 on 20/03/2016
 */
public class ExprFileContents extends SimpleExpression<String>{
	private Expression<String> path;

	@Override
	@Nullable
	protected String[] get(Event arg0) {
        File pth = new File("plugins\\" + path.getSingle(arg0).replaceAll("/", "\\"));
        if (pth.exists()){
            try {
                ArrayList<String> cl = new ArrayList<>();
                String inLi;
                BufferedReader br = new BufferedReader(new FileReader(pth));
                while ((inLi = br.readLine()) != null) {
                    cl.add(inLi);
                }
                br.close();
                String[] out = new String[cl.size()];
                return cl.toArray(out);
            } catch (Exception e) {
                skUtilities.prEW(e.getMessage(), getClass().getSimpleName(), 0);
                return null;
            }
        }else{
            skUtilities.prEW("'" + pth + "' doesn't exist!", getClass().getSimpleName(), 0);
            return null;
        }
	}

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] arg0, int arg1, Kleenean arg2, ParseResult arg3) {
        path = (Expression<String>) arg0[0];
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
