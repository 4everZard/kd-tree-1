/****
  * Author:        Paul Rodriguez
  * Created:       3/11/2014
  * Last Updated:  3/14/2014
  */
import java.util.ArrayList;

public class PointSET
{
  private SET<Point2D> points;
    public PointSET()
    {
    points = new SET<Point2D>();
    }
    
    public boolean isEmpty()
    {
        return points.isEmpty();
    }
    
    public int size()
    {
      return points.size();
    }
    
    public void insert(Point2D p)
    {
        points.add(p);
    }
    
    public boolean contains(Point2D p)
    {
      return points.contains(p);
    }
    
    public void draw()
    {
        for (Point2D p : points)
        {
            p.draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect)
    {
      ArrayList<Point2D> inRange = new ArrayList<Point2D>();
      for (Point2D p : points)
      {
        if (rect.contains(p))
        {
          inRange.add(p);
        }
      }
      
      return inRange;
    }
    
    public Point2D nearest(Point2D p)
    {
        Point2D minPoint = null;
        double minDistance = Double.POSITIVE_INFINITY;
        double distance = 0.0;
        for (Point2D point : points)
        {
            distance = p.distanceTo(point);
            if (distance < minDistance)
            {
                minPoint = point;
                minDistance = distance;
            }
        }
        return minPoint;
    }
}