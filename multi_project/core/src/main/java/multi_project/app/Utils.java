/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package multi_project.app;

import mylib.StringUtils;

public class Utils {
    public static boolean isAllPositiveNumbers(String... str) {
        boolean a = true;
        for (String s : str) {
            if (!StringUtils.isPositiveNumber(s)) {
                a = false;
            }
        }
        return a;
    }
}
