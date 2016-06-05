package io.github.mthli.dara.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtils {
    private static final String HASHTAG_LETTERS = "\\p{L}\\p{M}";
    private static final String HASHTAG_NUMERALS = "\\p{Nd}";
    private static final String HASHTAG_SPECIAL_CHARS = "_" + //underscore
            "\\u200c" + // ZERO WIDTH NON-JOINER (ZWNJ)
            "\\u200d" + // ZERO WIDTH JOINER (ZWJ)
            "\\ua67e" + // CYRILLIC KAVYKA
            "\\u05be" + // HEBREW PUNCTUATION MAQAF
            "\\u05f3" + // HEBREW PUNCTUATION GERESH
            "\\u05f4" + // HEBREW PUNCTUATION GERSHAYIM
            "\\uff5e" + // FULLWIDTH TILDE
            "\\u301c" + // WAVE DASH
            "\\u309b" + // KATAKANA-HIRAGANA VOICED SOUND MARK
            "\\u309c" + // KATAKANA-HIRAGANA SEMI-VOICED SOUND MARK
            "\\u30a0" + // KATAKANA-HIRAGANA DOUBLE HYPHEN
            "\\u30fb" + // KATAKANA MIDDLE DOT
            "\\u3003" + // DITTO MARK
            "\\u0f0b" + // TIBETAN MARK INTERSYLLABIC TSHEG
            "\\u0f0c" + // TIBETAN MARK DELIMITER TSHEG BSTAR
            "\\u00b7";  // MIDDLE DOT

    private static final String HASHTAG_LETTERS_NUMERALS = HASHTAG_LETTERS
            + HASHTAG_NUMERALS
            + HASHTAG_SPECIAL_CHARS;
    private static final String HASHTAG_LETTERS_SET = "["
            + HASHTAG_LETTERS
            + "]";
    private static final String HASHTAG_LETTERS_NUMERALS_SET = "["
            + HASHTAG_LETTERS_NUMERALS
            + "]";

    private static Pattern sValidPattern;
    private static Pattern sInvalidPattern;

    public static List<String> getHashTags(String text) {
        List<String> list = new ArrayList<>();
        if (sValidPattern == null) {
            sValidPattern = Pattern.compile("(^|[^&"
                    + HASHTAG_LETTERS_NUMERALS
                    + "])(#|\uFF03)(?!\uFE0F|\u20E3)("
                    + HASHTAG_LETTERS_NUMERALS_SET
                    + "*"
                    + HASHTAG_LETTERS_SET
                    + HASHTAG_LETTERS_NUMERALS_SET
                    + "*)", Pattern.CASE_INSENSITIVE);
        }
        if (sInvalidPattern == null) {
            sInvalidPattern = Pattern.compile("^(?:[#＃]|://)");
        }

        Matcher matcher = sValidPattern.matcher(text);
        while (matcher.find()) {
            String after = text.substring(matcher.end());
            if (!sInvalidPattern.matcher(after).find()) {
                list.add(matcher.group());
            }
        }

        return list;
    }
}
