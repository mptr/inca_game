package name.panitz.game.framework;

public class Color {

    private int red;
    private int green;
    private int blue;

    public Color(int hexValue) {
        // Extracting individual RGB components from the hexadecimal value
        this.red = (hexValue >> 16) & 0xFF;
        this.green = (hexValue >> 8) & 0xFF;
        this.blue = hexValue & 0xFF;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    @Override
    public String toString() {
        return "Color [Red: " + red + ", Green: "+green+", Blue: " + blue + "]";
    }
}
