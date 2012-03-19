/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluenova.fairytailcraft.commands.Util;

import java.lang.annotation.Retention;

/**
 *
 * @author Sven
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Command {
    String[] cmd();
    String[] firstArg();
}
