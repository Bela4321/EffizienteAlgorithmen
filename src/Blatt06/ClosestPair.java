package Blatt06;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClosestPair {
    public List<Point> points=new ArrayList<>();

    public ClosestPair(File file){
       try {
           BufferedReader br = new BufferedReader(new FileReader(file));
           String line = br.readLine();
           while (line != null) {
               String[] parts = line.split(" ");
               points.add(new Point(Float.parseFloat(parts[0]), Float.parseFloat(parts[1])));
               line = br.readLine();
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
        points.sort((p1, p2) -> Float.compare(p1.y, p2.y));
    }
    //a)
    public float recurse(int l, int r){
        //base case
        if (l==r) return Float.MAX_VALUE;
        if (l+1==r) return points.get(l).distanceTo(points.get(r));

        //divide
        int m=(l+r)/2;

        //conquer
        float d1=recurse(l,m);
        float d2=recurse(m+1,r);
        float dmin=Math.min(d1, d2);

        float ySep=points.get(m).y;
        List<Point> strip=new ArrayList<>();

        for (int i=m;i>=l;i--){
            if (ySep-points.get(i).y<dmin) strip.add(points.get(i));
        }
        for (int i=m+1;i<=r;i++){
            if (points.get(i).y-ySep<dmin) strip.add(points.get(i));
        }

        //combine
        for (Point p:strip){
            for (Point q:strip){
                if (q.y-p.y<dmin&&p!=q) dmin=Math.min(dmin, p.distanceTo(q));
            }
        }
        return dmin;
    }

    //b)
    public float recurseNew(int l, int r){
        //base case
        if (l==r) return Float.MAX_VALUE;
        if (l+1==r) return points.get(l).distanceTo(points.get(r));

        //divide
        int m=(l+r)/2;

        //conquer
        float d1=recurseNew(l,m);
        float d2=recurseNew(m+1,r);
        float dmin=Math.min(d1, d2);

        float ySep=points.get(m).y;
        List<Point> strip=new ArrayList<>();

        for (int i=m;i>=l;i--){
            if (ySep-points.get(i).y<dmin) strip.add(points.get(i));
            else break;
        }
        for (int i=m+1;i<=r;i++){
            if (points.get(i).y-ySep<dmin) strip.add(points.get(i));
            else break;
        }

        //combine
        for (Point p:strip){
            for (Point q:strip){
                if (q.y-p.y<dmin&&p!=q) dmin=Math.min(dmin, p.distanceTo(q));
            }
        }
        return dmin;
    }

    public float recurseLR(int l, int r){
        //base case
        if (l==r) return Float.MAX_VALUE;
        if (l+1==r) return points.get(l).distanceTo(points.get(r));

        //divide
        int m=(l+r)/2;

        //conquer
        float d1=recurseLR(l,m);
        float d2=recurseLR(m+1,r);
        float dmin=Math.min(d1, d2);

        float ySep=points.get(m).y;
        List<Point> stripl=new ArrayList<>();
        List<Point> stripr=new ArrayList<>();
        for (int i=m;i>=l;i--){
            if (ySep-points.get(i).y<dmin) stripl.add(points.get(i));
            else break;
        }
        for (int i=m+1;i<=r;i++){
            if (points.get(i).y-ySep<dmin) stripr.add(points.get(i));
            else break;
        }

        //combine
        for (Point p:stripl){
            for (Point q:stripr){
                if (q.y-p.y<dmin) dmin=Math.min(dmin, p.distanceTo(q));
            }
        }
        return dmin;
    }


}
