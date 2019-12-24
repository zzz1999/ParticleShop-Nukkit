package cn.zzz1999.ParticleShop.Language;

import cn.zzz1999.ParticleShop.ParticleShop;

import java.util.Objects;

public class TextTranslation {

    private String rawText;
    private String freshText;

    public TextTranslation(String text, String... params) {
        this.rawText = text;
        String str = ParticleShop.getInstance().getFallbackLang().get(text);
        if (!Objects.equals(str, null))
            if (params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    str = str.replaceAll("\\{" + i + "}", String.valueOf(params[i]));
                }
            }
        this.freshText = str;
    }

    public TextTranslation(String text) {
        this.rawText = text;
        this.freshText = ParticleShop.getInstance().getFallbackLang().get(text);
    }

    public String getRawText() {
        return rawText;
    }

    public String getFreshText() {
        return freshText;
    }

    @Override
    public String toString() {
        return this.freshText;
    }
}