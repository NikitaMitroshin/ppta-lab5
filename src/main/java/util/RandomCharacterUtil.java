package util;

import java.util.Random;
import java.util.Set;

public class RandomCharacterUtil {

    public static Character getRandom(Set<Character> alreadyUsed) {
        Random random = new Random();
        do {
            Character c = (char) (random.nextInt('Z' - 'A') + 'A');
            if (!alreadyUsed.contains(c))
                return c;
        } while (true);
    }
}
