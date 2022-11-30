package Blatt06;

public class Point {
    public float x;
    public float y;

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float distanceTo(Point p){
        return (float) Math.sqrt((p.x-x)*(p.x-x)+(p.y-y)*(p.y-y));
    }

}
