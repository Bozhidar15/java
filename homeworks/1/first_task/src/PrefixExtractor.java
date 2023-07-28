public class PrefixExtractor {
    public static String getLongestCommonPrefix(String[] words) {
        if (words == null) {
            return "";
        }
        int ind = 0, len;
        if (words.length > 0 && !words[0].equals("")) {
            len = words[0].length();
        } else {
            return "";
        }
        for (int i = 1; i < words.length; i++) {
            if (words[i] == null || words[i].equals("")) {
                return "";
            }
            if (words[i].length() < len) {
                len = words[i].length();
                ind = i;
            }
        }

        for (int i = 0; i < len; i++) {
            boolean check = true;

            for (String word : words) {
                if (!word.startsWith(words[ind].substring(0, words[ind].length() - i))) {
                    check = false;
                    break;
                }
            }
            if (check) {
                return words[ind].substring(0, words[ind].length() - i);
            }
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(getLongestCommonPrefix(new String[]{"autocorrect", "autoban","autogta"}));
    }
}