package com.wellnesscoach.Equipment;

/**
 * Created by Roman on 09.04.2016.
 */
public class Equipment {
    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    private String articleName;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    private String kind;
    private boolean music = false;
    private boolean color = false;
    private boolean sole = false;
    private boolean fragrance = false;
    private boolean herbs = false;

    public Equipment(String name, String art)
    {
        articleName = name;
        kind = art;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean isSole() {
        return sole;
    }

    public void setSole(boolean sole) {
        this.sole = sole;
    }

    public boolean isFragrance() {
        return fragrance;
    }

    public void setFragrance(boolean fragrance) {
        this.fragrance = fragrance;
    }

    public boolean isHerbs() {
        return herbs;
    }

    public void setHerbs(boolean herbs) {
        this.herbs = herbs;
    }


}
