package pt.isel;

public class RectJava {
    private final int width, height;

    public RectJava(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public int getArea() {
        return width * height;
    }
}
