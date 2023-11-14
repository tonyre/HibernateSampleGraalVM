import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class Sample {

    public static void main(String[] args) throws Exception {
        String string = new ByteBuddy()
                .subclass(Object.class)
                .name("hello.Graal")
                .method(named("toString"))
                .intercept(FixedValue.value("Hello Graal works!!!"))
                .make()
                .load(null)
                .getLoaded()
                .getConstructor()
                .newInstance()
                .toString();

        System.out.println(string);
    }
}

